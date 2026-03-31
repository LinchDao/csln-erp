package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import com.lin.csln.dto.product.ProductColorDTO;
import com.lin.csln.dto.product.ProductColorImageDTO;
import com.lin.csln.entity.ProductColorImageDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.ProductColorImageMapper;
import com.lin.csln.service.ProductColorImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品颜色图片表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class ProductColorImageServiceImpl extends BaseReadonlyServiceImpl<ProductColorImageMapper, ProductColorImageDO> implements ProductColorImageService {

    @Override
    public List<ProductColorImageDTO> listProductColorImage(String productId) {
        List<ProductColorImageDO> list = baseMapper.selectList(
                Wrappers.lambdaQuery(ProductColorImageDO.class)
                        .eq(ProductColorImageDO::getProductId, productId)
                        .eq(ProductColorImageDO::getIsDelete, GlobalEnums.NO.getCode())
        );

        return list.stream().map(item -> {
            ProductColorImageDTO dto = new ProductColorImageDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProductColorImage(String productId, List<ProductColorDTO> colorList) {
        LambdaQueryWrapper<ProductColorImageDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductColorImageDO::getProductId, productId);
        List<ProductColorImageDO> existImageList = baseMapper.selectList(queryWrapper);

        // 构建映射：colorName + colorFileId → DO（唯一标识一条图片记录）
        Map<String, ProductColorImageDO> imageKeyMap = new HashMap<>();
        for (ProductColorImageDO imageDO : existImageList) {
            String key = imageDO.getColorName() + ":" + imageDO.getColorFileId();
            imageKeyMap.put(key, imageDO);
        }

        List<ProductColorImageDO> insertList = new ArrayList<>();
        List<ProductColorImageDO> updateList = new ArrayList<>();
        Set<String> needKeepKeys = new HashSet<>();

        for (ProductColorDTO colorDTO : colorList) {
            String colorName = colorDTO.getColorName();
            List<String> imageIdList = colorDTO.getColorImageIdList();

            if (imageIdList == null || imageIdList.isEmpty()) {
                continue;
            }

            for (String fileId : imageIdList) {
                String key = colorName + ":" + fileId;
                needKeepKeys.add(key);

                ProductColorImageDO existImage = imageKeyMap.get(key);
                if (existImage != null) {
                    existImage.setIsDelete(GlobalEnums.NO.getCode());
                    updateList.add(existImage);
                }
                else {
                    ProductColorImageDO newImage = new ProductColorImageDO();
                    newImage.setProductId(productId);
                    newImage.setColorName(colorName);
                    newImage.setColorFileId(fileId);
                    newImage.setIsDelete(GlobalEnums.NO.getCode());
                    insertList.add(newImage);
                }
            }
        }

        // 3. 批量新增 + 更新
        if (!insertList.isEmpty()) {
            this.saveBatch(insertList);
        }
        if (!updateList.isEmpty()) {
            this.updateBatchById(updateList);
        }

        List<ProductColorImageDO> deleteList = new ArrayList<>();
        for (ProductColorImageDO imageDO : existImageList) {
            String key = imageDO.getColorName() + ":" + imageDO.getColorFileId();
            if (!needKeepKeys.contains(key)) {
                imageDO.setIsDelete(GlobalEnums.YES.getCode());
                deleteList.add(imageDO);
            }
        }
        if (!deleteList.isEmpty()) {
            this.updateBatchById(deleteList);
        }
    }

}
