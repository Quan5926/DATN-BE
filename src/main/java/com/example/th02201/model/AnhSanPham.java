package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "anh_san_pham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnhSanPham {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chi_tiet_sp", nullable = false)
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private ChiTietSanPham chiTietSp;


    @Column(name = "url_anh", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String urlAnh;

    @Column(name = "la_anh_dai_dien", nullable = false)
    private Boolean laAnhDaiDien;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat;

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
