package com.lin.csln.dto.product;

import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "ProductV2QueryParamDTO", description = "V2商品查询条件DTO")
public class ProductV2QueryParamDTO extends PageQueryParamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "商品编码")
    private String productNo;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "状态（1：启用，0：禁用）")
    private Integer status;

    @Schema(description = "类目ID")
    private String categoryId;
}
