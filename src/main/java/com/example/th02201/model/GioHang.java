package com.example.th02201.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "gio_hang") // Chỉ định tên bảng trong cơ sở dữ liệu
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GioHang {

    @Id
    @GeneratedValue // Khi sử dụng UuidGenerator, @GeneratedValue không cần strategy
    @UuidGenerator // Tự động sinh UUID
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uniqueidentifier") // Thêm name
    private UUID id;

    @Column(name = "ma_phien_gio_hang", length = 255, unique = true) // Thêm name, kiểm tra độ dài, thêm unique
    private String maPhienGioHang;

    @Column(name = "ngay_tao", nullable = false, updatable = false) // Bỏ columnDefinition "datetime2"
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat", nullable = false) // Bỏ columnDefinition "datetime2"
    private LocalDateTime ngayCapNhat;

    // Thiết lập mối quan hệ với bảng TrangThai
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trang_thai", nullable = false)
    private TrangThai trangThai;

    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GioHangChiTiet> gioHangChiTiets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang") // Sửa tên cột join theo DB là id_khach_hang
    private KhachHang khachHang;

    // Ghi đè phương thức toString để tránh lỗi stack overflow với các mối quan hệ
    @Override
    public String toString() {
        return "GioHang{" +
                "id=" + id +
                ", maPhienGioHang='" + maPhienGioHang + '\'' +
                ", ngayTao=" + ngayTao +
                ", ngayCapNhat=" + ngayCapNhat +
                '}';
    }

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