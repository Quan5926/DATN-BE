package com.example.th02201.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Lớp Entity cho bảng 'nhan_vien'.
 * Đại diện cho nhân viên.
 */
@Entity
@Table(name = "nhan_vien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NhanVienQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tai_khoan", referencedColumnName = "id", unique = true)
    private TaiKhoan taiKhoan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chuc_vu", referencedColumnName = "id", nullable = false)
    private ChucVu chucVu;

    @Column(name = "ten_nhan_vien", nullable = false, length = 100)
    private String tenNhanVien;

    @Column(name = "ngay_sinh")
    private java.sql.Date ngaySinh;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "ma_nhan_vien", nullable = false, unique = true, length = 50)
    private String maNhanVien;

    @Column(name = "cccd", unique = true, length = 20)
    private String cccd;

    @Column(name = "dia_chi_so_nha_ten_duong", length = 255)
    private String diaChiSoNhaTenDuong;

    @Column(name = "dia_chi_phuong_xa", length = 100)
    private String diaChiPhuongXa;

    @Column(name = "dia_chi_quan_huyen", length = 100)
    private String diaChiQuanHuyen;

    @Column(name = "dia_chi_tinh_thanh", length = 100)
    private String diaChiTinhThanh;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat = LocalDateTime.now();

}
