package com.example.th02201.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO để nhận dữ liệu khi cập nhật sản phẩm.
 * Chứa các thông tin cần thiết để update một sản phẩm.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SanPhamUpdateDTO {
    // ID is needed to identify which product to update
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String tenSanPham;

    // Các trường cập nhật thêm theo yêu cầu
    @Size(max = 50, message = "Mã sản phẩm không được vượt quá 50 ký tự")
    private String maSanPham;

    private String moTaSanPham;

    @Size(max = 100, message = "Quốc gia sản xuất không được vượt quá 100 ký tự")
    private String quocGiaSanXuat;

    private String urlAnhDaiDien;

    // ID của các đối tượng liên quan để cập nhật
    private Long danhMucId;
    private Long thuongHieuId;
    private Integer trangThaiId;
}