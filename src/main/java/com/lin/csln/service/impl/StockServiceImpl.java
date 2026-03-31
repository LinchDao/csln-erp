package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.entity.PurchaseInItemDO;
import com.lin.csln.entity.StockDO;
import com.lin.csln.mapper.StockMapper;
import com.lin.csln.service.PurchaseInItemService;
import com.lin.csln.service.StockService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库存表 服务实现类
 *
 * @author 系统生成器
 */
@Service
@Transactional(readOnly = true)
public class StockServiceImpl extends ServiceImpl<StockMapper, StockDO> implements StockService {

    @Resource
    private PurchaseInItemService purchaseInItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void purchaseIn(String inId, String warehouseId) {
        List<PurchaseInItemDO> itemList = purchaseInItemService.listPurchaseInItem(inId);

        for (PurchaseInItemDO item : itemList) {
            String skuId = item.getSkuId();
            int inQty = item.getQty();

            LambdaQueryWrapper<StockDO> stockQuery = new LambdaQueryWrapper<>();
            stockQuery.eq(StockDO::getWarehouseId, warehouseId)
                    .eq(StockDO::getSkuId, skuId);
            StockDO stock = baseMapper.selectOne(stockQuery);

            if (stock != null) {
                //  库存存在 → 增加可用库存
                StockDO updateStock = new StockDO();
                updateStock.setId(stock.getId());
                updateStock.setQty(stock.getQty() + inQty);
                baseMapper.updateById(updateStock);
            } else {
                //  库存不存在 → 新增库存
                StockDO newStock = new StockDO();
                newStock.setWarehouseId(warehouseId);
                newStock.setSkuId(skuId);
                newStock.setQty(inQty);
                newStock.setLockQty(0);
                newStock.setRecoveryQty(0);
                baseMapper.insert(newStock);
            }
        }

    }
}
