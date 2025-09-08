package com.example.th02201.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "phieu_giam_gia")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PhieuGiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "ma_phieu_giam_gia", nullable = false, unique = true, length = 100)
    private String maPhieuGiamGia;

    @Column(name = "ten_phieu_giam_gia", nullable = false, length = 100)
    private String tenPhieuGiamGia;

    @Column(name = "loai_giam_gia", nullable = false, length = 50)
    private String loaiGiamGia;

    @Column(name = "gia_tri_giam", nullable = false, precision = 10, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "hoa_don_toi_thieu", precision = 10, scale = 2)
    private BigDecimal hoaDonToiThieu;

    @Column(name = "so_tien_giam_toi_da", precision = 10, scale = 2)
    private BigDecimal soTienGiamToiDa;

    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_ket_thuc", nullable = false)
    private LocalDateTime ngayKetThuc;

    @Column(name = "loai_ap_dung", nullable = false, length = 50)
    private String loaiApDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien_tao", referencedColumnName = "id")
    @JsonIgnore
    private NhanVien nhanVienTao;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trang_thai", referencedColumnName = "id")
    private TrangThai trangThai;

    // Getter/Setter cho trạng thái
    public TrangThai getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThai trangThai) {
        this.trangThai = trangThai;
    }



    // ====== Logic tự động cập nhật trạng thái ======
    private int tinhTrangThaiId() {
        LocalDateTime now = LocalDateTime.now();

        if (ngayBatDau.isAfter(now)) {
            return 14; // Chưa diễn ra
        } else if (ngayKetThuc.isBefore(now)) {
            return 16; // Đã kết thúc
        } else {
            return 15; // Đang diễn ra
        }
    }

    // Sửa đổi từ private thành public để có thể truy cập từ MaGiamGiaService
    public void capNhatTrangThai() {
        int idTrangThai = tinhTrangThaiId();
        if (this.trangThai == null) {
            this.trangThai = new TrangThai();
        }
        this.trangThai.setId(idTrangThai);
    }

    // ====== Lifecycle Hooks ======
    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = LocalDateTime.now();
        }
        if (ngayCapNhat == null) {
            ngayCapNhat = LocalDateTime.now();
        }
        capNhatTrangThai(); // Tự động set trạng thái khi tạo
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
        capNhatTrangThai(); // Tự động update trạng thái
    }
}
