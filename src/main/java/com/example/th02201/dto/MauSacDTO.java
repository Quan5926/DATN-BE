package com.example.th02201.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class MauSacDTO {

    private Long id; // Có thể có nếu dùng cho các thao tác GET/PUT, không cần khi POST

    @NotBlank(message = "Tên màu sắc không được để trống")
    @Size(max = 100, message = "Tên màu sắc không được vượt quá 100 ký tự")
    private String tenMauSac;

    @NotBlank(message = "Mã màu sắc không được để trống")
    @Size(max = 20, message = "Mã màu sắc không được vượt quá 20 ký tự")
    private String maMauSac;

    @NotBlank(message = "Mã HEX không được để trống")
    @Size(max = 7, message = "Mã HEX không hợp lệ (ví dụ: #RRGGBB)")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Mã HEX phải có định dạng #RRGGBB")
    private String hex;


    // Trường mới để lưu tổng số lượng sản phẩm chi tiết có kích cỡ này
    private Integer soLuongSanPham;
    // Không cần các trường khác như rgb, hsl, cmyk, hsv ở đây
    // vì frontend không gửi chúng và database cũng không lưu chúng nữa.


    // Thêm các trường này
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayTao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayCapNhat;
}
