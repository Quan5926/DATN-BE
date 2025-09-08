package com.example.th02201.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO cho hóa đơn.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonDTO {
    private UUID id; // ID của hoa_don (UNIQUEIDENTIFIER)
    private String maHoaDon; // Từ hoa_don.ma_hoa_don
    private LocalDateTime ngayTao; // Từ hoa_don.ngay_tao (DATETIMEOFFSET)
    private BigDecimal tongTienThanhToan; // Từ hoa_don.tong_tien_thanh_toan
    private BigDecimal phiVanChuyen; // Từ hoa_don.phi_van_chuyen
    private BigDecimal giaTriGiam; // Tính từ phieu_giam_gia.gia_tri_giam qua ma_giam_gia
    private LocalDateTime ngayThanhToan; // Từ hoa_don.ngay_thanh_toan
    private String loaiThanhToan; // Từ phuong_thuc_thanh_toan.ten_phuong_thuc qua chi_tiet_thanh_toan
    private BigDecimal soTienThanhToanFinal; // Từ chi_tiet_thanh_toan.so_tien_thanh_toan
    private Integer khachHangId; // Từ hoa_don.id_khach_hang
    private String tenKhachHang; // Từ khach_hang.ten_khach_hang
    private String soDienThoaiKhachHang; // Từ khach_hang.so_dien_thoai
    private Integer trangThaiId; // Từ hoa_don.id_trang_thai
    private String tenTrangThai; // Từ trang_thai.ten_trang_thai
}

