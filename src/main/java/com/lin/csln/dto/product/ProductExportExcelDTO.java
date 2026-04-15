package com.lin.csln.dto.product;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品导出Excel DTO
 */
@Data
public class ProductExportExcelDTO {

    @ExcelProperty("款号")
    private String productNo;

    @ExcelProperty("商品名称")
    private String name;

    @ExcelProperty("品牌")
    private String brand;

    @ExcelProperty("季节")
    private String season;

    @ExcelProperty("年份")
    private String year;

    @ExcelProperty("系列")
    private String series;

    @ExcelProperty("成本价")
    private BigDecimal costPrice;

    @ExcelProperty("默认批发价")
    private BigDecimal wholesalePrice;

    @ExcelProperty("零售价")
    private BigDecimal retailPrice;

    @ExcelProperty("状态")
    private String statusText;

    @ExcelProperty("创建时间")
    private String createTime;
}
