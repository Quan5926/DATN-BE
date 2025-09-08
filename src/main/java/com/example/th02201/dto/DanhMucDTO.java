package com.example.th02201.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DanhMucDTO {
    private Long id;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 100, message = "Tên danh mục không được vượt quá 100 ký tự")
    private String tenDanhMuc;

    @NotBlank(message = "Mã danh mục không được để trống")
    @Size(max = 20, message = "Mã danh mục không được vượt quá 20 ký tự")
    private String maDanhMuc;

    // Trường mới để lưu tổng số lượng sản phẩm chi tiết có kích cỡ này
    private Integer soLuongSanPham;

    // Thêm các trường này
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayTao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayCapNhat;
}