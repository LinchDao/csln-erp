
INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000001', 'user:create', '用户-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'user:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000002', 'user:update', '用户-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'user:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000003', 'user:view', '用户-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'user:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000004', 'user:delete', '用户-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'user:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000005', 'user:list', '用户-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'user:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000006', 'user:export', '用户-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'user:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000007', 'user:audit', '用户-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'user:audit'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000008', 'role:create', '角色-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'role:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000009', 'role:update', '角色-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'role:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000010', 'role:view', '角色-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'role:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000011', 'role:delete', '角色-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'role:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000012', 'role:list', '角色-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'role:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000013', 'role:export', '角色-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'role:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000014', 'role:audit', '角色-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'role:audit'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000015', 'dict:create', '字典-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'dict:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000016', 'dict:update', '字典-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'dict:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000017', 'dict:view', '字典-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'dict:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000018', 'dict:delete', '字典-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'dict:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000019', 'dict:list', '字典-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'dict:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000020', 'dict:export', '字典-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'dict:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000021', 'dict:audit', '字典-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'dict:audit'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000022', 'menu:update', '菜单-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'menu:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000023', 'product:create', '商品-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'product:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000024', 'product:update', '商品-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'product:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000025', 'product:view', '商品-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'product:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000026', 'product:delete', '商品-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'product:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000027', 'product:list', '商品-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'product:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000028', 'product:export', '商品-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'product:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000029', 'product:audit', '商品-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'product:audit'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000030', 'customer:create', '客户-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'customer:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000031', 'customer:update', '客户-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'customer:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000032', 'customer:view', '客户-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'customer:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000033', 'customer:delete', '客户-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'customer:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000034', 'customer:list', '客户-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'customer:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000035', 'customer:export', '客户-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'customer:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000036', 'customer:audit', '客户-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'customer:audit'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000037', 'shop:create', '门店-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'shop:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000038', 'shop:update', '门店-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'shop:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000039', 'shop:view', '门店-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'shop:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000040', 'shop:delete', '门店-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'shop:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000041', 'shop:list', '门店-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'shop:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000042', 'shop:export', '门店-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'shop:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000043', 'shop:audit', '门店-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'shop:audit'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000044', 'warehouse:create', '仓库-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'warehouse:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000045', 'warehouse:update', '仓库-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'warehouse:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000046', 'warehouse:view', '仓库-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'warehouse:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000047', 'warehouse:delete', '仓库-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'warehouse:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000048', 'warehouse:list', '仓库-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'warehouse:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000049', 'warehouse:export', '仓库-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'warehouse:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000050', 'warehouse:audit', '仓库-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'warehouse:audit'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000051', 'order-master:create', '订单主单-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-master:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000052', 'order-master:update', '订单主单-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-master:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000053', 'order-master:view', '订单主单-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-master:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000054', 'order-master:delete', '订单主单-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-master:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000055', 'order-master:list', '订单主单-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-master:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000056', 'order-master:export', '订单主单-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-master:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000057', 'order-master:audit', '订单主单-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-master:audit'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000058', 'order-sub:create', '订单子单-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-sub:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000059', 'order-sub:update', '订单子单-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-sub:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000060', 'order-sub:view', '订单子单-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-sub:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000061', 'order-sub:delete', '订单子单-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-sub:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000062', 'order-sub:list', '订单子单-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-sub:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000063', 'order-sub:export', '订单子单-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-sub:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000064', 'order-sub:audit', '订单子单-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-sub:audit'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000065', 'order-sub:assign', '订单子单-分配'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'order-sub:assign'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000066', 'purchase-order:create', '采购单-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-order:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000067', 'purchase-order:update', '采购单-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-order:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000068', 'purchase-order:view', '采购单-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-order:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000069', 'purchase-order:delete', '采购单-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-order:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000070', 'purchase-order:list', '采购单-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-order:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000071', 'purchase-order:export', '采购单-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-order:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000072', 'purchase-order:audit', '采购单-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-order:audit'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000073', 'purchase-in:create', '入库单-创建'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-in:create'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000074', 'purchase-in:update', '入库单-修改'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-in:update'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000075', 'purchase-in:view', '入库单-查看'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-in:view'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000076', 'purchase-in:delete', '入库单-删除'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-in:delete'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000077', 'purchase-in:list', '入库单-列表'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-in:list'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000078', 'purchase-in:export', '入库单-导出'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-in:export'
);

INSERT INTO sys_permission (id, perm_code, perm_name)
SELECT '20260415000000000000000000000079', 'purchase-in:audit', '入库单-审核'
WHERE NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE perm_code = 'purchase-in:audit'
);
