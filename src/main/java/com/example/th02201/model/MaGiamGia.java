package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ma_giam_gia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaGiamGia {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "ma_code")
    private String maCode;

    @Column(name = "da_su_dung")
    private Boolean daSuDung;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_su_dung")
    private LocalDateTime ngaySuDung;

    @ManyToOne
    @JoinColumn(name = "id_phieu_giam_gia")
    private PhieuGiamGia phieuGiamGia;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang_duoc_cap")
    private KhachHang khachHangDuocCap;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don_da_su_dung")
    private HoaDon hoaDonDaSuDung;
}