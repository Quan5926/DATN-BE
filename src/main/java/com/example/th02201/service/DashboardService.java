package com.example.th02201.service;

import com.example.th02201.dto.HoaDonDTO;
import com.example.th02201.model.HoaDon;
import com.example.th02201.model.LichSuThanhToan;
import com.example.th02201.repository.ChiTietHoaDonRepository;
import com.example.th02201.repository.HoaDonRepository;
import com.example.th02201.repository.SanPhamChiTietQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    private final HoaDonRepository hoaDonRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final SanPhamChiTietQRepository sanPhamChiTietRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardData(LocalDate fromDate, LocalDate toDate) {
        // Ensure fromDate is not after toDate
        if (fromDate.isAfter(toDate)) {
            LocalDate temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }

        Map<String, Object> dashboardData = new HashMap<>();

        // Convert LocalDate to LocalDateTime for queries
        LocalDateTime fromLocalDateTime = fromDate.atStartOfDay();
        LocalDateTime toLocalDateTime = toDate.plusDays(1).atStartOfDay().minusNanos(1);

        logger.info("Fetching dashboard data from {} to {}", fromLocalDateTime, toLocalDateTime);

        // --- 1. Total revenue, order count, new customers ---
        try {
            dashboardData.put("totalRevenue", hoaDonRepository.getTotalRevenue(fromLocalDateTime, toLocalDateTime));
            dashboardData.put("orderCount", hoaDonRepository.getOrderCount(fromLocalDateTime, toLocalDateTime));
            dashboardData.put("newCustomers", hoaDonRepository.getNewCustomers(fromLocalDateTime, toLocalDateTime));
        } catch (Exception e) {
            logger.error("Error fetching core dashboard stats: {}", e.getMessage(), e);
            dashboardData.put("totalRevenue", BigDecimal.ZERO);
            dashboardData.put("orderCount", 0L);
            dashboardData.put("newCustomers", 0L);
        }

        // --- 2. Revenue for today, this week, this month ---
        try {
            dashboardData.put("totalRevenueToday", hoaDonRepository.getTotalRevenueForToday());
            dashboardData.put("totalRevenueThisWeek", hoaDonRepository.getTotalRevenueForCurrentWeek());
            dashboardData.put("totalRevenueThisMonth", hoaDonRepository.getTotalRevenueForCurrentMonth());
        } catch (Exception e) {
            logger.error("Error fetching current period revenues: {}", e.getMessage(), e);
            dashboardData.put("totalRevenueToday", BigDecimal.ZERO);
            dashboardData.put("totalRevenueThisWeek", BigDecimal.ZERO);
            dashboardData.put("totalRevenueThisMonth", BigDecimal.ZERO);
        }

        // --- 3. Top selling products ---
        try {
            List<Object[]> rawTopSellingProducts = chiTietHoaDonRepository.getTopSellingProducts(fromLocalDateTime, toLocalDateTime);
            List<Map<String, Object>> topSellingProducts = rawTopSellingProducts.stream()
                    .map(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        try {
                            map.put("productName", obj[0] != null ? obj[0].toString() : "N/A");
                            map.put("quantity", obj[1] != null ? ((Number) obj[1]).intValue() : 0);
                            map.put("revenue", obj[2] != null ? (BigDecimal) obj[2] : BigDecimal.ZERO);
                        } catch (Exception e) {
                            logger.warn("Error mapping top selling product data for obj: {}. Error: {}", java.util.Arrays.toString(obj), e.getMessage());
                        }
                        return map;
                    })
                    .collect(Collectors.toList());
            dashboardData.put("topSellingProducts", topSellingProducts);
        } catch (Exception e) {
            logger.error("Error fetching top selling products: {}", e.getMessage(), e);
            dashboardData.put("topSellingProducts", Collections.emptyList());
        }

        // --- 4. Order status data ---
        try {
            List<Object[]> rawOrderStatusData = hoaDonRepository.getOrderStatusData(fromLocalDateTime, toLocalDateTime);
            List<Map<String, Object>> orderStatusData = rawOrderStatusData.stream()
                    .map(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        try {
                            map.put("statusId", obj[0] != null ? ((Number) obj[0]).intValue() : -1);
                            map.put("count", obj[1] != null ? ((Number) obj[1]).longValue() : 0L);
                        } catch (Exception e) {
                            logger.warn("Error mapping order status data for obj: {}. Error: {}", java.util.Arrays.toString(obj), e.getMessage());
                        }
                        return map;
                    })
                    .collect(Collectors.toList());
            dashboardData.put("orderStatusData", orderStatusData);
        } catch (Exception e) {
            logger.error("Error fetching order status data: {}", e.getMessage(), e);
            dashboardData.put("orderStatusData", Collections.emptyList());
        }

        // --- 5. Daily revenue trend data ---
        try {
            List<Object[]> rawRevenueData = hoaDonRepository.getRevenueData(fromLocalDateTime, toLocalDateTime);
            List<Map<String, Object>> revenueData = rawRevenueData.stream()
                    .map(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        try {
                            map.put("date", obj[0] != null ? obj[0].toString() : "N/A");
                            BigDecimal revenueValue;
                            if (obj[1] instanceof BigDecimal) {
                                revenueValue = (BigDecimal) obj[1];
                            } else if (obj[1] instanceof Number) {
                                revenueValue = new BigDecimal(obj[1].toString());
                            } else {
                                revenueValue = BigDecimal.ZERO;
                            }
                            map.put("revenue", revenueValue);
                        } catch (Exception e) {
                            logger.warn("Error mapping revenue data for obj: {}. Error: {}", java.util.Arrays.toString(obj), e.getMessage());
                        }
                        return map;
                    })
                    .collect(Collectors.toList());
            dashboardData.put("revenueData", revenueData);
        } catch (Exception e) {
            logger.error("Error fetching daily revenue data: {}", e.getMessage(), e);
            dashboardData.put("revenueData", Collections.emptyList());
        }

        // --- 6. Revenue by weekday ---
        try {
            List<Object[]> rawRevenueByWeekDay = hoaDonRepository.getRevenueByWeekDay(fromLocalDateTime, toLocalDateTime);
            List<Map<String, Object>> revenueByWeekDay = rawRevenueByWeekDay.stream()
                    .map(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        try {
                            Integer dayNumber = obj[0] != null ? ((Number) obj[0]).intValue() : 0;
                            String dayName = switch (dayNumber) {
                                case 1 -> "Chủ Nhật";
                                case 2 -> "Thứ Hai";
                                case 3 -> "Thứ Ba";
                                case 4 -> "Thứ Tư";
                                case 5 -> "Thứ Năm";
                                case 6 -> "Thứ Sáu";
                                case 7 -> "Thứ Bảy";
                                default -> "Không xác định";
                            };
                            map.put("day", dayName);
                            BigDecimal revenueValue;
                            if (obj[1] instanceof BigDecimal) {
                                revenueValue = (BigDecimal) obj[1];
                            } else if (obj[1] instanceof Number) {
                                revenueValue = new BigDecimal(obj[1].toString());
                            } else {
                                revenueValue = BigDecimal.ZERO;
                            }
                            map.put("revenue", revenueValue);
                        } catch (Exception e) {
                            logger.warn("Error mapping revenue by weekday data for obj: {}. Error: {}", java.util.Arrays.toString(obj), e.getMessage());
                        }
                        return map;
                    })
                    .collect(Collectors.toList());
            dashboardData.put("revenueByWeekDay", revenueByWeekDay);
        } catch (Exception e) {
            logger.error("Error fetching revenue by weekday data: {}", e.getMessage(), e);
            dashboardData.put("revenueByWeekDay", Collections.emptyList());
        }

        // --- 7. Revenue by hour of day ---
        try {
            List<Object[]> rawRevenueByHour = hoaDonRepository.getRevenueByHour(fromLocalDateTime, toLocalDateTime);
            List<Map<String, Object>> revenueByHour = rawRevenueByHour.stream()
                    .map(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        try {
                            map.put("hour", obj[0] != null ? ((Number) obj[0]).intValue() : 0);
                            BigDecimal revenueValue;
                            if (obj[1] instanceof BigDecimal) {
                                revenueValue = (BigDecimal) obj[1];
                            } else if (obj[1] instanceof Number) {
                                revenueValue = new BigDecimal(obj[1].toString());
                            } else {
                                revenueValue = BigDecimal.ZERO;
                            }
                            map.put("revenue", revenueValue);
                        } catch (Exception e) {
                            logger.warn("Error mapping revenue by hour data for obj: {}. Error: {}", java.util.Arrays.toString(obj), e.getMessage());
                        }
                        return map;
                    })
                    .collect(Collectors.toList());
            dashboardData.put("revenueByHour", revenueByHour);
        } catch (Exception e) {
            logger.error("Error fetching revenue by hour data: {}", e.getMessage(), e);
            dashboardData.put("revenueByHour", Collections.emptyList());
        }

        // --- 8. Low stock products ---
        try {
            List<Object[]> rawLowStockProducts = sanPhamChiTietRepository.getLowStockProducts();
            List<Map<String, Object>> lowStockProducts = rawLowStockProducts.stream()
                    .map(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        try {
                            map.put("product", obj.length > 0 && obj[0] != null ? obj[0].toString() : "N/A");
                            map.put("stock", obj.length > 1 && obj[1] != null ? ((Number) obj[1]).intValue() : 0);
                            map.put("color", obj.length > 2 && obj[2] != null ? obj[2].toString() : "N/A");
                            map.put("size", obj.length > 3 && obj[3] != null ? obj[3].toString() : "N/A");
                            map.put("material", obj.length > 4 && obj[4] != null ? obj[4].toString() : "N/A");
                            map.put("ctspCode", obj.length > 5 && obj[5] != null ? obj[5].toString() : "N/A");
                        } catch (Exception e) {
                            logger.warn("Error mapping low stock product data for obj: {}. Error: {}", java.util.Arrays.toString(obj), e.getMessage());
                        }
                        return map;
                    })
                    .collect(Collectors.toList());
            dashboardData.put("lowStockProducts", lowStockProducts);
        } catch (Exception e) {
            logger.error("Error fetching low stock products: {}", e.getMessage(), e);
            dashboardData.put("lowStockProducts", Collections.emptyList());
        }

        // --- 9. General inventory statistics ---
        try {
            List<Object[]> rawInventoryStats = sanPhamChiTietRepository.getInventoryStats();
            Map<String, Object> inventoryStats = new HashMap<>();
            if (rawInventoryStats != null && !rawInventoryStats.isEmpty() && rawInventoryStats.get(0).length >= 2) {
                try {
                    inventoryStats.put("totalProducts", rawInventoryStats.get(0)[0] != null ? ((Number) rawInventoryStats.get(0)[0]).longValue() : 0L);
                    inventoryStats.put("totalStock", rawInventoryStats.get(0)[1] != null ? ((Number) rawInventoryStats.get(0)[1]).longValue() : 0L);
                } catch (Exception e) {
                    logger.warn("Error mapping inventory stats data for obj: {}. Error: {}", java.util.Arrays.toString(rawInventoryStats.get(0)), e.getMessage());
                    inventoryStats.put("totalProducts", 0L);
                    inventoryStats.put("totalStock", 0L);
                }
            } else {
                inventoryStats.put("totalProducts", 0L);
                inventoryStats.put("totalStock", 0L);
            }
            dashboardData.put("inventoryStats", inventoryStats);
        } catch (Exception e) {
            logger.error("Error fetching inventory stats: {}", e.getMessage(), e);
            dashboardData.put("inventoryStats", new HashMap<>());
        }

        // --- 10. Revenue by product category ---
        try {
            List<Object[]> rawCategoryRevenue = chiTietHoaDonRepository.getProductRevenue(fromLocalDateTime, toLocalDateTime);
            List<Map<String, Object>> categoryRevenue = rawCategoryRevenue.stream()
                    .map(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        try {
                            map.put("category", obj[0] != null ? obj[0].toString() : "N/A");
                            BigDecimal revenueValue;
                            if (obj[1] instanceof BigDecimal) {
                                revenueValue = (BigDecimal) obj[1];
                            } else if (obj[1] instanceof Number) {
                                revenueValue = new BigDecimal(obj[1].toString());
                            } else {
                                revenueValue = BigDecimal.ZERO;
                            }
                            map.put("revenue", revenueValue);
                        } catch (Exception e) {
                            logger.warn("Error mapping category revenue data for obj: {}. Error: {}", java.util.Arrays.toString(obj), e.getMessage());
                        }
                        return map;
                    })
                    .collect(Collectors.toList());
            dashboardData.put("categoryRevenue", categoryRevenue);
        } catch (Exception e) {
            logger.error("Error fetching category revenue: {}", e.getMessage(), e);
            dashboardData.put("categoryRevenue", Collections.emptyList());
        }

        // --- 11. KPI metrics (updated) ---
        try {
            Long totalOrders = hoaDonRepository.countByNgayTaoBetween(fromLocalDateTime, toLocalDateTime);
            Long completedOrders = hoaDonRepository.countByTrangThaiIdAndNgayTaoBetween(21, fromLocalDateTime, toLocalDateTime);
            Long cancelledOrders = hoaDonRepository.countByTrangThaiIdAndNgayTaoBetween(22, fromLocalDateTime, toLocalDateTime);
            Long totalCustomers = hoaDonRepository.countDistinctCustomersInPeriod(fromLocalDateTime, toLocalDateTime);

            BigDecimal totalRevenueValue = hoaDonRepository.getTotalRevenue(fromLocalDateTime, toLocalDateTime);
            if (totalRevenueValue == null) {
                totalRevenueValue = BigDecimal.ZERO;
            }

            double completionRate = totalOrders > 0 ? (completedOrders * 100.0 / totalOrders) : 0.0;
            BigDecimal avgOrderValue = completedOrders > 0 ? totalRevenueValue.divide(BigDecimal.valueOf(completedOrders), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            double cancellationRate = totalOrders > 0 ? (cancelledOrders * 100.0 / totalOrders) : 0.0;
            BigDecimal customerLifetimeValue = totalCustomers > 0 ? totalRevenueValue.divide(BigDecimal.valueOf(totalCustomers), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

            Map<String, Object> kpiStats = new HashMap<>();
            kpiStats.put("completionRate", BigDecimal.valueOf(completionRate).setScale(2, RoundingMode.HALF_UP));
            kpiStats.put("avgOrderValue", avgOrderValue);
            kpiStats.put("cancellationRate", BigDecimal.valueOf(cancellationRate).setScale(2, RoundingMode.HALF_UP));
            kpiStats.put("customerLifetimeValue", customerLifetimeValue);

            dashboardData.put("kpiStats", kpiStats);
        } catch (Exception e) {
            logger.error("Error calculating KPI stats: {}", e.getMessage(), e);
            Map<String, Object> kpiStats = new HashMap<>();
            kpiStats.put("completionRate", BigDecimal.ZERO);
            kpiStats.put("avgOrderValue", BigDecimal.ZERO);
            kpiStats.put("cancellationRate", BigDecimal.ZERO);
            kpiStats.put("customerLifetimeValue", BigDecimal.ZERO);
            dashboardData.put("kpiStats", kpiStats);
        }

        // --- 12. Top loyal customers (updated) ---
        try {
            List<Object[]> rawTopCustomers = hoaDonRepository.getTopCustomersByTotalSpent(fromLocalDateTime, toLocalDateTime);
            List<Map<String, Object>> topCustomers = rawTopCustomers.stream()
                    .map(obj -> {
                        Map<String, Object> map = new HashMap<>();
                        try {
                            map.put("id", obj[0] != null ? ((Number) obj[0]).intValue() : 0);
                            map.put("name", obj[1] != null ? obj[1].toString() : "N/A");
                            map.put("orderCount", obj[2] != null ? ((Number) obj[2]).longValue() : 0L);
                            map.put("totalSpent", obj[3] != null ? (BigDecimal) obj[3] : BigDecimal.ZERO);
                        } catch (Exception e) {
                            logger.warn("Error mapping top customer data for obj: {}. Error: {}", java.util.Arrays.toString(obj), e.getMessage());
                        }
                        return map;
                    })
                    .sorted(Comparator.comparing(m -> (BigDecimal) m.get("totalSpent"), Comparator.reverseOrder()))
                    .limit(10)
                    .collect(Collectors.toList());
            dashboardData.put("topCustomers", topCustomers);
        } catch (Exception e) {
            logger.error("Error fetching top customers: {}", e.getMessage(), e);
            dashboardData.put("topCustomers", Collections.emptyList());
        }

        logger.info("Finished fetching dashboard data.");
        return dashboardData;
    }

    @Transactional(readOnly = true)
    public List<HoaDonDTO> getInvoices(LocalDate fromDate, LocalDate toDate, Integer statusId) {
        LocalDateTime startOfDay = fromDate.atStartOfDay();
        LocalDateTime endOfDay = toDate.plusDays(1).atStartOfDay().minusNanos(1);

        List<HoaDon> hoaDons;
        if (statusId != null) {
            hoaDons = hoaDonRepository.findByNgayTaoBetweenAndTrangThaiId(startOfDay, endOfDay, statusId);
        } else {
            hoaDons = hoaDonRepository.findByNgayTaoBetween(startOfDay, endOfDay);
        }

        if (hoaDons == null || hoaDons.isEmpty()) {
            return Collections.emptyList();
        }

        return hoaDons.stream()
                .map(hoaDon -> {
                    HoaDonDTO dto = new HoaDonDTO();
                    dto.setId(hoaDon.getId());
                    dto.setMaHoaDon(hoaDon.getMaHoaDon());
                    dto.setNgayTao(hoaDon.getNgayTao());
                    dto.setTongTienThanhToan(hoaDon.getTongTienThanhToan());
                    dto.setPhiVanChuyen(hoaDon.getPhiVanChuyen());
                    if (hoaDon.getMaGiamGia() != null && hoaDon.getMaGiamGia().getPhieuGiamGia() != null) {
                        dto.setGiaTriGiam(hoaDon.getMaGiamGia().getPhieuGiamGia().getGiaTriGiam());
                    } else {
                        dto.setGiaTriGiam(BigDecimal.ZERO);
                    }

                    if (hoaDon.getKhachHang() != null) {
                        dto.setKhachHangId(hoaDon.getKhachHang().getId());
                        dto.setTenKhachHang(hoaDon.getKhachHang().getTenKhachHang());
                        dto.setSoDienThoaiKhachHang(hoaDon.getKhachHang().getSoDienThoai());
                    }

                    if (hoaDon.getTrangThai() != null) {
                        dto.setTrangThaiId(hoaDon.getTrangThai().getId());
                        dto.setTenTrangThai(hoaDon.getTrangThai().getTenTrangThai());
                    }

                    if (hoaDon.getLichSuThanhToan() != null && !hoaDon.getLichSuThanhToan().isEmpty()) {
                        hoaDon.getLichSuThanhToan().stream()
                                .max(Comparator.comparing(LichSuThanhToan::getThoiGianThanhToan))
                                .ifPresent(latestPayment -> {
                                    dto.setNgayThanhToan(latestPayment.getThoiGianThanhToan());
                                    dto.setLoaiThanhToan(latestPayment.getGhiChuThanhToan());
                                    dto.setSoTienThanhToanFinal(latestPayment.getSoTienThanhToan());
                                });
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}