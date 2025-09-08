package com.example.th02201.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnhSanPhamDTO {

    private Long id;

    @NotNull(message = "ID chi tiết sản phẩm không được để trống")
    private UUID chiTietSpId;

    // Bỏ @NotBlank vì urlAnh sẽ được Backend tạo ra sau khi upload file
    private String urlAnh;

    @NotNull(message = "Trạng thái ảnh đại diện không được để trống")
    private Boolean laAnhDaiDien;

    @Schema(description = "Ngày tạo ảnh")
    private LocalDateTime ngayTao;

    @Schema(description = "Ngày cập nhật ảnh")
    private LocalDateTime ngayCapNhat;
}


