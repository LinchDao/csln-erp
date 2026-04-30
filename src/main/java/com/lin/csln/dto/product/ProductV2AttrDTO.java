package com.lin.csln.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "V2商品通用属性")
public class ProductV2AttrDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "属性键")
    private String key;

    @Schema(description = "属性名")
    private String name;

    @Schema(description = "属性值")
    private String value;

    @Schema(description = "值类型")
    private String valueType;

    @Schema(description = "排序")
    private Integer sort;
}
