package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.csln.dto.purchase.PurchaseOrderDTO;
import com.lin.csln.dto.purchase.PurchaseOrderPageRespDTO;
import com.lin.csln.dto.purchase.PurchaseOrderQueryParamDTO;
import com.lin.csln.entity.PurchaseOrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 采购订单 Mapper接口
 *
 * @author 系统生成器
 */
@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrderDO> {

    IPage<PurchaseOrderPageRespDTO> pagePurchaseOrder(IPage<PurchaseOrderPageRespDTO> page, @Param("query") PurchaseOrderQueryParamDTO queryDTO);

    PurchaseOrderDTO getPurchaseDetailById(String id);
}
