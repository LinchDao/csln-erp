package com.lin.csln.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description:
 * @Author: linch
 */

@Data
@Schema(name = "ProductDTO", description = "商品DTO")
public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品ID")
    private String id;

    @Schema(description = "商品编号")
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

    @Schema(description = "状态 0-禁用 1-启用")
    private Integer status;

    @Schema(description = "主图ID")
    private String mainImageId;

    @Schema(description = "选中的尺码名称列表")
    private List<String> sizeNameList;

    @Schema(description = "颜色信息列表")
    private List<ProductColorDTO> colorList;
}
