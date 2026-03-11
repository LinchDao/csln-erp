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

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "商品ID", example = "1")
    private Long productId;

    @Schema(description = "颜色ID", example = "2")
    private Long colorId;

    @Schema(description = "颜色名称（冗余字段，方便前端展示）", example = "黑色")
    private String colorName;

    @Schema(description = "颜色图片", example = "https://example.com/images/black.jpg")
    private String image;

    @Schema(description = "创建时间", example = "2026-03-10 10:00:00")
    private Date createTime;
}
