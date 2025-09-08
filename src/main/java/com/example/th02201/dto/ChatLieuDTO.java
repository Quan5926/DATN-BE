package com.example.th02201.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatLieuDTO {
    private Long id;

    @NotBlank(message = "Tên chất liệu không được để trống")
    @Size(max = 100, message = "Tên chất liệu không được vượt quá 100 ký tự")
    private String tenChatLieu;

    @NotBlank(message = "Mã chất liệu không được để trống")
    @Size(max = 10, message = "Mã chất liệu không được vượt quá 10 ký tự")
    private String maChatLieu;

    // New field to store the total quantity of products using this material
    private Integer soLuongSanPham;

    // Thêm các trường này
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayTao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayCapNhat;
}
