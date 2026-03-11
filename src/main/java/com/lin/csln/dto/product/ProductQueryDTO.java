package com.lin.csln.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品查询条件DTO
 *
 * @Description:
 * @Author: linch
 */

@Data
@Schema(name = "ProductQueryDTO", description = "商品查询条件DTO")
public class ProductQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "款号（模糊查询）", example = "PROD2026")
    private String productNo;

    @Schema(description = "商品名称（模糊查询）", example = "T恤")
    private String name;

    @Schema(description = "品牌", example = "XX服饰")
    private String brand;

    @Schema(description = "季节", example = "春季")
    private String season;

    @Schema(description = "年份", example = "2026")
    private String year;

    @Schema(description = "状态（1：启用，0：禁用）", example = "1")
    private Integer status;

    @Schema(description = "页码（默认1）", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "页码（默认10）", example = "10")
    private Integer pageSize = 10;
}
