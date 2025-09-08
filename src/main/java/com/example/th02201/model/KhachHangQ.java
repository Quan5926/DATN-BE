package com.example.th02201.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Lớp Entity cho bảng 'khach_hang'.
 * Đại diện cho thông tin khách hàng.
 */
@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangQ  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tai_khoan", referencedColumnName = "id")
    private TaiKhoan taiKhoan;

    @Column(name = "ten_khach_hang", nullable = false, length = 100)
    private String tenKhachHang;

    @Column(name = "so_dien_thoai", nullable = false, unique = true, length = 20)
    private String soDienThoai;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Column(name = "ngay_sinh")
    private java.sql.Date ngaySinh;

    @Column(name = "ma_khach_hang", nullable = false, unique = true, length = 50)
    private String maKhachHang;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat = LocalDateTime.now();
}
