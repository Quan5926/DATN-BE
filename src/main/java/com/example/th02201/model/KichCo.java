package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "kich_co")
public class KichCo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_kich_co", nullable = false)
    private String tenKichCo;

    @Column(name = "ma_kich_co", nullable = false)
    private String maKichCo;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Transient
    private Integer soLuongSanPham;

    @OneToMany(mappedBy = "kichCo", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private Set<ChiTietSanPham> chiTietSanPhams; // Assuming KichCo has a collection of ChiTiet
}