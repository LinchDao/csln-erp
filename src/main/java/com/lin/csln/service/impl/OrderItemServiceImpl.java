package com.lin.csln.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.order.OrderItemDetailRespDTO;
import com.lin.csln.dto.order.OrderItemDTO;
import com.lin.csln.entity.OrderItemDO;
import com.lin.csln.entity.OrderSubDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.OrderItemMapper;
import com.lin.csln.mapper.OrderSubMapper;
import com.lin.csln.service.OrderItemService;
import com.lin.csln.service.StockService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 订单明细表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class OrderItemServiceImpl extends BaseReadonlyServiceImpl<OrderItemMapper, OrderItemDO> implements OrderItemService {
    @Resource
    private OrderSubMapper orderSubMapper;
    @Resource
    private StockService stockService;

    @Override
    public List<OrderItemDO> listBySubId(String subId) {
        return this.list(new LambdaQueryWrapper<OrderItemDO>().eq(OrderItemDO::getSubId, subId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveItems(String masterId, String subId, List<OrderItemDTO> items, Integer isDraft) {
        if (!StringUtils.hasText(masterId) || !StringUtils.hasText(subId)) {
            throw new BusinessException("母单ID和子单ID不能为空");
        }
        if (CollectionUtils.isEmpty(items)) {
            throw new BusinessException("子单明细不能为空");
        }
        OrderSubDO subOrder = orderSubMapper.selectById(subId);
        if (subOrder == null
                || !masterId.equals(subOrder.getMasterId())
                || !GlobalEnums.NO.getCode().equals(subOrder.getIsDelete())) {
            throw new BusinessException("子单不存在或不属于当前母单，ID：" + subId);
        }
        String warehouseId = subOrder.getWarehouseId();
        if (!StringUtils.hasText(warehouseId)) {
            throw new BusinessException("子单未配置发货仓库，ID：" + subId);
        }
        boolean shouldAdjustLockQty = !GlobalEnums.YES.getCode().equals(isDraft);

        List<OrderItemDO> dbItems = this.listBySubId(subId);
        Map<String, OrderItemDO> dbItemMap = new HashMap<>();
        for (OrderItemDO dbItem : dbItems) {
            dbItemMap.put(dbItem.getId(), dbItem);
        }

        Set<String> requestItemIdSet = new HashSet<>();
        List<OrderItemDO> saveOrUpdateList = new ArrayList<>();

        for (OrderItemDTO itemDTO : items) {
            if (itemDTO == null) {
                throw new BusinessException("子单明细不能为空");
            }
            if (!StringUtils.hasText(itemDTO.getSkuId())) {
                throw new BusinessException("SKU不能为空");
            }
            if (itemDTO.getQty() == null || itemDTO.getQty() <= 0) {
                throw new BusinessException("明细数量必须大于0");
            }

            String itemId = itemDTO.getId();
            if (StringUtils.hasText(itemId)) {
                if (!requestItemIdSet.add(itemId)) {
                    throw new BusinessException("子单明细ID重复：" + itemId);
                }

                OrderItemDO dbItem = dbItemMap.get(itemId);
                if (dbItem == null) {
                    throw new BusinessException("订单明细不存在或不属于当前子单，ID：" + itemId);
                }

                if (shouldAdjustLockQty) {
                    if (itemDTO.getSkuId().equals(dbItem.getSkuId())) {
                        stockService.adjustLockQty(warehouseId, itemDTO.getSkuId(), itemDTO.getQty() - dbItem.getQty());
                    } else {
                        stockService.adjustLockQty(warehouseId, dbItem.getSkuId(), -dbItem.getQty());
                        stockService.adjustLockQty(warehouseId, itemDTO.getSkuId(), itemDTO.getQty());
                    }
                }

                OrderItemDO updateItem = new OrderItemDO();
                BeanUtil.copyProperties(itemDTO, updateItem);
                updateItem.setId(itemId);
                updateItem.setMasterId(masterId);
                updateItem.setSubId(subId);
                saveOrUpdateList.add(updateItem);
                dbItemMap.remove(itemId);
                continue;
            }

            if (shouldAdjustLockQty) {
                stockService.adjustLockQty(warehouseId, itemDTO.getSkuId(), itemDTO.getQty());
            }

            OrderItemDO newItem = new OrderItemDO();
            BeanUtil.copyProperties(itemDTO, newItem);
            newItem.setMasterId(masterId);
            newItem.setSubId(subId);
            saveOrUpdateList.add(newItem);
        }

        if (shouldAdjustLockQty) {
            for (OrderItemDO deletedItem : dbItemMap.values()) {
                stockService.adjustLockQty(warehouseId, deletedItem.getSkuId(), -deletedItem.getQty());
            }
        }

        if (!CollectionUtils.isEmpty(saveOrUpdateList)) {
            this.saveOrUpdateBatch(saveOrUpdateList);
        }
        if (!CollectionUtils.isEmpty(dbItemMap)) {
            this.removeByIds(dbItemMap.keySet());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreLockQtyWhenDeleteSubOrder(String subId, String warehouseId) {
        if (!StringUtils.hasText(subId) || !StringUtils.hasText(warehouseId)) {
            throw new BusinessException("子单ID和仓库ID不能为空");
        }
        List<OrderItemDO> deleteSubItems = this.list(new LambdaQueryWrapper<OrderItemDO>()
                .eq(OrderItemDO::getSubId, subId));
        for (OrderItemDO itemDO : deleteSubItems) {
            stockService.adjustLockQty(warehouseId, itemDO.getSkuId(), -itemDO.getQty());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockDraftItemsByMasterId(String masterId) {
        if (!StringUtils.hasText(masterId)) {
            throw new BusinessException("母单ID不能为空");
        }
        List<OrderSubDO> subOrders = orderSubMapper.selectList(new LambdaQueryWrapper<OrderSubDO>()
                .eq(OrderSubDO::getMasterId, masterId)
                .eq(OrderSubDO::getIsDelete, GlobalEnums.NO.getCode()));
        for (OrderSubDO subOrder : subOrders) {
            if (!StringUtils.hasText(subOrder.getWarehouseId())) {
                throw new BusinessException("子单未配置发货仓库，ID：" + subOrder.getId());
            }
            List<OrderItemDO> itemList = this.listBySubId(subOrder.getId());
            for (OrderItemDO itemDO : itemList) {
                stockService.adjustLockQty(subOrder.getWarehouseId(), itemDO.getSkuId(), itemDO.getQty());
            }
        }
    }

    @Override
    public List<OrderItemDetailRespDTO> listDetailByMasterId(String masterId) {
        if (!StringUtils.hasText(masterId)) {
            throw new BusinessException("母单ID不能为空");
        }
        return baseMapper.selectDetailListByMasterId(masterId);
    }

    @Override
    public List<OrderItemDetailRespDTO> listDetailBySubId(String subId) {
        if (!StringUtils.hasText(subId)) {
            throw new BusinessException("子单ID不能为空");
        }
        return baseMapper.selectDetailListBySubId(subId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Integer> completePickingAndGetActualQtyMap(String subId) {
        if (!StringUtils.hasText(subId)) {
            throw new BusinessException("子单ID不能为空");
        }
        List<OrderItemDO> itemList = this.listBySubId(subId);
        if (CollectionUtils.isEmpty(itemList)) {
            throw new BusinessException("子单明细不能为空");
        }

        Map<String, Integer> skuActualQtyMap = new HashMap<>();
        List<OrderItemDO> updateList = new ArrayList<>();
        for (OrderItemDO itemDO : itemList) {
            Integer qty = itemDO.getQty();
            if (qty == null || qty <= 0) {
                throw new BusinessException("子单明细数量异常，明细ID：" + itemDO.getId());
            }

            // TODO 分批配货功能开发时，此处改为使用前端传入的发货明细 actualQty（按 itemId 维度）并做差量处理。
            int actualQty = qty;
            skuActualQtyMap.merge(itemDO.getSkuId(), actualQty, Integer::sum);

            if (!Integer.valueOf(actualQty).equals(itemDO.getActualQty())) {
                OrderItemDO updateItem = new OrderItemDO();
                updateItem.setId(itemDO.getId());
                updateItem.setActualQty(actualQty);
                updateList.add(updateItem);
            }
        }

        if (!CollectionUtils.isEmpty(updateList)) {
            this.updateBatchById(updateList);
        }
        return skuActualQtyMap;
    }
}
