package com.example.th02201.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "hoa_don_chi_tiet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JsonBackReference("hoaDon-chiTietHoaDon")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hoa_don", referencedColumnName = "id", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chi_tiet_sp", referencedColumnName = "id", nullable = false)
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong; // ✨ Đã sửa từ BigDecimal thành Integer để khớp với DB

    @Column(name = "don_gia", nullable = false, precision = 20, scale = 2)
    private BigDecimal donGia;

    // ✨ Đã thêm insertable = false, updatable = false để chỉ ra đây là cột tính toán
    @Column(name = "thanh_tien", nullable = false, precision = 20, scale = 2, insertable = false, updatable = false)
    private BigDecimal thanhTien;
}