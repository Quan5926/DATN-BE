package com.example.th02201.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ma_giam_gia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "phieuGiamGia", "khachHangDuocCap"})
public class MaGiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phieu_giam_gia", nullable = false)
    @JsonIgnoreProperties({"maGiamGias"}) // nếu PhieuGiamGia có list<MaGiamGia>
    private PhieuGiamGia phieuGiamGia;

    @Column(name = "ma_code", nullable = false, unique = true)
    private String maCode;

    @Column(name = "da_su_dung", nullable = false)
    private Boolean daSuDung = false;

    @Column(name = "id_hoa_don_da_su_dung", unique = true)
    private UUID idHoaDonDaSuDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang_duoc_cap")
    @JsonIgnoreProperties({"maGiamGias", "hoaDons"}) // tùy field trong KhachHang
    private KhachHang khachHangDuocCap;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_su_dung")
    private LocalDateTime ngaySuDung;
}