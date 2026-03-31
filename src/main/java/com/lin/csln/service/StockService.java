package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.entity.StockDO;

/**
 * 库存表 服务接口
 *
 * @author 系统生成器
 */
public interface StockService extends IService<StockDO> {

    void purchaseIn(String inId, String warehouseId);
}
