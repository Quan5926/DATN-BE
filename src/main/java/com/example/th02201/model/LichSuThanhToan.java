package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chi_tiet_thanh_toan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LichSuThanhToan {
    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hoa_don", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phuong_thuc_thanh_toan", nullable = false)
    private PhuongThucThanhToan phuongThucThanhToan;

    @Column(name = "so_tien_thanh_toan", nullable = false)
    private BigDecimal soTienThanhToan;

    @Column(name = "thoi_gian_thanh_toan")
    private LocalDateTime thoiGianThanhToan;

    @Column(name = "trang_thai_thanh_toan") // Sửa từ id_trang_thai thành trang_thai_thanh_toan
    private String trangThaiThanhToan;

    @Column(name = "ma_giao_dich_ngan_hang")
    private String maGiaoDichNganHang;

    @Column(name = "ngan_hang_thanh_toan")
    private String nganHangThanhToan;

    @Column(name = "ghi_chu_thanh_toan")
    private String ghiChuThanhToan;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    // Getters và Setters
}