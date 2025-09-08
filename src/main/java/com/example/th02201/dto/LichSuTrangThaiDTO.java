package com.example.th02201.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * DTO cho lịch sử trạng thái hóa đơn.
 * Phản ánh cấu trúc `statusHistory` trong Vue.js.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LichSuTrangThaiDTO {
    private Integer id; // Đã sửa từ Long sang Integer để khớp với ID của LichSuHoaDon entity
    private String hanhDong; // Từ lich_su_hoa_don.hanh_dong
    private String moTaHanhDong; // Từ lich_su_hoa_don.mo_ta_hanh_dong
    private LocalDateTime thoiGian; // Từ lich_su_hoa_don.thoi_gian
}
