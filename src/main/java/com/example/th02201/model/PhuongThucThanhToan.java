package com.example.th02201.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "phuong_thuc_thanh_toan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhuongThucThanhToan {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "ten_phuong_thuc", nullable = false)
    private String tenPhuongThuc;

    @Column(name = "mo_ta")
    private String moTa;
}