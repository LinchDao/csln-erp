package com.lin.csln.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(name = "ProductPageV2RespDTO", description = "V2商品分页DTO")
public class ProductPageV2RespDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "商品编码")
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

    @Schema(description = "主图ID")
    private String mainImageId;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;
}
