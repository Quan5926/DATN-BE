package com.example.th02201.respone;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SanPhamRespone {
    private Long id;
    private String tenSanPham;
    private String maSanPham;
    private String moTaSanPham;
    private BigDecimal giaBan;
    private Long soLuongTonKho;
}