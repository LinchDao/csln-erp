package com.lin.csln.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: linch
 */

@Data
@Schema(name = "ProductColorDTO", description = "商品颜色DTO")
public class ProductColorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "颜色记录ID")
    private String id;

    @Schema(description = "颜色名称")
    private String colorName;

    @Schema(description = "颜色图片ID列表")
    private List<String> colorImageIdList;

    @Schema(description = "该颜色对应的SKU ID列表")
    private List<String> skuIdList;
}
