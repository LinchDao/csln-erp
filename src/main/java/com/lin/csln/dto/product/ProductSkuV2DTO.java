package com.lin.csln.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "V2商品SKU DTO")
public class ProductSkuV2DTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "SKU主键ID")
    private String id;

    @Schema(description = "条码")
    private String barcode;

    @Schema(description = "维度数组")
    private List<ProductSkuDimV2DTO> dims;
}
