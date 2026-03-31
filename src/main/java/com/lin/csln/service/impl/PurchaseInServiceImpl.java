package com.lin.csln.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.purchase.in.*;
import com.lin.csln.entity.PurchaseInDO;
import com.lin.csln.enums.PurchaseInStatusEnums;
import com.lin.csln.mapper.PurchaseInMapper;
import com.lin.csln.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 采购入库单 服务实现类
 *
 * @author 系统生成器
 */
@Service
@Transactional(readOnly = true)
public class PurchaseInServiceImpl extends ServiceImpl<PurchaseInMapper, PurchaseInDO> implements PurchaseInService {

    private final String IN_CONNECT = "-IN-";

    @Resource
    private PurchaseInItemService purchaseInItemService;
    @Resource
    private UserService userService;
    @Resource
    private PurchaseOrderService purchaseOrderService;
    @Resource
    private StockService stockService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPurchaseIn(PurchaseInDTO dto, String userId) {
        if (checkExistUnAudit(dto.getPurchaseId())) {
            throw new BusinessException("该采购单存在未审核的入库单，完成审核后再进行入库操作。");
        }
        if (CollUtil.isEmpty(dto.getItemList())) {
            throw new BusinessException("未填写入库明细。");
        }

        PurchaseInDO inDO = new PurchaseInDO();
        inDO.setInNo(genInNo());
        inDO.setPurchaseId(dto.getPurchaseId());
        inDO.setWarehouseId(dto.getWarehouseId());
        inDO.setStatus(PurchaseInStatusEnums.WAIT_AUDIT.getCode());
        inDO.setCreateUserId(userId);
        inDO.setRemark(dto.getRemark());
        inDO.setCreateTime(new Date());
        inDO.setTotalQty(dto.getItemList().stream().mapToInt(PurchaseInItemDTO::getQty).sum());
        baseMapper.insert(inDO);

        purchaseInItemService.savePurchaseInItemList(inDO.getId(), dto.getItemList());

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
        wrapper.in(PurchaseInDO::getStatus
                , PurchaseInStatusEnums.WAIT_AUDIT.getCode()
                , PurchaseInStatusEnums.REJECTED.getCode());

        long count = baseMapper.selectCount(wrapper);

        return count > 0;
    }

    @Override
    public PageRespDTO<PurchaseInPageRespDTO> pagePurchaseIn(PurchaseInQueryParamDTO dto) {
        IPage<PurchaseInPageRespDTO> page = new Page<>(dto.getPage(), dto.getLimit());

        IPage<PurchaseInPageRespDTO> resultPage = baseMapper.selectPurchaseInPage(page, dto);


        //  批量获取用户名并回填
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
        if (Objects.isNull(detailResp)) {
            throw new BusinessException("采购入库单不存在");
        }

        Set<String> userIds = new HashSet<>();
        Optional.ofNullable(detailResp.getCreateUserId()).ifPresent(userIds::add);
        Optional.ofNullable(detailResp.getAuditUserId()).ifPresent(userIds::add);
        if (!CollectionUtils.isEmpty(userIds)) {
            Map<String, String> userNameMap = userService.getUserNamesByIds(userIds);
            detailResp.setCreateUserName(userNameMap.get(detailResp.getCreateUserId()));
            detailResp.setAuditUserName(userNameMap.get(detailResp.getAuditUserId()));
        } else {
        }

        List<PurchaseInItemRespDTO> purchaseInItemList = purchaseInItemService.listPurchaseInItem(inId, detailResp.getPurchaseId());
        detailResp.setPurchaseInItem(purchaseInItemList);

        return detailResp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditPass(String inId, String auditUserId) {
        PurchaseInDO purchaseIn = baseMapper.selectById(inId);
        if (purchaseIn == null) {
            throw new BusinessException("入库单不存在");
        }
        if (!Objects.equals(purchaseIn.getStatus(), 0)) {
            throw new BusinessException("只能审核【待审核】状态的单据");
        }

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
        PurchaseInDO purchaseIn = baseMapper.selectById(inId);
        if (purchaseIn == null) {
            throw new BusinessException("入库单不存在");
        }
        if (!Objects.equals(purchaseIn.getStatus(), 0)) {
            throw new BusinessException("只能审核【待审核】状态的单据");
        }

        PurchaseInDO update = new PurchaseInDO();
        update.setId(inId);
        update.setStatus(PurchaseInStatusEnums.REJECTED.getCode());
        update.setAuditUserId(auditUserId);
        baseMapper.updateById(update);
    }

    private String genInNo() {
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
