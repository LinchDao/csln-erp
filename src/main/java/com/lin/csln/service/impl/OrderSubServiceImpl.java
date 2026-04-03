package com.lin.csln.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.order.*;
import com.lin.csln.enums.DeliveryTypeEnums;
import com.lin.csln.entity.OrderSubDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.enums.OrderSubStatusEnums;
import com.lin.csln.mapper.OrderSubMapper;
import com.lin.csln.service.OrderItemService;
import com.lin.csln.service.OrderMasterService;
import com.lin.csln.service.OrderSubService;
import com.lin.csln.service.StockService;
import com.lin.csln.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
    @Resource
    private UserService userService;
    @Resource
    private StockService stockService;
    @Lazy
    @Resource
    private OrderMasterService orderMasterService;


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

    @Override
    public List<OrderSubDetailRespDTO> listDetailByMasterId(String masterId) {
        if (!StringUtils.hasText(masterId)) {
            throw new BusinessException("母单ID不能为空");
        }
        return baseMapper.selectDetailListByMasterId(masterId);
    }

    @Override
    public PageRespDTO<OrderSubPageRespDTO> pageQuery(OrderSubQueryParamDTO queryDTO) {
        IPage<OrderSubPageRespDTO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        IPage<OrderSubPageRespDTO> resultPage = baseMapper.pageQuery(page, queryDTO);

        Set<String> userIds = new HashSet<>();
        for (OrderSubPageRespDTO respDTO : resultPage.getRecords()) {
            if (StringUtils.hasText(respDTO.getCreateUserId())) {
                userIds.add(respDTO.getCreateUserId());
            }
            if (StringUtils.hasText(respDTO.getPickerUserId())) {
                userIds.add(respDTO.getPickerUserId());
            }
        }

        if (!CollectionUtils.isEmpty(userIds)) {
            Map<String, String> userNameMap = userService.getUserNamesByIds(userIds);
            for (OrderSubPageRespDTO respDTO : resultPage.getRecords()) {
                respDTO.setCreateUserName(userNameMap.get(respDTO.getCreateUserId()));
                respDTO.setPickerUserName(userNameMap.get(respDTO.getPickerUserId()));
            }
        }

        return PageRespDTO.build(resultPage, queryDTO);
    }

    @Override
    public OrderSubDetailDTO getDetail(String subId) {
        if (!StringUtils.hasText(subId)) {
            throw new BusinessException("子订单ID不能为空");
        }

        OrderSubDetailDTO detailDTO = baseMapper.selectDetailBySubId(subId);
        if (detailDTO == null) {
            throw new BusinessException("子订单不存在");
        }

        detailDTO.setItems(orderItemService.listDetailBySubId(subId));

        if (StringUtils.hasText(detailDTO.getPickerUserId())) {
            Set<String> userIds = new HashSet<>();
            userIds.add(detailDTO.getPickerUserId());
            Map<String, String> userNameMap = userService.getUserNamesByIds(userIds);
            detailDTO.setPickerUserName(userNameMap.get(detailDTO.getPickerUserId()));
        }

        return detailDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignOrderToUser(String orderSubId, String userId) {
        if (!StringUtils.hasText(orderSubId)) {
            throw new BusinessException("子订单ID不能为空");
        }
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException("用户ID不能为空");
        }

        OrderSubDO orderSubDO = this.getOne(new LambdaQueryWrapper<OrderSubDO>()
                .eq(OrderSubDO::getId, orderSubId)
                .eq(OrderSubDO::getIsDelete, GlobalEnums.NO.getCode()));
        if (orderSubDO == null) {
            throw new BusinessException("子订单不存在");
        }

        OrderSubDO updateDO = new OrderSubDO();
        updateDO.setId(orderSubId);
        updateDO.setPickerUserId(userId);
        if (OrderSubStatusEnums.WAIT_ALLOCATE.getCode().equals(orderSubDO.getStatus())) {
            updateDO.setStatus(OrderSubStatusEnums.PICKING.getCode());
        }
        this.updateById(updateDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completePicking(OrderSubPickingCompleteDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getOrderSubId())) {
            throw new BusinessException("子订单ID不能为空");
        }
        OrderSubDO orderSubDO = getSubOrderOrThrow(dto.getOrderSubId());
        if (!OrderSubStatusEnums.PICKING.getCode().equals(orderSubDO.getStatus())) {
            throw new BusinessException("仅“配货中”状态可操作配货完成");
        }
        if (!StringUtils.hasText(orderSubDO.getWarehouseId())) {
            throw new BusinessException("子单未配置发货仓库");
        }

        Map<String, Integer> skuActualQtyMap = orderItemService.completePickingAndGetActualQtyMap(orderSubDO.getId());
        for (Map.Entry<String, Integer> entry : skuActualQtyMap.entrySet()) {
            stockService.consumeLockedStock(orderSubDO.getWarehouseId(), entry.getKey(), entry.getValue());
        }

        OrderSubDO updateDO = new OrderSubDO();
        updateDO.setId(orderSubDO.getId());
        updateDO.setStatus(OrderSubStatusEnums.PICKED.getCode());
        this.updateById(updateDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ship(OrderSubShipDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getOrderSubId())) {
            throw new BusinessException("子订单ID不能为空");
        }
        OrderSubDO orderSubDO = getSubOrderOrThrow(dto.getOrderSubId());

        if (OrderSubStatusEnums.PICKING.getCode().equals(orderSubDO.getStatus())) {
            OrderSubPickingCompleteDTO completeDTO = new OrderSubPickingCompleteDTO();
            completeDTO.setOrderSubId(dto.getOrderSubId());
            completePicking(completeDTO);
            orderSubDO = getSubOrderOrThrow(dto.getOrderSubId());
        }
        if (!OrderSubStatusEnums.PICKED.getCode().equals(orderSubDO.getStatus())) {
            throw new BusinessException("仅“配货完成”状态可发货");
        }

        Integer effectiveDeliveryType = dto.getDeliveryType() != null ? dto.getDeliveryType() : orderSubDO.getDeliveryType();
        String effectiveExpressNo = StringUtils.hasText(dto.getExpressNo()) ? dto.getExpressNo() : orderSubDO.getExpressNo();
        String effectiveDriverPhone = StringUtils.hasText(dto.getDriverPhone()) ? dto.getDriverPhone() : orderSubDO.getDriverPhone();
        if (effectiveDeliveryType == null) {
            throw new BusinessException("配送方式不能为空");
        }
        DeliveryTypeEnums deliveryTypeEnums = DeliveryTypeEnums.getByCode(effectiveDeliveryType);
        if (deliveryTypeEnums == null) {
            throw new BusinessException("配送方式不合法");
        }
        if (DeliveryTypeEnums.EXPRESS.equals(deliveryTypeEnums) && !StringUtils.hasText(effectiveExpressNo)) {
            throw new BusinessException("快递配送必须填写快递单号");
        }
        if ((DeliveryTypeEnums.INSTANT_FREIGHT.equals(deliveryTypeEnums) || DeliveryTypeEnums.SELF_PICKUP.equals(deliveryTypeEnums))
                && !StringUtils.hasText(effectiveDriverPhone)) {
            throw new BusinessException("即时货运/自提配送必须填写司机/自提手机号");
        }

        OrderSubDO updateDO = new OrderSubDO();
        updateDO.setId(orderSubDO.getId());
        updateDO.setStatus(OrderSubStatusEnums.SHIPPED.getCode());
        if (dto.getDeliveryType() != null) {
            updateDO.setDeliveryType(dto.getDeliveryType());
        }
        if (StringUtils.hasText(dto.getExpressNo())) {
            updateDO.setExpressNo(dto.getExpressNo());
        }
        if (StringUtils.hasText(dto.getDriverPhone())) {
            updateDO.setDriverPhone(dto.getDriverPhone());
        }
        if (StringUtils.hasText(dto.getDeliveryRemark())) {
            updateDO.setDeliveryRemark(dto.getDeliveryRemark());
        }
        updateDO.setActualSendDate(parseActualSendDate(dto.getActualSendDate()));
        this.updateById(updateDO);

        long totalSubCount = this.count(new LambdaQueryWrapper<OrderSubDO>()
                .eq(OrderSubDO::getMasterId, orderSubDO.getMasterId())
                .eq(OrderSubDO::getIsDelete, GlobalEnums.NO.getCode()));
        long shippedSubCount = this.count(new LambdaQueryWrapper<OrderSubDO>()
                .eq(OrderSubDO::getMasterId, orderSubDO.getMasterId())
                .eq(OrderSubDO::getIsDelete, GlobalEnums.NO.getCode())
                .eq(OrderSubDO::getStatus, OrderSubStatusEnums.SHIPPED.getCode()));
        orderMasterService.syncStatusAfterSubShipped(orderSubDO.getMasterId(), totalSubCount, shippedSubCount);
    }

    @Override
    public boolean hasFinishedOrShippedSubOrder(String masterId) {
        if (!StringUtils.hasText(masterId)) {
            throw new BusinessException("母单ID不能为空");
        }
        return this.count(new LambdaQueryWrapper<OrderSubDO>()
                .eq(OrderSubDO::getMasterId, masterId)
                .eq(OrderSubDO::getIsDelete, GlobalEnums.NO.getCode())
                .in(OrderSubDO::getStatus, OrderSubStatusEnums.PICKED.getCode(), OrderSubStatusEnums.SHIPPED.getCode())) > 0;
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

    private Date parseActualSendDate(String actualSendDate) {
        if (!StringUtils.hasText(actualSendDate)) {
            return new Date();
        }
        try {
            LocalDate localDate = LocalDate.parse(actualSendDate);
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw new BusinessException("实际发货日期格式错误，应为yyyy-MM-dd");
        }
    }

    private OrderSubDO getSubOrderOrThrow(String subId) {
        OrderSubDO orderSubDO = this.getOne(new LambdaQueryWrapper<OrderSubDO>()
                .eq(OrderSubDO::getId, subId)
                .eq(OrderSubDO::getIsDelete, GlobalEnums.NO.getCode()));
        if (orderSubDO == null) {
            throw new BusinessException("子订单不存在");
        }
        return orderSubDO;
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
