package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.purchase.order.PurchaseOrderItemDTO;
import com.lin.csln.entity.PurchaseOrderItemDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.PurchaseOrderItemMapper;
import com.lin.csln.service.PurchaseOrderItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 采购明细表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class PurchaseOrderItemServiceImpl extends BaseReadonlyServiceImpl<PurchaseOrderItemMapper, PurchaseOrderItemDO> implements PurchaseOrderItemService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePurchaseOrderItem(String orderId, List<PurchaseOrderItemDTO> purchaseOrderItem) {

        if (CollectionUtils.isEmpty(purchaseOrderItem)) {
            throw new BusinessException("采购明细未填写。");
        }

        List<PurchaseOrderItemDO> oldItemList = this.list(new LambdaQueryWrapper<PurchaseOrderItemDO>()
                .eq(PurchaseOrderItemDO::getPurchaseId, orderId));

        Map<String, PurchaseOrderItemDO> oldItemMap = oldItemList.stream()
                .collect(Collectors.toMap(
                        item -> item.getPurchaseId() + "_" + item.getSkuId(),
                        item -> item,
                        (oldVal, newVal) -> oldVal
                ));

        List<PurchaseOrderItemDO> saveOrUpdateList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(purchaseOrderItem)) {
            for (PurchaseOrderItemDTO itemDTO : purchaseOrderItem) {
                PurchaseOrderItemDO itemDO = new PurchaseOrderItemDO();
                BeanUtils.copyProperties(itemDTO, itemDO);
                itemDO.setPurchaseId(orderId);
                itemDO.setIsDelete(GlobalEnums.NO.getCode());
                String key = orderId + "_" + itemDTO.getSkuId();

                if (oldItemMap.containsKey(key)) {
                    PurchaseOrderItemDO oldItem = oldItemMap.get(key);
                    itemDO.setId(oldItem.getId());
                }

                saveOrUpdateList.add(itemDO);
            }
        }

        // 批量保存或更新
        if (!CollectionUtils.isEmpty(saveOrUpdateList)) {
            this.saveOrUpdateBatch(saveOrUpdateList);
        }

        // 执行软删除
        if (!CollectionUtils.isEmpty(oldItemList) && !CollectionUtils.isEmpty(purchaseOrderItem)) {
            Set<String> currentSkuIds = purchaseOrderItem.stream()
                    .map(PurchaseOrderItemDTO::getSkuId)
                    .collect(Collectors.toSet());

            List<String> needDeleteIds = oldItemList.stream()
                    .filter(old -> !currentSkuIds.contains(old.getSkuId()))
                    .map(PurchaseOrderItemDO::getId)
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(needDeleteIds)) {
                this.update(new LambdaUpdateWrapper<PurchaseOrderItemDO>()
                        .in(PurchaseOrderItemDO::getId, needDeleteIds)
                        .set(PurchaseOrderItemDO::getIsDelete, GlobalEnums.YES.getCode()));
            }
        }
    }

    @Override
    public List<PurchaseOrderItemDTO> listPurchaseOrderItemSku(String purchaseId) {
        List<PurchaseOrderItemDTO> itemList = baseMapper.listPurchaseOrderItemSku(purchaseId);
        return itemList;
    }

    @Override
    public List<PurchaseOrderItemDO> listOrderItemList(String purchaseId) {
        LambdaQueryWrapper<PurchaseOrderItemDO> itemQuery = new LambdaQueryWrapper<>();
        itemQuery.eq(PurchaseOrderItemDO::getPurchaseId, purchaseId);
        itemQuery.eq(PurchaseOrderItemDO::getIsDelete, GlobalEnums.NO.getCode());
        return baseMapper.selectList(itemQuery);
    }
}
