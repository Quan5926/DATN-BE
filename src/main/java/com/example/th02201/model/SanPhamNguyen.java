package com.example.th02201.model;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(name = "san_pham")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class SanPhamNguyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ten_san_pham")
    private String tenSanPham;
    @Column(name = "ma_san_pham")
    private String maSanPham;
    @Column(name = "mo_ta_san_pham")
    private String moTaSanPham;
    @Column(name = "url_anh_dai_dien")
    private String urlAnhDaiDien;
    @Column(name = "quoc_gia_san_xuat")
    private String quocGiaSanXuat;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayTao;
    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_cap_nhat")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayCapNhat;
    @ManyToOne
    @JoinColumn(name = "id_danh_muc", referencedColumnName = "id")
    private DanhMuc danhMuc;
    @ManyToOne
    @JoinColumn(name = "id_thuong_hieu", referencedColumnName = "id")
    private ThuongHieu thuongHieu;
}

