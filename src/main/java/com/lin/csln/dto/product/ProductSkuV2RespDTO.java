package com.lin.csln.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "V2商品SKU响应DTO")
public class ProductSkuV2RespDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "商品ID")
    private String productId;

    @Schema(description = "条码")
    private String barcode;

    @Schema(description = "动态维度明细（V2）")
    private List<ProductSkuDimV2DTO> dims;
}
