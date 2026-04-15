package com.lin.csln.dto.product;


import com.lin.csln.common.dto.PageQueryParamDTO;
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
@Schema(name = "ProductQueryParamDTO", description = "商品查询条件DTO")
public class ProductQueryParamDTO extends PageQueryParamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "款号（模糊查询）")
    private String productNo;

    @Schema(description = "商品名称（模糊查询）")
    private String name;

    @Schema(description = "状态（1：启用，0：禁用）")
    private Integer status;
}
