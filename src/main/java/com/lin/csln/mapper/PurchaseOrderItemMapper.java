package com.lin.csln.mapper;

import com.lin.csln.dto.purchase.order.PurchaseOrderItemDTO;
import com.lin.csln.entity.PurchaseOrderItemDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 采购明细表 Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface PurchaseOrderItemMapper extends BaseMapper<PurchaseOrderItemDO> {

    List<PurchaseOrderItemDTO> listPurchaseOrderItemSku(String purchaseId);
}
