
        package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "chi_tiet_san_pham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietSanPhamQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_san_pham", referencedColumnName = "id", nullable = false)
    private SanPham sanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chat_lieu", referencedColumnName = "id", nullable = false)
    private ChatLieu chatLieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mau_sac", referencedColumnName = "id", nullable = false)
    private MauSac mauSac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kich_co", referencedColumnName = "id", nullable = false)
    private KichCo size;

    @Column(name = "so_luong_ton_kho", nullable = false)
    private Integer soLuongTonKho;

    @Column(name = "mo_ta_chi_tiet", length = 500)
    private String moTaChiTiet;

    @Column(name = "gia_nhap", nullable = false)
    private Double giaNhap;

    @Column(name = "gia_ban", nullable = false)
    private Double giaBan;

    @Column(name = "ma_ctsp", nullable = false, unique = true, length = 50)
    private String mactsp;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_nhap")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayNhap;

    @Column(name = "trang_thai_san_pham_rieng")
    private String trangThaiSanPhamRieng;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayTao;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_cap_nhat")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayCapNhat;
}
