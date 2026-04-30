package com.lin.csln.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "V2 SKU维度项")
public class ProductSkuDimV2DTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "英文键")
    private String key;

    @Schema(description = "维度展示名")
    private String name;

    @Schema(description = "维度值")
    private String value;

    @Schema(description = "维度顺序")
    private Integer order;

    @Schema(description = "维度图片文件ID")
    private String imageFileId;
}
