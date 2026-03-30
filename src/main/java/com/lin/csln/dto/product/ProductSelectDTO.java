package com.lin.csln.dto.product;


/**
 * @Description:
 * @Author: linch
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商品下拉选择DTO")
public class ProductSelectDTO {

    @Schema(description = "商品ID")
    private String id;
    @Schema(description = "商品款号")
    private String productNo;
    @Schema(description = "商品名称")
    private String name;
    @Schema(description = "主图ID")
    private String mainImageId;
}
