package com.example.th02201.respone;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ChiTietSanPhamRespone {
    private UUID id;
    private String maSanPham;
    private String tenSanPham;
    private BigDecimal giaBan;
    private Integer soLuongTonKho;
    private String tenMau;
    private String tenKichThuoc;

    // Constructor cho truy vấn JPQL với 5 tham số (nếu cần)
    public ChiTietSanPhamRespone(UUID id, String tenSanPham, String maSanPham, BigDecimal giaBan, Integer soLuongTonKho) {
        this.id = id;
        this.tenSanPham = tenSanPham;
        this.maSanPham = maSanPham;
        this.giaBan = giaBan;
        this.soLuongTonKho = soLuongTonKho;
        this.tenMau = null; // Giá trị mặc định
        this.tenKichThuoc = null; // Giá trị mặc định
    }
}