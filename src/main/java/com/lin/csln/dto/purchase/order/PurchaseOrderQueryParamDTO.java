package com.lin.csln.dto.purchase.order;


import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: linch
 */
@Data
@Schema(description = "采购订单分页查询参数")
public class PurchaseOrderQueryParamDTO extends PageQueryParamDTO {

    @Schema(description = "采购单号")
    private String purchaseNo;

    @Schema(description = "供应商ID")
    private String supplierId;

    @Schema(description = "订单状态 0待入库 1部分入库 2已完成 3取消")
    private Integer status;

    @Schema(description = "下单时间起")
    private Date startTime;
    @Schema(description = "下单时间止")
    private Date endTime;


}
