package com.example.th02201.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhieuGiamGiaResponseDTO {
    private UUID id;
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;
    private String loaiGiamGia;
    private BigDecimal giaTriGiam;
    private BigDecimal hoaDonToiThieu;
    private BigDecimal soTienGiamToiDa;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;

    // Thay thế trường cũ bằng trường mới để hiển thị tên trạng thái
    private String tenTrangThai;

    private String loaiApDung;

    // Nhân viên tạo
    private Integer idNhanVienTao;
    private String tenNhanVienTao;

    // Thông tin hệ thống
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}
