package com.lin.csln.dto.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 客户下商品件数 TOP DTO
 */
@Data
@Schema(description = "客户下商品件数排名")
public class TopCustomerProductQtyDTO {

    @Schema(description = "款号")
    private String productNo;

    @Schema(description = "名称")
    private String productName;

    @Schema(description = "数量")
    private Long qty;
}
