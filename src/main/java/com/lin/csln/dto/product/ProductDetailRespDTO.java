package com.lin.csln.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description:
 * @Author: linch
 */

@Data
@Schema(description = "商品详情响应DTO")
public class ProductDetailRespDTO {

    @Schema(description = "商品ID")
    private String id;

    @Schema(description = "款号")
    private String productNo;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "品牌")
    private String brand;

    @Schema(description = "季节")
    private String season;

    @Schema(description = "年份")
    private String year;

    @Schema(description = "系列")
    private String series;

    @Schema(description = "成本价")
    private BigDecimal costPrice;

    @Schema(description = "批发价")
    private BigDecimal wholesalePrice;

    @Schema(description = "零售价")
    private BigDecimal retailPrice;

    @Schema(description = "状态 1-启用 0-禁用")
    private Integer status;

    @Schema(description = "主图ID")
    private String mainImageId;
    @Schema(description = "产品SKU信息列表")
    private List<ProductSkuDTO> skuList;

    @Schema(description = "颜色图片列表")
    private List<ProductColorImageDTO> productColorImageList;
}