# OrderMaster 订单要点

## 1. 目标与范围
- 模块：订单主单（OrderMaster）及其子单/明细联动。
- 目标：明确创建、编辑、草稿编辑、草稿提交的业务口径，确保后续丢失会话也能继续开发。

## 2. 已确定业务规则（最终口径）

### 2.1 主单状态（order_master.status）
- 新增枚举：`OrderMasterStatusEnums`
  - `WAIT_AUDIT(0, "待审核")`
  - `WAREHOUSE_PREPARING(1, "仓库准备中")`
  - `PART_SHIPPED(2, "部分已发货")`
  - `FINISHED(3, "订单已完成")`
- 建单默认主单状态：`仓库准备中`（`status=1`）。

### 2.2 审核字段
- `auditStatus/auditUserId/auditTime` 目前视为留置字段。
- 现阶段不启用审核流程控制（后续审核功能上线再启用）。

### 2.3 草稿与库存
- 草稿单（`isDraft=1`）：
  - 保存主单/子单/明细数据。
  - 不触发库存锁定与回退（不改 `stock.lock_qty`）。
- 正式单（`isDraft=0`）：
  - 编辑时按明细差量联动库存。
  - 删除子单时回退库存（仅正式口径）。

### 2.4 正式单编辑门禁
- 仅正式单可走正式编辑接口。
- 仅当主单 `status` 为以下之一可编辑：
  - 待审核
  - 仓库准备中
- 拒绝文案：`仅“待审核/仓库准备中可编辑”`。

## 3. 接口职责划分（Controller 维度）

### 3.1 创建主单
- `POST /order/master/create`
- 用途：建单（草稿/正式都可创建，按 `isDraft` 区分库存行为）。

### 3.2 编辑正式单
- `PUT /order/master/{id}`
- 用途：编辑正式单内容（受状态门禁，且联动库存）。

### 3.3 编辑草稿单
- `PUT /order/master/{id}/draft`
- 用途：仅编辑草稿，不改变生命周期，不联动库存。

### 3.4 草稿提交
- `POST /order/master/{id}/submit`
- 用途：草稿转正式。
- 支持可选 DTO：
  - 有 DTO：先覆盖草稿内容，再提交。
  - 无 DTO：按数据库当前草稿内容提交。

## 4. Service 方法定义（当前口径）

### 4.1 OrderMasterService
- `String createOrderMaster(OrderMasterDTO dto, String userId)`
- `void editOrderMaster(String id, OrderMasterDTO dto)`（正式编辑）
- `void editDraftOrderMaster(String id, OrderMasterDTO dto)`（草稿编辑）
- `void submitOrderMaster(String id, OrderMasterDTO dto)`（草稿提交）

### 4.2 OrderSubService
- `void saveSubOrder(String masterId, String orderNo, List<OrderSubDTO> subOrders, Integer isDraft)`

### 4.3 OrderItemService
- `void saveItems(String masterId, String subId, List<OrderItemDTO> items, Integer isDraft)`
- `void restoreLockQtyWhenDeleteSubOrder(String subId, String warehouseId)`
- `void lockDraftItemsByMasterId(String masterId)`

## 5. 关键流程（实现意图）

### 5.1 createOrderMaster
1. 校验主单与金额数量一致性。
2. 保存主单（`status=仓库准备中`）。
3. 调用 `saveSubOrder(..., isDraft)`：
   - 草稿：仅落数据，不锁库。
   - 正式：落数据并按差量锁库。

### 5.2 editOrderMaster（正式编辑）
1. 查主单并校验：
   - 必须 `isDraft=0`。
   - `status` 必须在 `{待审核, 仓库准备中}`。
2. 校验 DTO。
3. 更新主单。
4. `saveSubOrder(..., isDraft=0)`，联动库存。

### 5.3 editDraftOrderMaster（草稿编辑）
1. 查主单并校验必须 `isDraft=1`。
2. 校验 DTO。
3. 更新主单。
4. `saveSubOrder(..., isDraft=1)`，不联动库存。

### 5.4 submitOrderMaster（草稿提交）
1. 查主单并校验必须 `isDraft=1`。
2. 若有 DTO：
   - 校验 DTO，先按草稿口径更新主单/子单/明细。
3. 若无 DTO：
   - 从 DB 组装当前草稿 DTO，校验。
4. 先按草稿口径保存子单/明细（避免差量逻辑吞掉提交锁库）。
5. 调用 `lockDraftItemsByMasterId(masterId)` 对当前有效子单明细做一次统一锁库（`+qty`）。
6. 更新主单：`isDraft=0`，`status=仓库准备中`。

## 6. 库存联动规则（OrderItem）
- 同 SKU 更新：`delta = newQty - oldQty`，`lockQty += delta`。
- SKU 变更：旧 SKU `-oldQty`，新 SKU `+newQty`。
- 新增：`+qty`；删除：`-qty`。
- 库存不存在时报错；扣减后负数时报错（整事务回滚）。

## 7. 当前已知实现风险/后续待办
- `submitOrderMaster` 的“无 DTO 提交”依赖 `buildOrderMasterDTOFromDb` 组装当前快照，后续可补专用 Mapper 优化查询性能。
- 正式单编辑目前只做了状态门禁，未做更细粒度业务门禁（例如按发货节点、审核节点），后续可扩展。
- 审核字段目前仍未纳入流程控制，审核上线时需补齐：
  - 提交后的 `auditStatus` 规则
  - 审核通过/拒绝后的状态流转
- 建议补测试：
  - 草稿编辑不锁库
  - 正式编辑差量锁库
  - 草稿提交一次性锁库
  - 门禁状态拦截

## 8. 阅读建议（下次续开发）
- 先看：
  - `OrderMasterServiceImpl`
  - `OrderSubServiceImpl`
  - `OrderItemServiceImpl`
  - `StockServiceImpl`
- 再看 Controller 路由是否与前端按钮动作一致（编辑正式/编辑草稿/提交）。
