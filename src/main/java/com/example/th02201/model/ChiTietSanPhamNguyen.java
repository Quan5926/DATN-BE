package com.example.th02201.model;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@Table(name = "chi_tiet_san_phamm")
public class ChiTietSanPhamNguyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "so_luong_ton_kho")
    private Integer soLuongTonKho;
    @Column(name = "mo_ta_chi_tiet")
    private String moTaChiTiet;
    @Column(name = "gia_nhap")
    private Double giaNhap;
    @Column(name = "gia_ban")
    private Double giaBan;
    @Column(name = "ma_ctsp")
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
    @ManyToOne
    @JoinColumn(name = "id_san_pham", referencedColumnName = "id")
    private SanPham sanPham;
//    @ManyToOne
//    @JoinColumn(name = "id_chat_lieu", referencedColumnName = "id")
//    private ChatLieu chatLieu;
//    @ManyToOne
//    @JoinColumn(name = "id_mau_sac", referencedColumnName = "id")
//    private MauSac mauSac;
//    @ManyToOne
//    @JoinColumn(name = "id_kich_co", referencedColumnName = "id")
//    private KichCo kichCo;

}

