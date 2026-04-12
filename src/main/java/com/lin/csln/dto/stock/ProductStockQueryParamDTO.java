package com.lin.csln.dto.stock;

import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品库存分页查询参数
 */
@Data
@Schema(name = "ProductStockQueryParamDTO", description = "商品库存分页查询参数")
public class ProductStockQueryParamDTO extends PageQueryParamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "款号（模糊查询）")
    private String productNo;

    @Schema(description = "商品名称（模糊查询）")
    private String name;

    @Schema(description = "仓库ID（精确查询）")
    private String warehouseId;
}
