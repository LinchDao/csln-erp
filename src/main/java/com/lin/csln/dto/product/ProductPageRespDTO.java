package com.lin.csln.dto.product;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品基础DTO
 *
 * @Description:
 * @Author: linch
 */


@Data
@Schema(name = "ProductDTO", description = "商品信息DTO")
public class ProductPageRespDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "款号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productNo;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Schema(description = "默认批发价")
    private BigDecimal wholesalePrice;

    @Schema(description = "零售价")
    private BigDecimal retailPrice;

    @Schema(description = "商品主图")
    private String mainImageId;

    @Schema(description = "状态（1：启用，0：禁用）")
    private Integer status;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;
}
