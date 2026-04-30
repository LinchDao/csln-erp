package com.lin.csln.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "商品详情V2响应DTO")
public class ProductDetailV2RespDTO {

    @Schema(description = "商品ID")
    private String id;

    @Schema(description = "款号")
    private String productNo;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "品牌")
    private String brand;

    @Schema(description = "类目ID")
    private String categoryId;

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

    @Schema(description = "销售属性定义JSON")
    private String saleAttrsSchema;

    @Schema(description = "扩展信息JSON")
    private String extJson;

    @Schema(description = "产品SKU信息列表（V2）")
    private List<ProductSkuV2RespDTO> skuList;

    @Schema(description = "通用属性列表（V2）")
    private List<ProductV2AttrDTO> attrList;

    @Schema(description = "可挂图维度键列表（V2）")
    private List<String> mountDimKeys;
}
