package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.dto.product.ProductDTO;
import com.lin.csln.dto.product.ProductQueryDTO;
import com.lin.csln.entity.ProductDO;
import com.lin.csln.mapper.ProductMapper;
import com.lin.csln.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商品表 服务实现类
 * @author 系统生成器
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductDO> implements ProductService {

    @Override
    public Long saveProduct(ProductDTO productDTO) {
        return 0L;
    }

    @Override
    public boolean updateProduct(ProductDTO productDTO) {
        return false;
    }

    @Override
    public boolean deleteProduct(Long id) {
        return false;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return null;
    }

    @Override
    public IPage<ProductDTO> pageProduct(Page<ProductDTO> page, ProductQueryDTO queryDTO) {
        return null;
    }
}
