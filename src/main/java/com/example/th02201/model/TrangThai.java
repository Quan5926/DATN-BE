package com.example.th02201.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "trang_thai")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrangThai {
    @Id // Đánh dấu trường này là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID
    @Column(name = "id") // Chỉ định tên cột trong bảng
    private Integer id;

    @Column(name = "ten_trang_thai", nullable = false, unique = true, length = 50)
    private String tenTrangThai; // ten_trang_thai

    @Column(name = "mo_ta", length = 255)
    private String moTa; // mo_ta

    @JsonIgnore // Thêm chú thích này
    @OneToMany(mappedBy = "trangThai",cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<SanPham> trangThaiSanPhams;

    @JsonIgnore // Thêm chú thích này
    @OneToMany(mappedBy = "trangThaiRieng",cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<ChiTietSanPham> trangThaiRiengChiTietSanPhams;

    // Ghi đè phương thức toString để dễ dàng debug
    @Override
    public String toString() {
        return "TrangThai{" +
                "id=" + id +
                ", tenTrangThai='" + tenTrangThai + '\'' +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}