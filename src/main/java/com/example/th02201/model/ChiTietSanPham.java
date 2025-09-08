package com.example.th02201.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "chi_tiet_san_pham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietSanPham {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uniqueidentifier")
    private UUID id;

    @Column(name = "so_luong_ton_kho", nullable = false)
    @Min(value = 0, message = "Số lượng tồn kho phải lớn hơn hoặc bằng 0")
    private Integer soLuongTonKho;

    @Column(name = "mo_ta_chi_tiet", length = 500)
    @Size(max = 500, message = "Mô tả chi tiết không được vượt quá 500 ký tự")
    private String moTaChiTiet;

    @Column(name = "gia_nhap", nullable = false, precision = 19, scale = 2)
    private BigDecimal giaNhap;

    @Column(name = "gia_ban", nullable = false, precision = 19, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "ma_ctsp", nullable = false, length = 50, unique = true)
    @NotBlank(message = "Mã chi tiết sản phẩm không được để trống")
    @Size(max = 50, message = "Mã chi tiết sản phẩm không được vượt quá 50 ký tự")
    private String maCtsp;

    @Column(name = "ngay_nhap", nullable = false)
    private LocalDateTime ngayNhap;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_san_pham", nullable = false)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private SanPham sanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chat_lieu", nullable = false)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private ChatLieu chatLieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mau_sac", nullable = false)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private MauSac mauSac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kich_co", nullable = false)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private KichCo kichCo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trang_thai_rieng", nullable = false)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private TrangThai trangThaiRieng; // Trạng thái riêng của chi tiết sản phẩm

    @OneToMany(mappedBy = "chiTietSp", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private Set<AnhSanPham> anhSanPhams;

    // @OneToMany(mappedBy = "chiTietSp", cascade = CascadeType.ALL, orphanRemoval = true)
    // @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    // private Set<HoaDonChiTiet> hoaDonChiTiets;

    // @OneToMany(mappedBy = "chiTietSp", cascade = CascadeType.ALL, orphanRemoval = true)
    // @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    // private Set<GioHangChiTiet> gioHangChiTiets;

    @PrePersist
    protected void onCreate() {
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
        this.ngayNhap = LocalDateTime.now();
        // Không cần set trạng thái mặc định ở đây nữa, sẽ được xử lý ở service
        // updateTrangThaiBasedOnSoLuong(); // Logic này sẽ được gọi sau khi trạng thái được thiết lập từ service
    }

    @PreUpdate
    protected void onUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
        // updateTrangThaiBasedOnSoLuong(); // Logic này sẽ được gọi sau khi trạng thái được thiết lập từ service
    }

    // Phương thức này sẽ được gọi tự động khi soLuongTonKho thay đổi
    // Đảm bảo trạng thái "het_hang" được cập nhật tự động
    // Logic này sẽ được di chuyển hoặc gọi từ service
    // private void updateTrangThaiBasedOnSoLuong() {
    //     if (this.soLuongTonKho != null && this.soLuongTonKho == 0) {
    //         this.trangThaiSanPhamRieng = "het_hang";
    //     } else if (this.soLuongTonKho != null && this.soLuongTonKho > 0 && this.trangThaiSanPhamRieng.equals("het_hang")) {
    //         this.trangThaiSanPhamRieng = "dang_kinh_doanh";
    //     }
    // }
}
