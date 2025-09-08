package com.example.th02201.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonItemDTO {
    private Integer id; // ID sản phẩm (ID của ChiTietSanPham)
    private String maSanPham;
    private String tenSanPham;
    private BigDecimal giaBan;
    private Integer soLuong;
}