package com.example.th02201.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietSanPhamDTO {

    private UUID id; // ID của ChiTietSanPham (có thể null khi thêm mới)

    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng tồn kho phải lớn hơn hoặc bằng 0")
    private Integer soLuongTonKho;

    @Size(max = 500, message = "Mô tả chi tiết không được vượt quá 500 ký tự")
    private String moTaChiTiet;

    @NotNull(message = "Giá nhập không được để trống")
    @DecimalMin(value = "0.0", message = "Giá nhập phải lớn hơn hoặc bằng 0")
    private BigDecimal giaNhap;

    @NotNull(message = "Giá bán không được để trống")
    @DecimalMin(value = "0.0", message = "Giá bán phải lớn hơn hoặc bằng 0")
    private BigDecimal giaBan;

    @NotBlank(message = "Mã chi tiết sản phẩm không được để trống")
    @Size(max = 50, message = "Mã chi tiết sản phẩm không được vượt quá 50 ký tự")
    private String maCtsp;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayNhap;

    @NotNull(message = "ID sản phẩm không được để trống")
    private Long sanPham; // ID của SanPham cha

    private String tenSanPham; // Tên sản phẩm để hiển thị
    private String urlAnhDaiDien; // URL ảnh đại diện của chi tiết sản phẩm (từ SanPham hoặc ảnh đại diện của CT_SP)

    private String tenThuongHieu; // Tên thương hiệu để hiển thị
    private Long thuongHieu; // ID của thương hiệu

    private String tenDanhMuc; // Tên danh mục để hiển thị
    private Long danhMuc; // ID của danh mục

    private String tenChatLieu; // Tên chất liệu để hiển thị
    private Long chatLieu; // ID của chất liệu

    private String tenMauSac; // Tên màu sắc để hiển thị
    private Long mauSac; // ID của màu sắc

    private String tenKichCo; // Tên kích cỡ để hiển thị
    private Long kichCo; // ID của kích cỡ

    @NotNull(message = "Trạng thái riêng không được để trống")
    private Integer idTrangThaiRieng; // ID của TrangThai riêng của chi tiết sản phẩm
    private String tenTrangThaiRieng; // Tên trạng thái riêng để hiển thị

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayTao;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ngayCapNhat;

    // NEW: Thêm danh sách ảnh liên quan đến chi tiết sản phẩm
    @Schema(description = "Danh sách ảnh của chi tiết sản phẩm")
    private List<AnhSanPhamDTO> images;


    // Constructor for JPQL query (giữ nguyên nếu bạn vẫn dùng)
    public ChiTietSanPhamDTO(UUID id, Integer soLuongTonKho, String moTaChiTiet, BigDecimal giaNhap, BigDecimal giaBan,
                             String maCtsp, LocalDateTime ngayNhap, Integer idTrangThaiRieng, // Thêm idTrangThaiRieng
                             LocalDateTime ngayTao, LocalDateTime ngayCapNhat,
                             Long chatLieu, String tenChatLieu, Long mauSac, String tenMauSac, Long kichCo, String tenKichCo,
                             Long sanPham, String tenSanPham, String urlAnhDaiDien, Long thuongHieu, String tenThuongHieu, Long danhMuc, String tenDanhMuc) {
        this.id = id;
        this.soLuongTonKho = soLuongTonKho;
        this.moTaChiTiet = moTaChiTiet;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.maCtsp = maCtsp;
        this.ngayNhap = ngayNhap;
        this.idTrangThaiRieng = idTrangThaiRieng; // Gán idTrangThaiRieng
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
        this.chatLieu = chatLieu;
        this.tenChatLieu = tenChatLieu;
        this.mauSac = mauSac;
        this.tenMauSac = tenMauSac;
        this.kichCo = kichCo;
        this.tenKichCo = tenKichCo;
        this.sanPham = sanPham;
        this.tenSanPham = tenSanPham;
        this.urlAnhDaiDien = urlAnhDaiDien;
        this.thuongHieu = thuongHieu;
        this.tenThuongHieu = tenThuongHieu;
        this.danhMuc = danhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }
}