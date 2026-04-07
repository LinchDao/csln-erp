package com.lin.csln.dto.sys.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商店下拉列表DTO")
public class ShopListDTO {

    @Schema(description = "商店ID")
    private String id;

    @Schema(description = "商店名称")
    private String shopName;
}
