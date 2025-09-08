package com.example.th02201.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LichSuThanhToanDTO {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("ngayThanhToan")
    private LocalDateTime ngayThanhToan;

    @JsonProperty("soTienThanhToan")
    private BigDecimal soTienThanhToan;

    @JsonProperty("tenPhuongThuc")
    private String tenPhuongThuc;

    @JsonProperty("ghiChuThanhToan")
    private String ghiChuThanhToan;

    @JsonProperty("trangThai")
    private String trangThai;

    // Constructor mới để khớp với cách gọi trong HoaDonService
    public LichSuThanhToanDTO(UUID id, LocalDateTime ngayThanhToan, BigDecimal soTienThanhToan,
                              String tenPhuongThuc, String ghiChuThanhToan) {
        this.id = id;
        this.ngayThanhToan = ngayThanhToan;
        this.soTienThanhToan = soTienThanhToan;
        this.tenPhuongThuc = tenPhuongThuc;
        this.ghiChuThanhToan = ghiChuThanhToan;
        this.trangThai = null; // Gán mặc định null hoặc giá trị phù hợp nếu cần
    }
}
