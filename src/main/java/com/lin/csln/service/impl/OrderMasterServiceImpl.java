package com.lin.csln.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.common.cache.UserCache;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.dto.UserInfoDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.order.*;
import com.lin.csln.entity.OrderItemDO;
import com.lin.csln.entity.OrderMasterDO;
import com.lin.csln.entity.OrderSubDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.enums.OrderAuditStatusEnums;
import com.lin.csln.enums.OrderMasterStatusEnums;
import com.lin.csln.mapper.OrderMasterMapper;
import com.lin.csln.service.OrderItemService;
import com.lin.csln.service.OrderMasterService;
import com.lin.csln.service.OrderSubService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 订单主表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class OrderMasterServiceImpl extends BaseReadonlyServiceImpl<OrderMasterMapper, OrderMasterDO> implements OrderMasterService {

    private static final DateTimeFormatter ORDER_NO_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String ORDER_NO_SPLIT = "-M";

    @Resource
    private OrderSubService orderSubService;
    @Resource
    private OrderItemService orderItemService;

    @Override
    public PageRespDTO<OrderMasterPageRespDTO> pageOrderMaster(OrderMasterQueryParamDTO queryParamDTO) {
        IPage<OrderMasterPageRespDTO> page = new Page<>(queryParamDTO.getPage(), queryParamDTO.getLimit());
        IPage<OrderMasterPageRespDTO> resultPage = baseMapper.pageOrderMaster(page, queryParamDTO);
        return PageRespDTO.build(resultPage, queryParamDTO);
    }

    @Override
    public OrderMasterDetailRespDTO getOrderMasterDetail(String id) {
        OrderMasterDetailRespDTO detail = baseMapper.selectOrderMasterDetail(id);
        if (detail == null) {
            throw new BusinessException("订单不存在");
        }
        List<OrderSubDetailRespDTO> subOrders = orderSubService.listDetailByMasterId(id);
        List<OrderItemDetailRespDTO> items = orderItemService.listDetailByMasterId(id);

        Map<String, List<OrderItemDetailRespDTO>> itemMap = new HashMap<>();
        for (OrderItemDetailRespDTO item : items) {
            itemMap.computeIfAbsent(item.getSubId(), k -> new ArrayList<>()).add(item);
        }
        for (OrderSubDetailRespDTO sub : subOrders) {
            sub.setItems(itemMap.getOrDefault(sub.getId(), new ArrayList<>()));
        }
        detail.setSubOrders(subOrders);
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrderMaster(OrderMasterDTO dto, String userId) {
        validateOrderMaster(dto);
        validateAmountAndQty(dto);
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException("未获取到当前用户，请重新登录");
        }
        UserInfoDTO userinfo = UserCache.getUserInfo(userId);

        OrderMasterDO orderMasterDO = new OrderMasterDO();
        BeanUtil.copyProperties(dto, orderMasterDO);
        orderMasterDO.setShopId(userinfo.getShopId());
        orderMasterDO.setAuditStatus(OrderAuditStatusEnums.WAIT_AUDIT.getCode());
        orderMasterDO.setIsAr(GlobalEnums.YES.getCode());
        orderMasterDO.setAllowReplace(GlobalEnums.NO.getCode());
        orderMasterDO.setStatus(OrderMasterStatusEnums.WAREHOUSE_PREPARING.getCode());
        String orderNo = generateOrderNo();
        orderMasterDO.setOrderNo(orderNo);
        orderMasterDO.setCreateUserId(userId);
        orderMasterDO.setSalesUserId(userId);
        this.save(orderMasterDO);

        //todo 后续改为前端传参
        dto.getSubOrders().forEach(orderSubDO -> {
            orderSubDO.setWarehouseId(userinfo.getWarehouseId());
        });

        orderSubService.saveSubOrder(orderMasterDO.getId(), orderNo, dto.getSubOrders(), dto.getIsDraft());
        return orderMasterDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editOrderMaster(String id, OrderMasterDTO dto) {
        if (dto != null) {
            dto.setIsDraft(GlobalEnums.NO.getCode());
        }
        validateOrderMaster(dto);
        validateAmountAndQty(dto);

        OrderMasterDO dbOrder = getOrderOrThrow(id);
        if (GlobalEnums.YES.getCode().equals(dbOrder.getIsDraft())) {
            throw new BusinessException("正式单编辑接口不支持草稿单");
        }
        if (!OrderMasterStatusEnums.WAIT_AUDIT.getCode().equals(dbOrder.getStatus())
                && !OrderMasterStatusEnums.WAREHOUSE_PREPARING.getCode().equals(dbOrder.getStatus())) {
            throw new BusinessException("仅“待审核/仓库准备中可编辑”");
        }

        BeanUtil.copyProperties(dto, dbOrder);
        dbOrder.setIsDraft(GlobalEnums.NO.getCode());
        baseMapper.updateById(dbOrder);
        orderSubService.saveSubOrder(id, dbOrder.getOrderNo(), dto.getSubOrders(), GlobalEnums.NO.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editDraftOrderMaster(String id, OrderMasterDTO dto) {
        if (dto != null) {
            dto.setIsDraft(GlobalEnums.YES.getCode());
        }
        validateOrderMaster(dto);
        validateAmountAndQty(dto);

        OrderMasterDO dbOrder = getOrderOrThrow(id);
        if (!GlobalEnums.YES.getCode().equals(dbOrder.getIsDraft())) {
            throw new BusinessException("仅草稿单可编辑");
        }

        BeanUtil.copyProperties(dto, dbOrder);
        dbOrder.setIsDraft(GlobalEnums.YES.getCode());
        baseMapper.updateById(dbOrder);
        orderSubService.saveSubOrder(id, dbOrder.getOrderNo(), dto.getSubOrders(), GlobalEnums.YES.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitOrderMaster(String id, OrderMasterDTO dto) {
        OrderMasterDO dbOrder = getOrderOrThrow(id);
        if (!GlobalEnums.YES.getCode().equals(dbOrder.getIsDraft())) {
            throw new BusinessException("仅草稿单可提交");
        }

        OrderMasterDTO submitDTO;
        if (dto != null) {
            dto.setIsDraft(GlobalEnums.YES.getCode());
            validateOrderMaster(dto);
            validateAmountAndQty(dto);
            BeanUtil.copyProperties(dto, dbOrder);
            dbOrder.setIsDraft(GlobalEnums.YES.getCode());
            baseMapper.updateById(dbOrder);
            submitDTO = dto;
        } else {
            submitDTO = buildOrderMasterDTOFromDb(dbOrder.getId());
            validateOrderMaster(submitDTO);
            validateAmountAndQty(submitDTO);
        }

        // 提交阶段允许使用最终内容更新子单/明细，但仍按草稿口径保存，避免被差量逻辑吞掉锁库动作
        orderSubService.saveSubOrder(id, dbOrder.getOrderNo(), submitDTO.getSubOrders(), GlobalEnums.YES.getCode());

        // 草稿转正式时，统一按当前有效子单明细做一次锁库
        orderItemService.lockDraftItemsByMasterId(id);

        OrderMasterDO submitUpdate = new OrderMasterDO();
        submitUpdate.setId(id);
        submitUpdate.setIsDraft(GlobalEnums.NO.getCode());
        submitUpdate.setStatus(OrderMasterStatusEnums.WAREHOUSE_PREPARING.getCode());
        baseMapper.updateById(submitUpdate);
    }

    private void validateOrderMaster(OrderMasterDTO dto) {
        if (dto == null) {
            throw new BusinessException("订单数据不能为空");
        }
        if (!StringUtils.hasText(dto.getCustomerId())) {
            throw new BusinessException("客户不能为空");
        }
        if (dto.getOrderType() == null) {
            throw new BusinessException("订单类型不能为空");
        }
        if (dto.getIsDraft() == null) {
            throw new BusinessException("是否草稿不能为空");
        }
        if (dto.getTotalQty() == null || dto.getTotalQty() < 0) {
            throw new BusinessException("主单总数量不能为空且不能小于0");
        }
        if (dto.getTotalAmount() == null || dto.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("主单总金额不能为空且不能小于0");
        }
        if (CollectionUtils.isEmpty(dto.getSubOrders())) {
            throw new BusinessException("至少需要一个子单");
        }
        Set<String> subIdSet = new HashSet<>();
        for (OrderSubDTO subDTO : dto.getSubOrders()) {
            if (StringUtils.hasText(subDTO.getId()) && !subIdSet.add(subDTO.getId())) {
                throw new BusinessException("子单ID重复：" + subDTO.getId());
            }
            if (subDTO.getAmount() == null || subDTO.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("子单金额不能为空且不能小于0");
            }
            if (CollectionUtils.isEmpty(subDTO.getItems())) {
                throw new BusinessException("子单明细不能为空");
            }
            Set<String> itemIdSet = new HashSet<>();
            for (OrderItemDTO itemDTO : subDTO.getItems()) {
                if (StringUtils.hasText(itemDTO.getId()) && !itemIdSet.add(itemDTO.getId())) {
                    throw new BusinessException("子单明细ID重复：" + itemDTO.getId());
                }
                if (!StringUtils.hasText(itemDTO.getSkuId())) {
                    throw new BusinessException("SKU不能为空");
                }
                if (itemDTO.getQty() == null || itemDTO.getQty() <= 0) {
                    throw new BusinessException("明细数量必须大于0");
                }
                if (itemDTO.getPrice() == null || itemDTO.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                    throw new BusinessException("明细单价不能小于0");
                }
                if (itemDTO.getAmount() == null || itemDTO.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new BusinessException("明细金额不能小于0");
                }
            }
        }
    }

    private void validateAmountAndQty(OrderMasterDTO dto) {
        int totalQty = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderSubDTO subDTO : dto.getSubOrders()) {
            int subQty = 0;
            BigDecimal subAmount = BigDecimal.ZERO;
            for (OrderItemDTO itemDTO : subDTO.getItems()) {
                BigDecimal calcItemAmount = itemDTO.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQty()));
                if (calcItemAmount.compareTo(itemDTO.getAmount()) != 0) {
                    throw new BusinessException("明细金额与数量*单价不一致，SKU：" + itemDTO.getSkuId());
                }
                subQty += itemDTO.getQty();
                subAmount = subAmount.add(itemDTO.getAmount());
            }
            if (subDTO.getAmount().compareTo(subAmount) != 0) {
                throw new BusinessException("子单金额不一致");
            }
            totalQty += subQty;
            totalAmount = totalAmount.add(subAmount);
        }
        if (!Objects.equals(dto.getTotalQty(), totalQty)) {
            throw new BusinessException("主单总数量不一致");
        }
        if (dto.getTotalAmount().compareTo(totalAmount) != 0) {
            throw new BusinessException("主单总金额不一致");
        }
    }

    private String generateOrderNo() {
        String datePart = LocalDate.now().format(ORDER_NO_DATE_FORMATTER);
        String orderNoPrefix = datePart + ORDER_NO_SPLIT;
        String maxOrderNo = baseMapper.selectMaxOrderNoByPrefix(orderNoPrefix);
        int nextSeq = parseOrderSeq(maxOrderNo, orderNoPrefix) + 1;
        if (nextSeq > 99999) {
            throw new BusinessException("主单号超出当日最大序号，请联系管理员");
        }
        return orderNoPrefix + String.format("%05d", nextSeq);
    }

    private int parseOrderSeq(String orderNo, String orderNoPrefix) {
        if (!StringUtils.hasText(orderNo) || !orderNo.startsWith(orderNoPrefix)) {
            return 0;
        }
        String seqPart = orderNo.substring(orderNoPrefix.length());
        if (seqPart.length() != 5 || !seqPart.chars().allMatch(Character::isDigit)) {
            return 0;
        }
        return Integer.parseInt(seqPart);
    }

    private OrderMasterDO getOrderOrThrow(String id) {
        OrderMasterDO dbOrder = this.getById(id);
        if (dbOrder == null) {
            throw new BusinessException("订单不存在");
        }
        return dbOrder;
    }

    private OrderMasterDTO buildOrderMasterDTOFromDb(String masterId) {
        OrderMasterDO masterDO = getOrderOrThrow(masterId);
        OrderMasterDTO dto = new OrderMasterDTO();
        BeanUtil.copyProperties(masterDO, dto);

        List<OrderSubDO> subOrderDOList = orderSubService.list(new LambdaQueryWrapper<OrderSubDO>()
                .eq(OrderSubDO::getMasterId, masterId)
                .eq(OrderSubDO::getIsDelete, GlobalEnums.NO.getCode()));
        List<OrderSubDTO> subOrders = new java.util.ArrayList<>();
        for (OrderSubDO subDO : subOrderDOList) {
            OrderSubDTO subDTO = new OrderSubDTO();
            BeanUtil.copyProperties(subDO, subDTO);
            List<OrderItemDO> itemDOList = orderItemService.listBySubId(subDO.getId());
            List<OrderItemDTO> itemDTOList = new java.util.ArrayList<>();
            for (OrderItemDO itemDO : itemDOList) {
                OrderItemDTO itemDTO = new OrderItemDTO();
                BeanUtil.copyProperties(itemDO, itemDTO);
                itemDTOList.add(itemDTO);
            }
            subDTO.setItems(itemDTOList);
            subOrders.add(subDTO);
        }
        dto.setSubOrders(subOrders);
        return dto;
    }

}
