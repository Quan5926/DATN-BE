package com.example.th02201.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp Entity cho bảng 'tinh_thanh'.
 * Đại diện cho tỉnh/thành.
 */
@Entity
@Table(name = "tinh_thanh")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TinhThanh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ten_tinh_thanh", nullable = false, unique = true, length = 100)
    private String tenTinhThanh;

    @Column(name = "ma_vung", length = 50)
    private String maVung;
}
