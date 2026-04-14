package com.lin.csln.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.product.*;
import com.lin.csln.entity.ProductDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.ProductMapper;
import com.lin.csln.service.ProductColorImageService;
import com.lin.csln.service.ProductService;
import com.lin.csln.service.ProductSkuService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class ProductServiceImpl extends BaseReadonlyServiceImpl<ProductMapper, ProductDO> implements ProductService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Resource
    private ProductColorImageService productColorImageService;
    @Resource
    private ProductSkuService productSkuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addProduct(ProductDTO productDTO) {
        ProductDO product = new ProductDO();
        BeanUtils.copyProperties(productDTO, product);
        baseMapper.insert(product);


        String productId = product.getId();
        List<ProductColorDTO> colorList = productDTO.getColorList();
        if (colorList != null && !colorList.isEmpty()) {
            productColorImageService.saveProductColorImage(productId, colorList);
        }

        List<String> sizeNameList = productDTO.getSizeNameList();
        if (colorList != null && !colorList.isEmpty()
                && sizeNameList != null && !sizeNameList.isEmpty()) {
            productSkuService.saveProductSku(productId, colorList, sizeNameList);
        }

        return productId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(String productId, ProductDTO productDTO) {
        ProductDO product = baseMapper.selectById(productId);
        if (product == null || Objects.equals(product.getIsDelete(), GlobalEnums.YES.getCode())) {
            throw new BusinessException("更新失败，产品数据不存在。");
        }
        BeanUtils.copyProperties(productDTO, product, "id", "isDelete");
        baseMapper.updateById(product);


        List<ProductColorDTO> colorList = productDTO.getColorList();
        List<String> sizeNameList = productDTO.getSizeNameList();
        productSkuService.saveProductSku(productId, colorList, sizeNameList);
        productColorImageService.saveProductColorImage(productId, productDTO.getColorList());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(String id, String userId) {
        return false;
    }

    @Override
    public ProductDetailRespDTO getProductById(String productId) {
        // 1. 查询商品主表
        ProductDO product = baseMapper.selectById(productId);
        if (product == null) {
            return null;
        }


        List<ProductColorImageDTO> colorImageList = productColorImageService.listProductColorImage(productId);
        List<ProductSkuDTO> skuList = productSkuService.listSkuWithStockByProductId(productId);

        ProductDetailRespDTO respDTO = new ProductDetailRespDTO();
        BeanUtils.copyProperties(product, respDTO);

        respDTO.setSkuList(skuList);
        respDTO.setProductColorImageList(colorImageList);

        return respDTO;
    }

    @Override
    public PageRespDTO<ProductPageRespDTO> pageProduct(ProductQueryParamDTO queryDTO) {
        IPage<ProductPageRespDTO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        IPage<ProductPageRespDTO> result = baseMapper.pageProduct(page, queryDTO);
        return PageRespDTO.build(result, queryDTO);
    }

    @Override
    public List<ProductSelectDTO> listProductSelect(String productNo) {
        LambdaQueryWrapper<ProductDO> wrapper = new LambdaQueryWrapper<>();

        wrapper.select(ProductDO::getId, ProductDO::getProductNo, ProductDO::getName,
                ProductDO::getWholesalePrice, ProductDO::getRetailPrice, ProductDO::getMainImageId);
        wrapper.like(StringUtils.hasText(productNo), ProductDO::getProductNo, productNo);

        List<ProductDO> products = baseMapper.selectList(wrapper);

        return products.stream().map(p -> {
            ProductSelectDTO vo = new ProductSelectDTO();
            BeanUtils.copyProperties(p, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void exportProduct(ProductQueryParamDTO queryDTO, HttpServletResponse response) {
        ProductQueryParamDTO exportQuery = queryDTO == null ? new ProductQueryParamDTO() : queryDTO;
        int page = 1;
        int pageSize = 500;

        String fileName = "商品列表_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".xlsx";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodedFileName);

        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream(), ProductExportExcelDTO.class)
                    .autoCloseStream(Boolean.FALSE)
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet("商品列表").build();

            while (true) {
                exportQuery.setPage(page);
                exportQuery.setLimit(pageSize);
                PageRespDTO<ProductPageRespDTO> pageResp = pageProduct(exportQuery);
                List<ProductPageRespDTO> rows = pageResp.getRows();
                if (rows == null || rows.isEmpty()) {
                    break;
                }

                List<ProductExportExcelDTO> exportRows = rows.stream()
                        .map(this::convertToExportDTO)
                        .collect(Collectors.toList());
                excelWriter.write(exportRows, writeSheet);

                if (page >= pageResp.getTotalPages()) {
                    break;
                }
                page++;
            }
        } catch (Exception e) {
            throw new RuntimeException("导出商品失败：" + e.getMessage(), e);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private ProductExportExcelDTO convertToExportDTO(ProductPageRespDTO row) {
        ProductExportExcelDTO dto = new ProductExportExcelDTO();
        dto.setProductNo(row.getProductNo());
        dto.setName(row.getName());
        dto.setBrand(row.getBrand());
        dto.setSeason(row.getSeason());
        dto.setYear(row.getYear());
        dto.setSeries(row.getSeries());
        dto.setCostPrice(row.getCostPrice());
        dto.setWholesalePrice(row.getWholesalePrice());
        dto.setRetailPrice(row.getRetailPrice());
        dto.setStatusText(row.getStatus() == null ? "" : (row.getStatus() == 1 ? "启用" : "禁用"));
        dto.setCreateTime(formatDate(row.getCreateTime()));
        return dto;
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.format(DATE_TIME_FORMATTER);
    }


}
