package com.example.th02201.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ThuongHieuDTO {
    private Long id;

    @NotBlank(message = "Tên thương hiệu không được để trống")
    @Size(max = 100, message = "Tên thương hiệu không được vượt quá 100 ký tự")
    private String tenThuongHieu;

    @NotBlank(message = "Mã thương hiệu không được để trống")
    @Size(max = 20, message = "Mã thương hiệu không được vượt quá 20 ký tự")
    private String maThuongHieu;


    // New field to store the total quantity of products for this brand
    private Integer soLuongSanPham;

    // Thêm các trường này
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayTao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayCapNhat;

    // Thêm trường này để chứa danh sách các ChiTietSanPhamDTO
    private Set<ChiTietSanPhamDTO> chiTietSanPhams;

}