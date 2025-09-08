package com.example.th02201.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhieuGiamGiaDTO {
    public enum LoaiApDung {
        TOAN_BO,
        KH_CU_THE
    }

    public enum LoaiGiamGia {
        PHAN_TRAM,
        SO_TIEN_CO_DINH
    }

    @NotBlank(message = "Mã phiếu giảm giá không được để trống")
    private String maPhieuGiamGia;

    @NotBlank(message = "Tên phiếu giảm giá không được để trống")
    private String tenPhieuGiamGia;

    @NotNull(message = "Giá trị giảm không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá trị giảm phải lớn hơn 0")
    private BigDecimal giaTriGiam;

    @NotNull(message = "Hoá đơn tối thiểu không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Hoá đơn tối thiểu không được âm")
    private BigDecimal hoaDonToiThieu;

    @DecimalMin(value = "0.0", inclusive = true, message = "Số tiền giảm tối đa không được âm")
    private BigDecimal soTienGiamToiDa;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hiện tại hoặc tương lai")
    private LocalDateTime ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDateTime ngayKetThuc;

    @NotNull(message = "Loại giảm giá không được để trống")
    private LoaiGiamGia loaiGiamGia;

    @NotNull(message = "Loại áp dụng không được để trống")
    private LoaiApDung loaiApDung;

    private Integer trangThaiPhieuId;

    private List<Integer> customerIds;
}
