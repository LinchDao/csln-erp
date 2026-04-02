package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
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
public class StockServiceImpl extends BaseReadonlyServiceImpl<StockMapper, StockDO> implements StockService {

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustLockQty(String warehouseId, String skuId, int delta) {
        if (delta == 0) {
            return;
        }
        StockDO stock = baseMapper.selectOne(new LambdaQueryWrapper<StockDO>()
                .eq(StockDO::getWarehouseId, warehouseId)
                .eq(StockDO::getSkuId, skuId));
        if (stock == null) {
            throw new BusinessException("库存记录不存在，仓库：" + warehouseId + "，SKU：" + skuId);
        }
        int currentLockQty = stock.getLockQty() == null ? 0 : stock.getLockQty();
        int targetLockQty = currentLockQty + delta;
        if (targetLockQty < 0) {
            throw new BusinessException("锁定库存不足，仓库：" + warehouseId + "，SKU：" + skuId);
        }
        StockDO updateStock = new StockDO();
        updateStock.setId(stock.getId());
        updateStock.setLockQty(targetLockQty);
        baseMapper.updateById(updateStock);
    }
}
