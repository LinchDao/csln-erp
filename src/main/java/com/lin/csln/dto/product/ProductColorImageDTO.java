package com.lin.csln.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品颜色图片DTO
 *
 * @Description:
 * @Author: linch
 */


@Data
@Schema(name = "ProductColorImageDTO", description = "商品颜色图片DTO")
public class ProductColorImageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "商品ID")
    private String productId;

    @Schema(description = "颜色名称")
    private String colorName;

    @Schema(description = "颜色图片")
    private String colorFileId;

    @Schema(description = "创建时间")
    private Date createTime;
}
