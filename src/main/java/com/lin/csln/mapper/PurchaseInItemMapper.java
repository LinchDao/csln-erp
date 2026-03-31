package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.csln.dto.purchase.in.PurchaseInItemRespDTO;
import com.lin.csln.entity.PurchaseInItemDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库明细表 Mapper接口
 *
 * @author 系统生成器
 */
@Mapper
public interface PurchaseInItemMapper extends BaseMapper<PurchaseInItemDO> {

    List<PurchaseInItemRespDTO> listPurchaseInItem(
            @Param("inId") String inId,
            @Param("purchaseId") String purchaseId
    );
}
