package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.stock.ProductStockPageRespDTO;
import com.lin.csln.dto.stock.ProductStockQueryParamDTO;
import com.lin.csln.entity.StockDO;

import java.util.List;

/**
 * 库存服务接口
 */
public interface StockService extends IService<StockDO> {

    void purchaseIn(String inId, String warehouseId);

    void adjustLockQty(String warehouseId, String skuId, int delta);

    void consumeLockedStock(String warehouseId, String skuId, int qty);

    boolean hasOccupiedStockBySkuIds(List<String> skuIds);

    PageRespDTO<ProductStockPageRespDTO> pageProductStock(ProductStockQueryParamDTO queryDTO);
}
