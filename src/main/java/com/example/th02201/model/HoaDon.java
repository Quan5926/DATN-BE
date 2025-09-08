package com.example.th02201.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet; // Import HashSet
import java.util.Set;    // Import Set
import java.util.UUID;

/**
 * Lớp Entity cho bảng 'hoa_don'.
 * Đại diện cho một hóa đơn trong hệ thống.
 */
@Entity
@Table(name = "hoa_don")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {
    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @Column(name = "ma_hoa_don", nullable = false, unique = true, length = 50)
    private String maHoaDon;

    @ManyToOne(fetch = FetchType.LAZY) // Hoặc FetchType.EAGER nếu bạn muốn tải ngay
    @JoinColumn(name = "id_khach_hang", referencedColumnName = "id") // Tên cột khóa ngoại trong bảng hoa_don trỏ đến khach_hang
    private KhachHangQ khachHang;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien_tao", referencedColumnName = "id")
    private NhanVien nhanVienTao; // Nhân viên tạo hóa đơn (có thể null)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dia_chi_giao_hang", referencedColumnName = "id")
    private DiaChiKhachHang diaChiGiaoHang; // Địa chỉ giao hàng (có thể null)

    @Column(name = "ma_van_don", length = 255)
    private String maVanDon;

    @Column(name = "loai_don_hang", nullable = false, length = 50)
    private String loaiDonHang; // ONLINE, OFFLINE

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Column(name = "ngay_thanh_toan")
    private LocalDateTime ngayThanhToan;

    @Column(name = "ngay_giao_hang")
    private LocalDateTime ngayGiaoHang;

    @Column(name = "ngay_nhan_hang")
    private LocalDateTime ngayNhanHang;

    @Column(name = "tong_tien_san_pham", nullable = false, precision = 20, scale = 2)
    private BigDecimal tongTienSanPham = BigDecimal.ZERO;

    @Column(name = "phi_van_chuyen", nullable = false, precision = 20, scale = 2)
    private BigDecimal phiVanChuyen = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ma_giam_gia", referencedColumnName = "id")
    private MaGiamGia maGiamGia; // Mã giảm giá áp dụng (có thể null)

    @Column(name = "tong_tien_thanh_toan", nullable = false, precision = 20, scale = 2)
    private BigDecimal tongTienThanhToan = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trang_thai", referencedColumnName = "id", nullable = true)
    private TrangThai trangThai;

    @Column(name = "ngay_cap_nhat", nullable = true)
    private LocalDateTime ngayCapNhat = LocalDateTime.now();

    @Column(name = "ghi_chu", columnDefinition = "NVARCHAR(MAX)")
    private String ghiChu;


    // Mối quan hệ OneToMany với ChiTietHoaDon
    @JsonManagedReference("hoaDon-chiTietHoaDon")
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HoaDonChiTiet> chiTietHoaDon = new HashSet<>(); // ✨ Đã đổi từ List sang Set

    // Mối quan hệ OneToMany với LichSuThanhToan
    @JsonManagedReference("hoaDon-lichSuThanhToan")
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LichSuThanhToan> lichSuThanhToan = new HashSet<>(); // ✨ Đã đổi từ List sang Set

    // Mối quan hệ OneToMany với LichSuHoaDon
    @JsonManagedReference("hoaDon-lichSuHoaDon")
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LichSuHoaDon> lichSuHoaDon = new HashSet<>(); // ✨ Đã đổi từ List sang Set

    // Mối quan hệ OneToMany với SanPhamTraLai
    @JsonManagedReference("hoaDon-sanPhamTraLai")
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SanPhamTraLai> sanPhamTraLai = new HashSet<>();
}
