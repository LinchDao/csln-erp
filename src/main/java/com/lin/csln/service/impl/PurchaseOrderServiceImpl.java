package com.lin.csln.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.purchase.in.PurchaseInstockedQtyDTO;
import com.lin.csln.dto.purchase.order.PurchaseOrderDTO;
import com.lin.csln.dto.purchase.order.PurchaseOrderItemDTO;
import com.lin.csln.dto.purchase.order.PurchaseOrderPageRespDTO;
import com.lin.csln.dto.purchase.order.PurchaseOrderQueryParamDTO;
import com.lin.csln.entity.PurchaseOrderDO;
import com.lin.csln.entity.PurchaseOrderItemDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.enums.PurchaseOrderStatusEnums;
import com.lin.csln.mapper.PurchaseOrderMapper;
import com.lin.csln.service.PurchaseInItemService;
import com.lin.csln.service.PurchaseInService;
import com.lin.csln.service.PurchaseOrderItemService;
import com.lin.csln.service.PurchaseOrderService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 采购订单 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class PurchaseOrderServiceImpl extends BaseReadonlyServiceImpl<PurchaseOrderMapper, PurchaseOrderDO> implements PurchaseOrderService {

    @Resource
    private PurchaseOrderItemService purchaseOrderItemService;
    @Resource
    private PurchaseInService purchaseInService;

    @Override
    public PageRespDTO<PurchaseOrderPageRespDTO> pagePurchaseOrder(PurchaseOrderQueryParamDTO queryDTO) {
        IPage<PurchaseOrderPageRespDTO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());

        IPage<PurchaseOrderPageRespDTO> resultPage = baseMapper.pagePurchaseOrder(page, queryDTO);

        return PageRespDTO.build(resultPage, queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPurchaseOrder(PurchaseOrderDTO dto, String userId) {
        if (Objects.isNull(dto)) {
            throw new BusinessException("采购单信息不能为空");
        }
        List<PurchaseOrderItemDTO> itemList = dto.getPurchaseOrderItem();
        if (CollectionUtils.isEmpty(itemList)) {
            throw new BusinessException("采购单明细不能为空");
        }

        this.validateTotalAmountAndQty(dto, itemList);

        PurchaseOrderDO order = new PurchaseOrderDO();
        BeanUtils.copyProperties(dto, order);
        order.setCreateUserId(userId);
        this.save(order);

        purchaseOrderItemService.savePurchaseOrderItem(order.getId(), itemList);

        return order.getId();
    }

    @Override
    public PurchaseOrderDTO getPurchaseDetail(String id) {
        PurchaseOrderDTO dto = baseMapper.getPurchaseDetailById(id);

        if (dto == null) {
            throw new BusinessException("采购单不存在");
        }
        List<PurchaseOrderItemDTO> itemDTOList = purchaseOrderItemService.listPurchaseOrderItemSku(dto.getId());
        dto.setPurchaseOrderItem(itemDTOList);

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPurchaseOrder(String id, PurchaseOrderDTO dto) {
        if (Objects.isNull(dto)) {
            throw new BusinessException("采购单信息不能为空");
        }
        List<PurchaseOrderItemDTO> itemList = dto.getPurchaseOrderItem();
        if (CollectionUtils.isEmpty(itemList)) {
            throw new BusinessException("采购单明细不能为空");
        }

        this.validateTotalAmountAndQty(dto, itemList);

        PurchaseOrderDO order = new PurchaseOrderDO();
        BeanUtils.copyProperties(dto, order);
        order.setId(id);
        baseMapper.updateById(order);

        purchaseOrderItemService.savePurchaseOrderItem(order.getId(), itemList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPurchaseOrder(String id) {
        PurchaseOrderDO purchaseOrder = baseMapper.selectOne(new LambdaQueryWrapper<PurchaseOrderDO>()
                .eq(PurchaseOrderDO::getId, id)
                .eq(PurchaseOrderDO::getIsDelete, GlobalEnums.NO.getCode()));
        if (purchaseOrder == null) {
            throw new BusinessException("采购单不存在");
        }

        if (!PurchaseOrderStatusEnums.WAIT_IN.getCode().equals(purchaseOrder.getStatus())) {
            throw new BusinessException("只有【待入库】状态才能取消");
        }

        purchaseOrder.setStatus(PurchaseOrderStatusEnums.CANCELED.getCode());

        baseMapper.updateById(purchaseOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchaseOrderStatus(String purchaseId) {

        List<PurchaseInstockedQtyDTO> instockedList = purchaseInService.listInstockedQty(purchaseId);
        if (CollUtil.isEmpty(instockedList)) {
            return;
        }

        List<PurchaseOrderItemDO> orderItemList = purchaseOrderItemService.listOrderItemList(purchaseId);

        int totalPurchaseQty = orderItemList.stream().mapToInt(PurchaseOrderItemDO::getQty).sum();
        int totalInstockedQty = instockedList.stream().mapToInt(PurchaseInstockedQtyDTO::getQty).sum();

        PurchaseOrderStatusEnums status;
        if (totalInstockedQty >= totalPurchaseQty) {
            status = PurchaseOrderStatusEnums.FINISHED;
        } else if (totalInstockedQty > 0) {
            status = PurchaseOrderStatusEnums.PART_IN;
        } else {
            status = PurchaseOrderStatusEnums.WAIT_IN;
        }

        PurchaseOrderDO updateDO = new PurchaseOrderDO();
        updateDO.setId(purchaseId);
        updateDO.setStatus(status.getCode());
        baseMapper.updateById(updateDO);
    }

    /**
     * 校验采购单总数量、总金额与明细累加值一致
     */
    private void validateTotalAmountAndQty(PurchaseOrderDTO dto, List<PurchaseOrderItemDTO> itemList) {
        int realTotalQty = 0;
        BigDecimal realTotalAmount = BigDecimal.ZERO;

        for (PurchaseOrderItemDTO item : itemList) {
            if (Objects.isNull(item.getQty()) || item.getQty() <= 0) {
                throw new BusinessException("采购数量必须大于0，商品：" + item.getProductName());
            }
            if (Objects.isNull(item.getPrice()) || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("采购单价必须大于0，商品：" + item.getProductName());
            }

            realTotalQty += item.getQty();
            BigDecimal itemAmount = item.getPrice().multiply(new BigDecimal(item.getQty()));
            realTotalAmount = realTotalAmount.add(itemAmount);
        }

        if (!Objects.equals(dto.getTotalQty(), realTotalQty)) {
            throw new BusinessException("采购单总数量错误，前端传入：" + dto.getTotalQty() + "，实际计算：" + realTotalQty);
        }

        if (dto.getTotalAmount().compareTo(realTotalAmount) != 0) {
            throw new BusinessException("采购单总金额错误，前端传入：" + dto.getTotalAmount() + "，实际计算：" + realTotalAmount);
        }
    }

}
