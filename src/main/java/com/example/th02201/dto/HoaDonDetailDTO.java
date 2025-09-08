package com.example.th02201.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonDetailDTO {
    private UUID id;
    private String maHoaDon;
    private KhachHangDTO khachHang;
    private String maVanDon;
    private LocalDateTime ngayTao;
    private BigDecimal tongTienSanPham; // Thêm trường này
    private BigDecimal tongTienThanhToan;
    private BigDecimal phiVanChuyen;
    private BigDecimal giaTriGiam;
    private TrangThaiDTO trangThai;
    private List<LichSuThanhToanDTO> paymentHistory;
    private List<ChiTietHoaDonDTO> products;
    private List<LichSuTrangThaiDTO> statusHistory;
    private List<SanPhamTraLaiDTO> returnedProducts;
}