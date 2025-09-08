package com.example.th02201.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangInfo {
    private String ten; // Khớp với khachHangHienTai.ten
    private String soDienThoai; // Khớp với khachHangHienTai.soDienThoai
}