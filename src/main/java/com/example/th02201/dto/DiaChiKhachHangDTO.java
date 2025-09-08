package com.example.th02201.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DiaChiKhachHangDTO {

        private Integer id; // Sửa kiểu dữ liệu từ Integer sang Long

        @NotNull
        @Size(max = 100)
        private String tenNguoiNhan;

        @NotNull
        @Size(max = 20)
        private String soDienThoaiNguoiNhan;

        @NotNull
        @Size(max = 255)
        private String soNhaTenDuong;

        private String ghiChu;

        private Boolean laDiaChiMacDinh;

        private LocalDateTime ngayTao;

        private LocalDateTime ngayCapNhat;


        private Integer khachHang;

        @NotNull
        private Integer phuongXa;

        private String tenPhuongXa;


        @NotNull
        private Integer quanHuyen;

        private String tenQuanHuyen;


        @Size(max = 10)
        private String maHuyen;

        private Integer tinhThanh;

        @NotNull
        @Size(max = 100)
        private String tenTinhThanh;

        @Size(max = 10)
        private String maVung;
}
