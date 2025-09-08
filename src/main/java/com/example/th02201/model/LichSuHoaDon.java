package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lich_su_hoa_don")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LichSuHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id; // Sửa: từ UUID sang Integer để khớp với bảng

    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien_thuc_hien")
    private NhanVien nhanVienThucHien;

    @Column(name = "hanh_dong")
    private String hanhDong;

    @Column(name = "mo_ta_hanh_dong")
    private String moTaHanhDong;

    @Column(name = "thoi_gian")
    private LocalDateTime thoiGian;
}