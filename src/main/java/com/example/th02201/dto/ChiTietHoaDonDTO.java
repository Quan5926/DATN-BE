package com.example.th02201.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO cho chi tiết sản phẩm trong hóa đơn.
 * Phản ánh cấu trúc `products` trong Vue.js.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDonDTO {
    private Integer id; // Đã sửa từ Long sang Integer để khớp với ID của ChiTietHoaDon entity
    private Integer productId; // ID của san_pham (INT IDENTITY)
    private String tenSanPham; // Từ san_pham.ten_san_pham
    private Integer soLuong; // Từ hoa_don_chi_tiet.so_luong
    private BigDecimal donGia; // Từ hoa_don_chi_tiet.don_gia
    private BigDecimal thanhTien; // Từ hoa_don_chi_tiet.thanh_tien (computed)
    private String chatLieu; // Từ chat_lieu.ten_chat_lieu
    private String size; // Từ kich_co.ten_kich_co
    private String mauSac; // Từ mau_sac.ten_mau_sac
}

