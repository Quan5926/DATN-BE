package com.example.th02201.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonRequestDTO {
    private String id; // Khớp với tabActive (ví dụ: "HD000001")
    private KhachHangInfo khachHang;
    private BigDecimal tongTienHang;
    private BigDecimal giamGia;
    private BigDecimal khachCanTra;
    private BigDecimal khachThanhToan;
    private BigDecimal tienThua;
    private String phuongThucThanhToan;
    private List<HoaDonItemDTO> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KhachHangInfo {
        private String ten;
        private String soDienThoai;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HoaDonItemDTO {
        private UUID id; // ID của ChiTietSanPham
        private String maSanPham;
        private String tenSanPham;
        private double giaBan;
        private int soLuong;
    }
}