package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gio_hang_chi_tiet", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_gio_hang", "id_chi_tiet_sp"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GioHangChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "gia_ban_hien_tai", nullable = false, precision = 20, scale = 2)
    private BigDecimal giaBanHienTai;

    @Column(name = "ngay_them_vao", nullable = false, updatable = false)
    private LocalDateTime ngayThemVao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gio_hang", nullable = false)
    private GioHang gioHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chi_tiet_sp", nullable = false)
    private ChiTietSanPham chiTietSp;

    // Phương thức getter để tính toán thành tiền, không cần thuộc tính riêng
    public BigDecimal getThanhTien() {
        if (this.soLuong != null && this.giaBanHienTai != null) {
            return this.giaBanHienTai.multiply(new BigDecimal(this.soLuong));
        }
        return BigDecimal.ZERO;
    }

    @PrePersist
    protected void onCreate() {
        if (ngayThemVao == null) {
            ngayThemVao = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "GioHangChiTiet{" +
                "id=" + id +
                ", soLuong=" + soLuong +
                ", giaBanHienTai=" + giaBanHienTai +
                ", ngayThemVao=" + ngayThemVao +
                '}';
    }
}
