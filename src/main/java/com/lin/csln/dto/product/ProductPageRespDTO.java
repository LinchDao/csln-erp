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

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "款号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PROD20260310001")
    private String productNo;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "春季纯棉T恤")
    private String name;

    @Schema(description = "品牌", example = "XX服饰")
    private String brand;

    @Schema(description = "季节", example = "春季")
    private String season;

    @Schema(description = "年份", example = "2026")
    private String year;

    @Schema(description = "系列", example = "休闲系列")
    private String series;

    @Schema(description = "成本价", example = "59.90")
    private BigDecimal costPrice;

    @Schema(description = "默认批发价", example = "89.90")
    private BigDecimal wholesalePrice;

    @Schema(description = "零售价", example = "159.90")
    private BigDecimal retailPrice;

    @Schema(description = "商品主图", example = "1")
    private Long mainImageId;

    @Schema(description = "状态（1：启用，0：禁用）", example = "1")
    private Integer status;

    @Schema(description = "创建时间", example = "2026-03-10 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;
}
