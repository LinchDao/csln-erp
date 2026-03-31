package com.lin.csln.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.purchase.in.PurchaseInDTO;
import com.lin.csln.dto.purchase.in.PurchaseInDetailRespDTO;
import com.lin.csln.dto.purchase.in.PurchaseInItemDTO;
import com.lin.csln.dto.purchase.in.PurchaseInItemRespDTO;
import com.lin.csln.dto.purchase.in.PurchaseInPageRespDTO;
import com.lin.csln.dto.purchase.in.PurchaseInQueryParamDTO;
import com.lin.csln.dto.purchase.in.PurchaseInstockedQtyDTO;
import com.lin.csln.entity.PurchaseInDO;
import com.lin.csln.entity.PurchaseOrderItemDO;
import com.lin.csln.enums.PurchaseInStatusEnums;
import com.lin.csln.mapper.PurchaseInMapper;
import com.lin.csln.service.PurchaseInItemService;
import com.lin.csln.service.PurchaseInService;
import com.lin.csln.service.PurchaseOrderItemService;
import com.lin.csln.service.PurchaseOrderService;
import com.lin.csln.service.StockService;
import com.lin.csln.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 采购入库单服务实现类
 */
@Service
@Transactional(readOnly = true)
public class PurchaseInServiceImpl extends ServiceImpl<PurchaseInMapper, PurchaseInDO> implements PurchaseInService {

    private static final String IN_CONNECT = "-IN-";

    @Resource
    private PurchaseInItemService purchaseInItemService;
    @Resource
    private UserService userService;
    @Resource
    private PurchaseOrderService purchaseOrderService;
    @Resource
    private PurchaseOrderItemService purchaseOrderItemService;
    @Resource
    private StockService stockService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPurchaseIn(PurchaseInDTO dto, String userId) {
        if (dto == null) {
            throw new BusinessException("入库单信息不能为空");
        }
        if (checkExistUnAudit(dto.getPurchaseId())) {
            throw new BusinessException("当前采购单存在待审核或已驳回的入库单，请先处理后再入库");
        }

        List<PurchaseInItemDTO> itemList = validateCreatePurchaseIn(dto);

        PurchaseInDO inDO = new PurchaseInDO();
        inDO.setInNo(genInNo());
        inDO.setPurchaseId(dto.getPurchaseId());
        inDO.setWarehouseId(dto.getWarehouseId());
        inDO.setStatus(PurchaseInStatusEnums.WAIT_AUDIT.getCode());
        inDO.setCreateUserId(userId);
        inDO.setRemark(dto.getRemark());
        inDO.setCreateTime(new Date());
        inDO.setTotalQty(itemList.stream().mapToInt(PurchaseInItemDTO::getQty).sum());
        baseMapper.insert(inDO);

        purchaseInItemService.savePurchaseInItemList(inDO.getId(), itemList);
        return inDO.getId();
    }

    @Override
    public List<PurchaseInstockedQtyDTO> listInstockedQty(String purchaseId) {
        return baseMapper.selectSumInstockedQtyByPurchaseId(purchaseId);
    }

    @Override
    public Boolean checkExistUnAudit(String purchaseId) {
        LambdaQueryWrapper<PurchaseInDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseInDO::getPurchaseId, purchaseId);
        wrapper.in(PurchaseInDO::getStatus,
                PurchaseInStatusEnums.WAIT_AUDIT.getCode(),
                PurchaseInStatusEnums.REJECTED.getCode());
        return baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public PageRespDTO<PurchaseInPageRespDTO> pagePurchaseIn(PurchaseInQueryParamDTO dto) {
        IPage<PurchaseInPageRespDTO> page = new Page<>(dto.getPage(), dto.getLimit());
        IPage<PurchaseInPageRespDTO> resultPage = baseMapper.selectPurchaseInPage(page, dto);

        Set<String> userIds = resultPage.getRecords().stream()
                .flatMap(o -> Stream.of(o.getCreateUserId(), o.getAuditUserId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (CollUtil.isNotEmpty(userIds)) {
            Map<String, String> userNameMap = userService.getUserNamesByIds(userIds);
            resultPage.getRecords().forEach(resp -> {
                resp.setCreateUserName(userNameMap.get(resp.getCreateUserId()));
                resp.setAuditUserName(userNameMap.get(resp.getAuditUserId()));
            });
        }

        return PageRespDTO.build(resultPage, dto);
    }

    @Override
    public PurchaseInDetailRespDTO getPurchaseInDetail(String inId) {
        PurchaseInDetailRespDTO detailResp = baseMapper.selectPurchaseInDetail(inId);
        if (detailResp == null) {
            throw new BusinessException("采购入库单不存在");
        }

        Set<String> userIds = new HashSet<>();
        Optional.ofNullable(detailResp.getCreateUserId()).ifPresent(userIds::add);
        Optional.ofNullable(detailResp.getAuditUserId()).ifPresent(userIds::add);
        if (!CollectionUtils.isEmpty(userIds)) {
            Map<String, String> userNameMap = userService.getUserNamesByIds(userIds);
            detailResp.setCreateUserName(userNameMap.get(detailResp.getCreateUserId()));
            detailResp.setAuditUserName(userNameMap.get(detailResp.getAuditUserId()));
        }

        List<PurchaseInItemRespDTO> purchaseInItemList =
                purchaseInItemService.listPurchaseInItem(inId, detailResp.getPurchaseId());
        detailResp.setPurchaseInItem(purchaseInItemList);
        return detailResp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditPass(String inId, String auditUserId) {
        PurchaseInDO purchaseIn = getWaitAuditPurchaseIn(inId);

        PurchaseInDO update = new PurchaseInDO();
        update.setId(inId);
        update.setStatus(PurchaseInStatusEnums.AUDITED.getCode());
        update.setAuditUserId(auditUserId);
        baseMapper.updateById(update);

        purchaseOrderService.updatePurchaseOrderStatus(purchaseIn.getPurchaseId());
        stockService.purchaseIn(inId, purchaseIn.getWarehouseId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditReject(String inId, String auditUserId) {
        getWaitAuditPurchaseIn(inId);

        PurchaseInDO update = new PurchaseInDO();
        update.setId(inId);
        update.setStatus(PurchaseInStatusEnums.REJECTED.getCode());
        update.setAuditUserId(auditUserId);
        baseMapper.updateById(update);
    }

    private PurchaseInDO getWaitAuditPurchaseIn(String inId) {
        PurchaseInDO purchaseIn = baseMapper.selectById(inId);
        if (purchaseIn == null) {
            throw new BusinessException("入库单不存在");
        }
        if (!Objects.equals(purchaseIn.getStatus(), PurchaseInStatusEnums.WAIT_AUDIT.getCode())) {
            throw new BusinessException("只能审核待审核状态的单据");
        }
        return purchaseIn;
    }

    private List<PurchaseInItemDTO> validateCreatePurchaseIn(PurchaseInDTO dto) {
        List<PurchaseInItemDTO> itemList = dto.getItemList();
        if (CollUtil.isEmpty(itemList)) {
            throw new BusinessException("入库明细不能为空");
        }

        Map<String, Integer> currentInQtyMap = new HashMap<>();
        for (PurchaseInItemDTO item : itemList) {
            if (item == null) {
                throw new BusinessException("入库明细不能为空");
            }
            if (item.getSkuId() == null || item.getSkuId().isBlank()) {
                throw new BusinessException("SKU不能为空");
            }
            if (item.getQty() == null || item.getQty() <= 0) {
                throw new BusinessException("入库数量必须大于0，SKU: " + item.getSkuId());
            }
            currentInQtyMap.merge(item.getSkuId(), item.getQty(), Integer::sum);
        }

        if (currentInQtyMap.size() != itemList.size()) {
            throw new BusinessException("入库明细中存在重复SKU，请先合并后再提交");
        }

        Map<String, Integer> orderedQtyMap = purchaseOrderItemService.listOrderItemList(dto.getPurchaseId()).stream()
                .collect(Collectors.toMap(PurchaseOrderItemDO::getSkuId, PurchaseOrderItemDO::getQty, Integer::sum));
        if (CollUtil.isEmpty(orderedQtyMap)) {
            throw new BusinessException("采购单明细不存在，无法创建入库单");
        }

        Map<String, Integer> instockedQtyMap = Optional.ofNullable(listInstockedQty(dto.getPurchaseId()))
                .orElseGet(Collections::emptyList)
                .stream()
                .collect(Collectors.toMap(PurchaseInstockedQtyDTO::getSkuId, PurchaseInstockedQtyDTO::getQty, Integer::sum));

        for (Map.Entry<String, Integer> entry : currentInQtyMap.entrySet()) {
            String skuId = entry.getKey();
            Integer orderQty = orderedQtyMap.get(skuId);
            if (orderQty == null) {
                throw new BusinessException("入库商品不在采购单中，SKU: " + skuId);
            }

            int remainingQty = orderQty - instockedQtyMap.getOrDefault(skuId, 0);
            if (entry.getValue() > remainingQty) {
                throw new BusinessException("SKU " + skuId + " 入库数量超过可入库数量，剩余可入库: "
                        + Math.max(remainingQty, 0));
            }
        }

        return itemList;
    }

    private synchronized String genInNo() {
        String today = DateUtil.format(new Date(), "yyyyMMdd");
        String prefix = today + IN_CONNECT;
        String maxNo = baseMapper.getMaxInNoByPrefix(prefix);

        int seq = 1;
        if (maxNo != null) {
            String seqStr = maxNo.substring(prefix.length());
            seq = Integer.parseInt(seqStr) + 1;
        }
        return prefix + String.format("%04d", seq);
    }
}
