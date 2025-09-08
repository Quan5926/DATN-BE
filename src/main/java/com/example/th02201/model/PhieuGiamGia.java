package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "phieu_giam_gia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ma_phieu_giam_gia")
    private String maPhieuGiamGia;

    @Column(name = "ten_phieu_giam_gia")
    private String tenPhieuGiamGia;

    @Column(name = "gia_tri_giam")
    private BigDecimal giaTriGiam;

    @Column(name = "so_tien_giam_toi_da")
    private BigDecimal soTienGiamToiDa;

    @Column(name = "hoa_don_toi_thieu")
    private BigDecimal hoaDonToiThieu;

    @Column(name = "loai_giam_gia")
    private String loaiGiamGia;

    @Column(name = "loai_ap_dung")
    private String loaiApDung;

    @Column(name = "trang_thai_phieu")
    private String trangThaiPhieu;

    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien_tao")
    private NhanVien nhanVienTao;
}