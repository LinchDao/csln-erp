package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import com.lin.csln.dto.product.ProductSkuListDTO;
import com.lin.csln.dto.product.ProductColorDTO;
import com.lin.csln.dto.product.ProductSkuDTO;
import com.lin.csln.entity.ProductSkuDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.ProductSkuMapper;
import com.lin.csln.service.ProductSkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SKU表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class ProductSkuServiceImpl extends BaseReadonlyServiceImpl<ProductSkuMapper, ProductSkuDO> implements ProductSkuService {

    @Override
    public List<ProductSkuDTO> listSkuWithStockByProductId(String productId) {
        return baseMapper.listSkuWithStockByProductId(productId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProductSku(String productId, List<ProductColorDTO> colorList, List<String> sizeNameList) {
        LambdaQueryWrapper<ProductSkuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductSkuDO::getProductId, productId);
        List<ProductSkuDO> existSkuList = baseMapper.selectList(queryWrapper);

        // 构建映射：skuId -> SKU
        Map<String, ProductSkuDO> skuIdMap = new HashMap<>();
        for (ProductSkuDO sku : existSkuList) {
            skuIdMap.put(sku.getId(), sku);
        }

        List<ProductSkuDO> needUpdateList = new ArrayList<>();
        List<ProductSkuDO> needInsertList = new ArrayList<>();
        Set<String> needKeepIds = new HashSet<>();

        for (ProductColorDTO colorDTO : colorList) {
            String colorName = colorDTO.getColorName();
            List<String> skuIdList = colorDTO.getSkuIdList();

            for (String sizeName : sizeNameList) {
                // 先尝试从 skuIdList 匹配（优先用ID匹配，保证库存不变）
                ProductSkuDO targetSku = null;

                for (String skuId : skuIdList) {
                    if (skuIdMap.containsKey(skuId)) {
                        targetSku = skuIdMap.get(skuId);
                        break;
                    }
                }

                if (targetSku != null) {
                    targetSku.setColorName(colorName);
                    targetSku.setIsDelete(GlobalEnums.NO.getCode());           // 恢复启用
                    needUpdateList.add(targetSku);

                    needKeepIds.add(targetSku.getId());
                } else {
                    ProductSkuDO newSku = new ProductSkuDO();
                    newSku.setProductId(productId);
                    newSku.setColorName(colorName);
                    newSku.setSizeName(sizeName);
                    needInsertList.add(newSku);
                }
            }
        }

        // 3. 批量操作
        if (!needInsertList.isEmpty()) {
            this.saveBatch(needInsertList);
        }
        if (!needUpdateList.isEmpty()) {
            this.updateBatchById(needUpdateList);
        }

        // 4. 未在保留列表中的 → 软删除
        List<ProductSkuDO> deleteList = new ArrayList<>();
        for (ProductSkuDO sku : existSkuList) {
            if (!needKeepIds.contains(sku.getId())) {
                sku.setIsDelete(GlobalEnums.YES.getCode());
                deleteList.add(sku);
            }
        }
        if (!deleteList.isEmpty()) {
            this.updateBatchById(deleteList);
        }
    }

    @Override
    public List<ProductSkuListDTO> getSkuByProductId(String productId) {

        LambdaQueryWrapper<ProductSkuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductSkuDO::getProductId, productId);
        wrapper.eq(ProductSkuDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductSkuDO> skuList = baseMapper.selectList(wrapper);

        // 转VO
        return skuList.stream().map(sku -> {
            ProductSkuListDTO dto = new ProductSkuListDTO();
            BeanUtils.copyProperties(sku, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
