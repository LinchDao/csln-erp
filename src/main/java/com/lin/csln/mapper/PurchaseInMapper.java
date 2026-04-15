package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.dto.purchase.in.PurchaseInDetailRespDTO;
import com.lin.csln.dto.purchase.in.PurchaseInPageRespDTO;
import com.lin.csln.dto.purchase.in.PurchaseInQueryParamDTO;
import com.lin.csln.dto.purchase.in.PurchaseInstockedQtyDTO;
import com.lin.csln.entity.PurchaseInDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购入库单 Mapper接口
 *
 * @author 系统生成器
 */
@Mapper
public interface PurchaseInMapper extends BaseMapper<PurchaseInDO> {

    String getMaxInNoByPrefix(String prefix);

    List<PurchaseInstockedQtyDTO> selectSumInstockedQtyByPurchaseId(String purchaseId);

    IPage<PurchaseInPageRespDTO> selectPurchaseInPage(IPage<PurchaseInPageRespDTO> page
            , @Param("dto") PurchaseInQueryParamDTO dto);

    PurchaseInDetailRespDTO selectPurchaseInDetail(@Param("inId") String inId);
}
