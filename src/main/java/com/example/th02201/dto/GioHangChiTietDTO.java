package com.example.th02201.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GioHangChiTietDTO {

    private Long id;

    @NotNull
    private Integer soLuong;

    @NotNull
    @Digits(integer = 20, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "94.08")
    private BigDecimal giaBanHienTai;

    private LocalDateTime ngayThemVao;

    @NotNull
    private UUID idGioHang; // Cập nhật để phù hợp với Entity GioHang


    private UUID idChiTietSp; // Cập nhật để phù hợp với Entity ChiTietSanPham

    // Các trường mới để nhận mã từ client, giúp việc tạo mới dễ dàng hơn
    private String maPhienGioHang;
    private String maCtsp;

    // Thêm các trường mới để hiển thị thông tin sản phẩm
    private Long sanPham;
    private String tenSanPham;
    private String moTaSanPham;
    private BigDecimal giaBan; // Thêm trường này để nhận giá bán từ ChiTietSanPham
    private String urlAnhDaiDien;
    private String tenKichCo;
    private Long kichCo; // ID của kích cỡ
    private String maMauSac;
    private String tenMauSac;
    private Integer idKhachHang;
    // Thêm trường mới để hiển thị số lượng tồn kho
    private Integer soLuongTonKho;
    public GioHangChiTietDTO(Long id, Integer soLuong, BigDecimal giaBanHienTai, LocalDateTime ngayThemVao, UUID idGioHang, UUID idChiTietSp, String maPhienGioHang, String maCtsp,
                             Long sanPham, String tenSanPham, String moTaSanPham, BigDecimal giaBan, String urlAnhDaiDien, Long kichCo, String tenKichCo, Integer idKhachHang, String maMauSac, String tenMauSac,Integer soLuongTonKho) {
        this.id = id;
        this.soLuong = soLuong;
        this.giaBanHienTai = giaBanHienTai;
        this.ngayThemVao = ngayThemVao;
        this.idGioHang = idGioHang;
        this.idChiTietSp = idChiTietSp;
        this.maPhienGioHang = maPhienGioHang;
        this.maCtsp = maCtsp;
        this.sanPham = sanPham;
        this.tenSanPham = tenSanPham;
        this.moTaSanPham = moTaSanPham;
        this.giaBan = giaBan;
        this.urlAnhDaiDien = urlAnhDaiDien;
        this.kichCo = kichCo;
        this.tenKichCo = tenKichCo;
        this.maMauSac = maMauSac;
        this.tenMauSac = tenMauSac;
        this.idKhachHang = idKhachHang;
        this.soLuongTonKho = soLuongTonKho;
    }

    /**
     * Phương thức getter để tính toán tổng tiền cho một mặt hàng trong giỏ hàng.
     * Tổng tiền được tính bằng cách nhân số lượng với giá bán hiện tại.
     * @return BigDecimal tổng tiền, trả về BigDecimal.ZERO nếu số lượng hoặc giá bán hiện tại là null.
     */
    public BigDecimal getThanhTien() {
        if (this.soLuong != null && this.giaBanHienTai != null) {
            return this.giaBanHienTai.multiply(new BigDecimal(this.soLuong));
        }
        return BigDecimal.ZERO;
    }
}
