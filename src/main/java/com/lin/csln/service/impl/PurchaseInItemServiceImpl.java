package com.lin.csln.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.purchase.in.PurchaseInItemDTO;
import com.lin.csln.dto.purchase.in.PurchaseInItemRespDTO;
import com.lin.csln.entity.PurchaseInItemDO;
import com.lin.csln.mapper.PurchaseInItemMapper;
import com.lin.csln.service.PurchaseInItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 入库明细表 服务实现类
 *
 * @author 系统生成器
 */
@Service
@Transactional(readOnly = true)
public class PurchaseInItemServiceImpl extends ServiceImpl<PurchaseInItemMapper, PurchaseInItemDO> implements PurchaseInItemService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePurchaseInItemList(String id, List<PurchaseInItemDTO> itemList) {
        if (CollUtil.isEmpty(itemList)) {
            throw new BusinessException("未填写入库明细。");
        }

        LambdaQueryWrapper<PurchaseInItemDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseInItemDO::getInId, id);
        List<PurchaseInItemDO> dbList = baseMapper.selectList(queryWrapper);


        Map<String, PurchaseInItemDO> dbMap = dbList.stream()
                .collect(Collectors.toMap(PurchaseInItemDO::getSkuId, i -> i));

        for (PurchaseInItemDTO dto : itemList) {
            String skuId = dto.getSkuId();
            Integer qty = dto.getQty();

            PurchaseInItemDO dbItem = dbMap.get(skuId);

            if (dbItem == null) {
                PurchaseInItemDO newItem = new PurchaseInItemDO();
                newItem.setId(cn.hutool.core.util.IdUtil.simpleUUID());
                newItem.setInId(id);
                newItem.setSkuId(skuId);
                newItem.setQty(qty);
                baseMapper.insert(newItem);
            } else {
                dbItem.setQty(qty);
                baseMapper.updateById(dbItem);
                dbMap.remove(skuId);
            }
        }

        if (CollUtil.isNotEmpty(dbMap)) {
            List<String> deleteIds = dbMap.values().stream()
                    .map(PurchaseInItemDO::getId)
                    .collect(Collectors.toList());
            baseMapper.deleteBatchIds(deleteIds);
        }
    }

    @Override
    public List<PurchaseInItemRespDTO> listPurchaseInItem(String inId, String purchaseId) {
        return baseMapper.listPurchaseInItem(inId, purchaseId);
    }

    @Override
    public List<PurchaseInItemDO> listPurchaseInItem(String inId) {
        LambdaQueryWrapper<PurchaseInItemDO> itemQuery = new LambdaQueryWrapper<>();
        itemQuery.eq(PurchaseInItemDO::getInId, inId);
        List<PurchaseInItemDO> itemList = baseMapper.selectList(itemQuery);

        if (CollUtil.isEmpty(itemList)) {
            throw new BusinessException("入库单明细为空，信息异常。");
        }
        return itemList;
    }

}
