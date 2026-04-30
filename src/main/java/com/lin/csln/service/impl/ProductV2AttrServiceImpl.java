package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.dto.product.ProductV2AttrDTO;
import com.lin.csln.entity.ProductV2AttrDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.ProductV2AttrMapper;
import com.lin.csln.service.ProductV2AttrService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductV2AttrServiceImpl extends BaseReadonlyServiceImpl<ProductV2AttrMapper, ProductV2AttrDO> implements ProductV2AttrService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProductV2Attrs(String productV2Id, List<ProductV2AttrDTO> attrList) {
        LambdaQueryWrapper<ProductV2AttrDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductV2AttrDO::getProductV2Id, productV2Id)
                .eq(ProductV2AttrDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductV2AttrDO> oldList = baseMapper.selectList(wrapper);
        if (!oldList.isEmpty()) {
            for (ProductV2AttrDO old : oldList) {
                old.setIsDelete(GlobalEnums.YES.getCode());
            }
            this.updateBatchById(oldList);
        }

        if (attrList == null || attrList.isEmpty()) {
            return;
        }

        List<ProductV2AttrDO> insertList = new ArrayList<>();
        for (ProductV2AttrDTO attr : attrList) {
            if (attr == null || !StringUtils.hasText(attr.getKey()) || !StringUtils.hasText(attr.getName())) {
                continue;
            }
            ProductV2AttrDO entity = new ProductV2AttrDO();
            entity.setProductV2Id(productV2Id);
            entity.setAttrKey(attr.getKey().trim());
            entity.setAttrName(attr.getName().trim());
            entity.setAttrValue(attr.getValue());
            entity.setValueType(attr.getValueType());
            entity.setSort(attr.getSort() == null ? 0 : attr.getSort());
            entity.setIsDelete(GlobalEnums.NO.getCode());
            insertList.add(entity);
        }
        if (!insertList.isEmpty()) {
            this.saveBatch(insertList);
        }
    }

    @Override
    public List<ProductV2AttrDTO> listProductV2Attrs(String productV2Id) {
        LambdaQueryWrapper<ProductV2AttrDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductV2AttrDO::getProductV2Id, productV2Id)
                .eq(ProductV2AttrDO::getIsDelete, GlobalEnums.NO.getCode())
                .orderByAsc(ProductV2AttrDO::getSort, ProductV2AttrDO::getId);
        List<ProductV2AttrDO> list = baseMapper.selectList(wrapper);
        return list.stream().map(entity -> {
            ProductV2AttrDTO dto = new ProductV2AttrDTO();
            dto.setKey(entity.getAttrKey());
            dto.setName(entity.getAttrName());
            dto.setValue(entity.getAttrValue());
            dto.setValueType(entity.getValueType());
            dto.setSort(entity.getSort());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void softDeleteByProductV2Id(String productV2Id) {
        LambdaQueryWrapper<ProductV2AttrDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductV2AttrDO::getProductV2Id, productV2Id)
                .eq(ProductV2AttrDO::getIsDelete, GlobalEnums.NO.getCode());
        List<ProductV2AttrDO> list = baseMapper.selectList(wrapper);
        if (list.isEmpty()) {
            return;
        }
        for (ProductV2AttrDO item : list) {
            item.setIsDelete(GlobalEnums.YES.getCode());
        }
        this.updateBatchById(list);
    }
}
