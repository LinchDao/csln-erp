package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.dto.purchase.in.PurchaseInItemDTO;
import com.lin.csln.dto.purchase.in.PurchaseInItemRespDTO;
import com.lin.csln.entity.PurchaseInItemDO;

import java.util.List;

/**
 * 入库明细表 服务接口
 *
 * @author 系统生成器
 */
public interface PurchaseInItemService extends IService<PurchaseInItemDO> {

    void savePurchaseInItemList(String id, List<PurchaseInItemDTO> itemList);

    List<PurchaseInItemRespDTO> listPurchaseInItem(String inId, String purchaseId);

    List<PurchaseInItemDO> listPurchaseInItem(String inId);
}
