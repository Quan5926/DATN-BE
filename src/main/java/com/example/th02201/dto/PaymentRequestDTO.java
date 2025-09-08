package com.example.th02201.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequestDTO {
    @NotNull(message = "Số tiền thanh toán không được null")
    private BigDecimal soTienThanhToan;
    @NotNull(message = "Phương thức thanh toán không được null")
    private Integer idPhuongThucThanhToan;
    private String trangThaiThanhToan;
    private String ghiChuThanhToan;
    private String maGiaoDichNganHang;
    private String nganHangThanhToan;
}