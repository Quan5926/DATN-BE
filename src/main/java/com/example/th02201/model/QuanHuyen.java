package com.example.th02201.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp Entity cho bảng 'quan_huyen'.
 * Đại diện cho quận/huyện.
 */
@Entity
@Table(name = "quan_huyen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuanHuyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tinh_thanh", referencedColumnName = "id", nullable = false)
    private TinhThanh tinhThanh;

    @Column(name = "ten_quan_huyen", nullable = false, length = 100)
    private String tenQuanHuyen;

    @Column(name = "ma_huyen", length = 50)
    private String maHuyen;
}
