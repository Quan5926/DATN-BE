package com.example.th02201.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherValidationResponse {
    private String message;
    private BigDecimal discountAmount;
    private String loaiGiamGia;
    private BigDecimal giaTriGiam;
}