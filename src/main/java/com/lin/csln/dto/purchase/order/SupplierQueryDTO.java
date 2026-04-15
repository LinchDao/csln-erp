package com.lin.csln.dto.purchase.order;


/**
 * @Description:
 * @Author: linch
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "供应商查询DTO")
public class SupplierQueryDTO {
    @Schema(description = "供应商名称（模糊匹配）")
    private String name;
}
