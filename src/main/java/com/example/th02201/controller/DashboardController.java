package com.example.th02201.controller;

import com.example.th02201.dto.HoaDonDTO;
import com.example.th02201.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class DashboardController {
    private final DashboardService dashboardService;

    /**
     * Lấy dữ liệu tổng quan cho dashboard.
     * API: GET /api/dashboard?fromDate=YYYY-MM-DD&toDate=YYYY-MM-DD
     * @param fromDate Ngày bắt đầu
     * @param toDate Ngày kết thúc
     * @return ResponseEntity chứa Map dữ liệu dashboard
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboardData(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc"));
        }

        Map<String, Object> dashboardData = dashboardService.getDashboardData(fromDate, toDate);
        return ResponseEntity.ok(dashboardData);
    }

    /**
     * Lấy danh sách hóa đơn.
     * API: GET /api/dashboard/invoices?fromDate=YYYY-MM-DD&toDate=YYYY-MM-DD[&statusId=INT]
     * @param fromDate Ngày bắt đầu
     * @param toDate Ngày kết thúc
     * @param statusId (TÙY CHỌN) ID trạng thái hóa đơn cần lọc (ví dụ: 21 cho "Đã hoàn thành")
     * @return ResponseEntity chứa danh sách HoaDonDTO
     */
    @GetMapping("/invoices")
    public ResponseEntity<List<HoaDonDTO>> getInvoices(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(value = "statusId", required = false) Integer statusId) {

        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body(null);
        }

        List<HoaDonDTO> invoices = dashboardService.getInvoices(fromDate, toDate, statusId);
        return ResponseEntity.ok(invoices);
    }
}
