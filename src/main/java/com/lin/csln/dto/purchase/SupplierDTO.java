package com.lin.csln.dto.purchase;


/**
 * @Description:
 * @Author: linch
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "供应商DTO")
public class SupplierDTO {
    private String id;
    private String name;
    private String phone;
}
