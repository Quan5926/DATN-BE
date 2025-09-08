package com.example.th02201.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date; // Đã thay đổi từ java.time.LocalDate sang java.sql.Date

/**
 * DTO cho thông tin khách hàng.
 * Phản ánh cấu trúc `invoice.khachHang` trong Vue.js.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangDTO {
    private Integer id; // ID của khach_hang (INT IDENTITY)
    private String tenKhachHang; // Từ khach_hang.ten_khach_hang
    private String soDienThoai; // Từ khach_hang.so_dien_thoai
    private String email; // Từ tai_khoan.email qua khach_hang.id_tai_khoan
    private Date ngaySinh; // Đã thay đổi từ LocalDate sang java.sql.Date để khớp với Entity
}
