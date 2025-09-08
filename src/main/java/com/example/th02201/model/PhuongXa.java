package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "phuong_xa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhuongXa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quan_huyen", referencedColumnName = "id", nullable = false)
    private QuanHuyen quanHuyen;

    @Column(name = "ten_phuong_xa", nullable = false, length = 100)
    private String tenPhuongXa;

    @Column(name = "ma_xa", length = 50)
    private String maXa;
}
