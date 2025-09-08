package com.example.th02201.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Lớp Entity cho bảng 'dia_chi_khach_hang'.
 * Đại diện cho địa chỉ của khách hàng.
 */
@Entity
@Table(name = "dia_chi_khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaChiKhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang", referencedColumnName = "id", nullable = false)
    private KhachHang khachHang;

    @Column(name = "ten_nguoi_nhan", nullable = false, length = 100)
    private String tenNguoiNhan;

    @Column(name = "so_dien_thoai_nguoi_nhan", nullable = false, length = 20)
    private String soDienThoaiNguoiNhan;

    @Column(name = "so_nha_ten_duong", nullable = false, length = 255)
    private String soNhaTenDuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phuong_xa", referencedColumnName = "id", nullable = false)
    private PhuongXa phuongXa;

    @Column(name = "ghi_chu", columnDefinition = "NVARCHAR(MAX)")
    private String ghiChu;

    @Column(name = "la_dia_chi_mac_dinh", nullable = false)
    private Boolean laDiaChiMacDinh = false;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat = LocalDateTime.now();
}
