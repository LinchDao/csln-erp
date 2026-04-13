package com.lin.csln.log.snapshot.impl;

import com.lin.csln.log.snapshot.OperationSnapshotProvider;
import com.lin.csln.enums.OperationLogModuleEnum;
import com.lin.csln.service.ProductService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 商品快照读取器
 */
@Slf4j
@Component
public class ProductSnapshotProvider implements OperationSnapshotProvider {

    @Resource
    private ProductService productService;

    @Override
    public boolean supports(OperationLogModuleEnum module) {
        return OperationLogModuleEnum.PRODUCT == module;
    }

    @Override
    public Object snapshot(String bizId) {
        if (!StringUtils.hasText(bizId)) {
            return null;
        }
        try {
            return productService.getProductById(bizId);
        } catch (Exception e) {
            log.warn("读取商品快照失败，bizId={}", bizId, e);
            return null;
        }
    }
}
