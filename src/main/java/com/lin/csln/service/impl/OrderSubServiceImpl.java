package com.lin.csln.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.order.OrderItemDTO;
import com.lin.csln.dto.order.OrderSubDTO;
import com.lin.csln.entity.OrderSubDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.enums.OrderSubStatusEnums;
import com.lin.csln.mapper.OrderSubMapper;
import com.lin.csln.service.OrderItemService;
import com.lin.csln.service.OrderSubService;
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
 * 子订单表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class OrderSubServiceImpl extends BaseReadonlyServiceImpl<OrderSubMapper, OrderSubDO> implements OrderSubService {
    private static final String SUB_ORDER_NO_SPLIT = "-C";

    @Resource
    private OrderItemService orderItemService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSubOrder(String masterId, String orderNo, List<OrderSubDTO> subOrders, Integer isDraft) {
        List<OrderSubDO> dbSubOrders = this.list(new LambdaQueryWrapper<OrderSubDO>()
                .eq(OrderSubDO::getMasterId, masterId)
                .eq(OrderSubDO::getIsDelete, GlobalEnums.NO.getCode()));

        Map<String, OrderSubDO> dbSubMap = new HashMap<>();
        for (OrderSubDO dbSubOrder : dbSubOrders) {
            dbSubMap.put(dbSubOrder.getId(), dbSubOrder);
        }

        List<OrderSubDTO> safeSubOrders = subOrders == null ? new ArrayList<>() : subOrders;
        Set<String> requestIdSet = new HashSet<>();
        List<String> needDeleteIds = new ArrayList<>();
        List<OrderSubDO> saveOrUpdateList = new ArrayList<>();
        List<OrderItemRef> itemRefs = new ArrayList<>();
        int nextSeq = 0;

        for (OrderSubDTO subDTO : safeSubOrders) {
            if (subDTO == null) {
                continue;
            }
            if (StringUtils.hasText(subDTO.getId())) {
                String subId = subDTO.getId();
                if (!requestIdSet.add(subId)) {
                    throw new BusinessException("子单ID重复：" + subId);
                }
                OrderSubDO dbSubOrder = dbSubMap.get(subId);
                if (dbSubOrder == null) {
                    throw new BusinessException("子单不存在或不属于当前主单，ID：" + subId);
                }

                OrderSubDO updateSubOrder = new OrderSubDO();
                BeanUtil.copyProperties(subDTO, updateSubOrder);
                updateSubOrder.setId(subId);
                updateSubOrder.setMasterId(masterId);
                updateSubOrder.setSubOrderNo(dbSubOrder.getSubOrderNo());
                updateSubOrder.setStatus(dbSubOrder.getStatus());
                updateSubOrder.setIsDelete(GlobalEnums.NO.getCode());
                saveOrUpdateList.add(updateSubOrder);
                itemRefs.add(new OrderItemRef(subId, subDTO.getItems()));
                continue;
            }

            if (nextSeq == 0) {
                nextSeq = parseSubOrderSeq(baseMapper.selectMaxSubOrderNoByMasterIdAndPrefix(masterId, orderNo + SUB_ORDER_NO_SPLIT), orderNo) + 1;
            }

            OrderSubDO newSubOrder = new OrderSubDO();
            BeanUtil.copyProperties(subDTO, newSubOrder);
            newSubOrder.setMasterId(masterId);
            newSubOrder.setSubOrderNo(orderNo + SUB_ORDER_NO_SPLIT + String.format("%03d", nextSeq));
            newSubOrder.setStatus(OrderSubStatusEnums.WAIT_ALLOCATE.getCode());
            newSubOrder.setIsDelete(GlobalEnums.NO.getCode());
            saveOrUpdateList.add(newSubOrder);
            itemRefs.add(new OrderItemRef(null, subDTO.getItems()));
            nextSeq++;
        }

        for (OrderSubDO dbSubOrder : dbSubOrders) {
            if (!requestIdSet.contains(dbSubOrder.getId())) {
                needDeleteIds.add(dbSubOrder.getId());
            }
        }

        handleDeletedSubOrders(dbSubOrders, needDeleteIds, isDraft);

        if (!CollectionUtils.isEmpty(saveOrUpdateList)) {
            this.saveOrUpdateBatch(saveOrUpdateList);
        }

        for (int i = 0; i < itemRefs.size(); i++) {
            OrderItemRef ref = itemRefs.get(i);
            String subId = ref.subId;
            if (!StringUtils.hasText(subId) && i < saveOrUpdateList.size()) {
                subId = saveOrUpdateList.get(i).getId();
            }
            if (!StringUtils.hasText(subId)) {
                throw new BusinessException("子单保存失败，未获取到子单ID");
            }
            orderItemService.saveItems(masterId, subId, ref.items, isDraft);
        }
    }

    private int parseSubOrderSeq(String subOrderNo, String orderNo) {
        String subOrderPrefix = orderNo + SUB_ORDER_NO_SPLIT;
        if (!StringUtils.hasText(subOrderNo) || !subOrderNo.startsWith(subOrderPrefix)) {
            return 0;
        }
        String seqPart = subOrderNo.substring(subOrderPrefix.length());
        if (seqPart.length() != 3 || !seqPart.chars().allMatch(Character::isDigit)) {
            return 0;
        }
        return Integer.parseInt(seqPart);
    }

    private void handleDeletedSubOrders(List<OrderSubDO> dbSubOrders, List<String> needDeleteIds, Integer isDraft) {
        if (CollectionUtils.isEmpty(needDeleteIds)) {
            return;
        }
        if (!GlobalEnums.YES.getCode().equals(isDraft)) {
            for (OrderSubDO dbSubOrder : dbSubOrders) {
                if (needDeleteIds.contains(dbSubOrder.getId())) {
                    orderItemService.restoreLockQtyWhenDeleteSubOrder(dbSubOrder.getId(), dbSubOrder.getWarehouseId());
                }
            }
        }
        this.update(new LambdaUpdateWrapper<OrderSubDO>()
                .in(OrderSubDO::getId, needDeleteIds)
                .set(OrderSubDO::getIsDelete, GlobalEnums.YES.getCode()));
    }

    private static class OrderItemRef {
        private final String subId;
        private final List<OrderItemDTO> items;

        private OrderItemRef(String subId, List<OrderItemDTO> items) {
            this.subId = subId;
            this.items = items;
        }
    }

}
