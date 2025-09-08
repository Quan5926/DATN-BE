package com.example.th02201.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamTraLaiDTO {
    private Long id; // Khớp với SanPhamTraLai
    private Integer chiTietHoaDonId; // Sửa: từ UUID sang Integer
    private String tenSanPham;
    private String chatLieu;
    private String size;
    private String mauSac;
    private Integer soLuongTra;
    private BigDecimal donGia;
    private BigDecimal soTienHoan;
    private String lyDoTra;
    private LocalDateTime ngayTra;
}