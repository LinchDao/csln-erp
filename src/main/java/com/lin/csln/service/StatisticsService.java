package com.lin.csln.service;

import com.lin.csln.dto.sys.DailySalesDTO;
import com.lin.csln.dto.sys.GlobalSummaryDTO;
import com.lin.csln.dto.sys.TopCustomerOrderAmountDTO;
import com.lin.csln.dto.sys.TopProductOrderQtyDTO;
import java.util.List;

/**
 * 核心统计服务 (解耦版)
 */
public interface StatisticsService {

    /**
     * 获取全局概览指标
     */
    GlobalSummaryDTO getGlobalSummary();

    /**
     * 获取指定天数内的日均销售趋势
     * @param days 天数，如 7, 30
     */
    List<DailySalesDTO> getDailySales(Integer days);

    /**
     * 获取近一个月订单量前五商品
     */
    List<TopProductOrderQtyDTO> getTopProductsByOrderQtyInLastMonth();

    /**
     * 获取近一个月订单总金额最高的客户前七及客户内商品件数前三
     */
    List<TopCustomerOrderAmountDTO> getTopCustomersWithTopProductsInLastMonth();
}
