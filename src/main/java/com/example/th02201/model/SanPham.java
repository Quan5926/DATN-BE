package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "san_pham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_san_pham", nullable = false, length = 255, unique = true) // NEW: Thêm unique = true
    private String tenSanPham;

    @Column(name = "ma_san_pham", nullable = false, length = 50, unique = true, updatable = false, insertable = false)
    private String maSanPham;

    @Column(name = "mo_ta_san_pham", columnDefinition = "NVARCHAR(MAX)")
    private String moTaSanPham;

    @Column(name = "url_anh_dai_dien", columnDefinition = "NVARCHAR(MAX)")
    private String urlAnhDaiDien;

    @Column(name = "quoc_gia_san_xuat", length = 100)
    private String quocGiaSanXuat;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_danh_muc", nullable = false)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private DanhMuc danhMuc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_thuong_hieu", nullable = false)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private ThuongHieu thuongHieu;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private Set<ChiTietSanPham> chiTietSanPhams;

    // Thay thế trường id_trang_thai cũ bằng mối quan hệ ManyToOne
    @ManyToOne(fetch = FetchType.LAZY) // Đảm bảo fetch type là LAZY
    @JoinColumn(name = "id_trang_thai", nullable = false) // Đảm bảo khớp với tên cột FK trong DB
    private TrangThai trangThai; // Tên thuộc tính này phải khớp với mappedBy trong TrangThai

//    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<DanhGiaSanPham> danhGiaSanPhams;

    @PrePersist
    protected void onCreate() {
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}
