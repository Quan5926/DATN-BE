package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "san_pham_tra_lai")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamTraLai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // Sửa: từ UUID sang Long

    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don_chi_tiet")
    private HoaDonChiTiet chiTietHoaDon;

    @Column(name = "so_luong_tra")
    private Integer soLuongTra;

    @Column(name = "so_tien_hoan")
    private BigDecimal soTienHoan;

    @Column(name = "ly_do_tra")
    private String lyDoTra;

    @Column(name = "ngay_tra")
    private LocalDateTime ngayTra;
}