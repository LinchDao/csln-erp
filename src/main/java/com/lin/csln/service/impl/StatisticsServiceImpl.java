package com.lin.csln.service.impl;

import com.lin.csln.common.cache.CacheClient;
import com.lin.csln.common.constants.RedisKeyPrefixConstants;
import com.lin.csln.dto.sys.DailySalesDTO;
import com.lin.csln.dto.sys.GlobalSummaryDTO;
import com.lin.csln.dto.sys.TopCustomerOrderAmountDTO;
import com.lin.csln.dto.sys.TopCustomerProductQtyDTO;
import com.lin.csln.dto.sys.TopProductOrderQtyDTO;
import com.lin.csln.service.StatisticsService;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 核心统计服务实现 (NamedParameterJdbcTemplate 实现)
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final int DEFAULT_DAILY_SALES_DAYS = 15;
    private static final int TOP_PRODUCT_QUERY_DAYS = 30;
    private static final int TOP_PRODUCT_LIMIT = 5;
    private static final int TOP_CUSTOMER_QUERY_DAYS = 30;
    private static final int TOP_CUSTOMER_LIMIT = 3;
    private static final int TOP_CUSTOMER_PRODUCT_LIMIT = 3;
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");
    private static final String STATISTICS_CACHE_KEY_PREFIX = RedisKeyPrefixConstants.STATISTICS;
    private static final String GLOBAL_SUMMARY_CACHE_KEY = STATISTICS_CACHE_KEY_PREFIX + "summary";
    private static final String DAILY_SALES_CACHE_KEY_PREFIX = STATISTICS_CACHE_KEY_PREFIX + "daily-sales:";
    private static final String TOP_PRODUCTS_CACHE_KEY = STATISTICS_CACHE_KEY_PREFIX + "top-products-last-month";
    private static final String TOP_CUSTOMERS_CACHE_KEY = STATISTICS_CACHE_KEY_PREFIX + "top-customers-last-month";
    private static final String CACHE_LOCK_KEY_PREFIX = RedisKeyPrefixConstants.LOCK_STATISTICS;
    private static final long CACHE_TTL_MIN_SECONDS = 300L;
    private static final long CACHE_TTL_MAX_SECONDS = 600L;

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource
    private CacheClient cacheClient;

    @Override
    public GlobalSummaryDTO getGlobalSummary() {
        return cacheClient.getOrLoadObjectWithMutex(
                GLOBAL_SUMMARY_CACHE_KEY,
                GlobalSummaryDTO.class,
                this::queryGlobalSummary,
                randomTtlSeconds(),
                TimeUnit.SECONDS,
                CACHE_LOCK_KEY_PREFIX
        );
    }

    @Override
    public List<DailySalesDTO> getDailySales(Integer days) {
        int queryDays = (days == null || days <= 0) ? DEFAULT_DAILY_SALES_DAYS : days;
        String cacheKey = DAILY_SALES_CACHE_KEY_PREFIX + queryDays;
        return cacheClient.getOrLoadListWithMutex(
                cacheKey,
                DailySalesDTO.class,
                () -> queryDailySales(queryDays),
                randomTtlSeconds(),
                TimeUnit.SECONDS,
                CACHE_LOCK_KEY_PREFIX
        );
    }

    @Override
    public List<TopProductOrderQtyDTO> getTopProductsByOrderQtyInLastMonth() {
        return cacheClient.getOrLoadListWithMutex(
                TOP_PRODUCTS_CACHE_KEY,
                TopProductOrderQtyDTO.class,
                this::queryTopProductsByOrderQtyInLastMonth,
                randomTtlSeconds(),
                TimeUnit.SECONDS,
                CACHE_LOCK_KEY_PREFIX
        );
    }

    @Override
    public List<TopCustomerOrderAmountDTO> getTopCustomersWithTopProductsInLastMonth() {
        return cacheClient.getOrLoadListWithMutex(
                TOP_CUSTOMERS_CACHE_KEY,
                TopCustomerOrderAmountDTO.class,
                this::queryTopCustomersWithTopProductsInLastMonth,
                randomTtlSeconds(),
                TimeUnit.SECONDS,
                CACHE_LOCK_KEY_PREFIX
        );
    }

    private GlobalSummaryDTO queryGlobalSummary() {
        // 核心 SQL：跨表聚合
        // 1. 客户总数
        // 2. 订单总金额 (排除草稿单状态 4)
        // 3. 待出库数量 (子单状态 not in 1.4 的 item 数量之和)
        // 4. 已出库数量 (子单状态 = 4 的 item 数量之和)
        String sql = """
                SELECT 
                    (SELECT COUNT(1) FROM customer WHERE status = 1) as customerCount,
                    (SELECT SUM(total_amount) FROM order_master WHERE is_draft = 0) as totalOrderAmount,
                    (SELECT SUM(qty) FROM order_item i JOIN order_sub s ON i.sub_id = s.id WHERE s.status != 4 and s.status != 0 AND s.is_delete = 0) as pendingShipmentQty,
                    (SELECT SUM(qty) FROM order_item i JOIN order_sub s ON i.sub_id = s.id WHERE s.status = 4 AND s.is_delete = 0) as shippedQty
                """;

        return namedParameterJdbcTemplate.queryForObject(sql, Collections.emptyMap(),
                new BeanPropertyRowMapper<>(GlobalSummaryDTO.class));
    }

    private List<DailySalesDTO> queryDailySales(int queryDays) {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(queryDays - 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", java.sql.Date.valueOf(startDate));
        params.put("endExclusive", java.sql.Date.valueOf(endDate.plusDays(1)));

        Map<String, BigDecimal> salesAmountByDay = new HashMap<>();
        Map<String, Long> salesQtyByDay = new HashMap<>();
        fillOrderStatsByDay(params, salesAmountByDay, salesQtyByDay);
        Map<String, Long> shipmentQtyByDay = queryShipmentQtyByDay(params);

        // 3) 组装连续日期结果，对无数据日期补 0
        return buildDailySalesResult(startDate, endDate, queryDays, salesAmountByDay, salesQtyByDay, shipmentQtyByDay);
    }

    private List<TopProductOrderQtyDTO> queryTopProductsByOrderQtyInLastMonth() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(TOP_PRODUCT_QUERY_DAYS - 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", java.sql.Date.valueOf(startDate));
        params.put("endExclusive", java.sql.Date.valueOf(endDate.plusDays(1)));
        params.put("limit", TOP_PRODUCT_LIMIT);

        String sql = """
                SELECT p.product_no AS productNo,
                       p.name AS productName,
                       SUM(oi.qty) AS qty
                FROM order_item oi
                JOIN order_sub s ON oi.sub_id = s.id AND s.is_delete = 0
                JOIN order_master om ON s.master_id = om.id
                JOIN product_sku ps ON oi.sku_id = ps.id
                JOIN product p ON ps.product_id = p.id
                WHERE om.is_draft = 0
                  AND om.create_time >= :startDate
                  AND om.create_time < :endExclusive
                GROUP BY p.id, p.product_no, p.name
                ORDER BY qty DESC, p.product_no ASC
                LIMIT :limit
                """;

        return namedParameterJdbcTemplate.query(sql, params,
                new BeanPropertyRowMapper<>(TopProductOrderQtyDTO.class));
    }

    private List<TopCustomerOrderAmountDTO> queryTopCustomersWithTopProductsInLastMonth() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(TOP_CUSTOMER_QUERY_DAYS - 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", java.sql.Date.valueOf(startDate));
        params.put("endExclusive", java.sql.Date.valueOf(endDate.plusDays(1)));
        params.put("limit", TOP_CUSTOMER_LIMIT);

        String customerTopSql = """
                SELECT om.customer_id AS customerId,
                       c.name AS customerName,
                       SUM(om.total_amount) AS orderAmount
                FROM order_master om
                LEFT JOIN customer c ON om.customer_id = c.id
                WHERE om.is_draft = 0
                  AND om.create_time >= :startDate
                  AND om.create_time < :endExclusive
                  AND EXISTS (
                      SELECT 1
                      FROM order_sub s
                      WHERE s.master_id = om.id
                        AND s.is_delete = 0
                  )
                GROUP BY om.customer_id, c.name
                ORDER BY orderAmount DESC, customerName ASC
                LIMIT :limit
                """;

        List<Map<String, Object>> customerRows = namedParameterJdbcTemplate.queryForList(customerTopSql, params);
        if (customerRows.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> customerIds = new ArrayList<>(customerRows.size());
        List<TopCustomerOrderAmountDTO> result = new ArrayList<>(customerRows.size());
        for (Map<String, Object> row : customerRows) {
            String customerId = Objects.toString(row.get("customerId"), null);
            if (customerId == null) {
                continue;
            }
            customerIds.add(customerId);

            TopCustomerOrderAmountDTO dto = new TopCustomerOrderAmountDTO();
            dto.setCustomerName(Objects.toString(row.get("customerName"), ""));
            dto.setOrderAmount(toBigDecimal(row.get("orderAmount")));
            dto.setTopProducts(new ArrayList<>());
            result.add(dto);
        }

        if (customerIds.isEmpty() || result.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<TopCustomerProductQtyDTO>> topProductsByCustomer =
                queryTopProductsByCustomer(customerIds, startDate, endDate);

        for (int i = 0; i < customerIds.size(); i++) {
            String customerId = customerIds.get(i);
            result.get(i).setTopProducts(topProductsByCustomer.getOrDefault(customerId, Collections.emptyList()));
        }
        return result;
    }

    private Map<String, List<TopCustomerProductQtyDTO>> queryTopProductsByCustomer(List<String> customerIds,
                                                                                    LocalDate startDate,
                                                                                    LocalDate endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerIds", customerIds);
        params.put("startDate", java.sql.Date.valueOf(startDate));
        params.put("endExclusive", java.sql.Date.valueOf(endDate.plusDays(1)));

        String productTopSql = """
                SELECT om.customer_id AS customerId,
                       p.product_no AS productNo,
                       p.name AS productName,
                       SUM(oi.qty) AS qty
                FROM order_item oi
                JOIN order_sub s ON oi.sub_id = s.id AND s.is_delete = 0
                JOIN order_master om ON s.master_id = om.id
                JOIN product_sku ps ON oi.sku_id = ps.id
                JOIN product p ON ps.product_id = p.id
                WHERE om.is_draft = 0
                  AND om.create_time >= :startDate
                  AND om.create_time < :endExclusive
                  AND om.customer_id IN (:customerIds)
                GROUP BY om.customer_id, p.id, p.product_no, p.name
                ORDER BY om.customer_id ASC, qty DESC, p.product_no ASC
                """;

        List<Map<String, Object>> productRows = namedParameterJdbcTemplate.queryForList(productTopSql, params);
        Map<String, List<TopCustomerProductQtyDTO>> grouped = new HashMap<>();
        for (Map<String, Object> row : productRows) {
            String customerId = Objects.toString(row.get("customerId"), null);
            if (customerId == null) {
                continue;
            }
            List<TopCustomerProductQtyDTO> topProducts =
                    grouped.computeIfAbsent(customerId, key -> new ArrayList<>(TOP_CUSTOMER_PRODUCT_LIMIT));
            if (topProducts.size() >= TOP_CUSTOMER_PRODUCT_LIMIT) {
                continue;
            }

            TopCustomerProductQtyDTO productDTO = new TopCustomerProductQtyDTO();
            productDTO.setProductNo(Objects.toString(row.get("productNo"), ""));
            productDTO.setProductName(Objects.toString(row.get("productName"), ""));
            productDTO.setQty(toLong(row.get("qty")));
            topProducts.add(productDTO);
        }
        return grouped;
    }

    private long randomTtlSeconds() {
        return ThreadLocalRandom.current().nextLong(CACHE_TTL_MIN_SECONDS, CACHE_TTL_MAX_SECONDS + 1);
    }


    /**
     * 按 order_master.create_time 统计每日订单金额与订单件数。
     */
    private void fillOrderStatsByDay(Map<String, Object> params, Map<String, BigDecimal> salesAmountByDay,
                                     Map<String, Long> salesQtyByDay) {
        String sql = """
                SELECT DATE_FORMAT(create_time, '%m-%d') AS statDay,
                       SUM(total_amount) AS salesAmount,
                       SUM(total_qty) AS salesQty
                FROM order_master
                WHERE is_draft = 0
                  AND create_time >= :startDate
                  AND create_time < :endExclusive
                GROUP BY DATE_FORMAT(create_time, '%m-%d')
                """;

        List<Map<String, Object>> orderRows = namedParameterJdbcTemplate.queryForList(sql, params);
        for (Map<String, Object> row : orderRows) {
            String day = (String) row.get("statDay");
            salesAmountByDay.put(day, toBigDecimal(row.get("salesAmount")));
            salesQtyByDay.put(day, toLong(row.get("salesQty")));
        }
    }

    /**
     * 按 order_sub.actual_send_date 统计每日出库件数。
     */
    private Map<String, Long> queryShipmentQtyByDay(Map<String, Object> params) {
        Map<String, Long> shipmentQtyByDay = new HashMap<>();
        String sql = """
                SELECT DATE_FORMAT(s.actual_send_date, '%m-%d') AS statDay,
                       SUM(i.actual_qty) AS shipmentQty
                FROM order_sub s
                JOIN order_master m ON m.id = s.master_id
                JOIN order_item i ON i.sub_id = s.id
                WHERE s.is_delete = 0
                  AND m.is_draft = 0
                  and s.status = 4
                  AND s.actual_send_date >= :startDate
                  AND s.actual_send_date < :endExclusive
                GROUP BY DATE_FORMAT(s.actual_send_date, '%m-%d')
                """;
        List<Map<String, Object>> shipmentRows = namedParameterJdbcTemplate.queryForList(sql, params);
        for (Map<String, Object> row : shipmentRows) {
            String day = (String) row.get("statDay");
            shipmentQtyByDay.put(day, toLong(row.get("shipmentQty")));
        }
        return shipmentQtyByDay;
    }

    /**
     * 组装连续日期趋势数据，对缺失日期补 0。
     */
    private List<DailySalesDTO> buildDailySalesResult(LocalDate startDate, LocalDate endDate, int queryDays, Map<String, BigDecimal> salesAmountByDay, Map<String, Long> salesQtyByDay, Map<String, Long> shipmentQtyByDay) {
        List<DailySalesDTO> result = new ArrayList<>(queryDays);
        for (LocalDate day = startDate; !day.isAfter(endDate); day = day.plusDays(1)) {
            String dayStr = day.format(DAY_FORMATTER);
            DailySalesDTO dto = new DailySalesDTO();
            dto.setDay(dayStr);
            dto.setSalesAmount(salesAmountByDay.getOrDefault(dayStr, BigDecimal.ZERO));
            dto.setSalesQty(salesQtyByDay.getOrDefault(dayStr, 0L));
            dto.setShipmentQty(shipmentQtyByDay.getOrDefault(dayStr, 0L));
            result.add(dto);
        }
        return result;

    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }
        if (value instanceof Number number) {
            return new BigDecimal(number.toString());
        }
        return new BigDecimal(value.toString());
    }

    private Long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(value.toString());
    }

}
