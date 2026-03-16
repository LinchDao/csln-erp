package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.product.ProductDTO;
import com.lin.csln.dto.product.ProductPageRespDTO;
import com.lin.csln.dto.product.ProductQueryParamDTO;
import com.lin.csln.entity.ProductDO;
import com.lin.csln.mapper.ProductMapper;
import com.lin.csln.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品表 服务实现类
 *
 * @author 系统生成器
 */
@Service
@Transactional(readOnly = true)
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
    public boolean deleteProduct(Long id, String userId) {
        return false;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return null;
    }

    @Override
    public PageRespDTO<ProductPageRespDTO> pageProduct(ProductQueryParamDTO queryDTO) {
        Page<ProductPageRespDTO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        Page<ProductPageRespDTO> result = baseMapper.pageProduct(page, queryDTO);
        return PageRespDTO.of(result.getTotal(), result.getRecords(), queryDTO.getPage(), queryDTO.getLimit());
    }
}
