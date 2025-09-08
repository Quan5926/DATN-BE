package com.example.th02201.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GioHangDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String maPhienGioHang;

    private LocalDateTime ngayTao;

    private LocalDateTime ngayCapNhat;

    private Integer idTrangThai; // Cập nhật để phù hợp với Entity TrangThai

    private Integer idKhachHang; // Cập nhật để phù hợp với Entity KhachHang

    // Thêm trường mới để nhận danh sách chi tiết giỏ hàng
    private List<GioHangChiTietDTO> gioHangChiTiets;
}
