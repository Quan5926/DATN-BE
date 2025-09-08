package com.example.th02201.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NhanVienRequest {
    //liet ke cac thuoc tinh muon add
//    private Integer chucVuID;
//    private Integer taiKhoanID;
    @JsonProperty("ChucVuID")
    private Integer chucVuID;
    //
    @JsonProperty("TaiKhoanID")
    private Integer taiKhoanID;
    private String  tenNhanVien;
    private Date ngaySinh;
    private Boolean gioiTinh;
    private String soDienThoai;
    //    private String maNhanVien;
    private String cCCD;
    private String diaChiSoNhaTenDuong;
    private String diaChiPhuongXa;
    private String diaChiQuanHuyen;
    private String diaChiTinhThanh;
    private Date ngayTao;
    private Date ngayCapNhat;

    private Boolean trangThai;
}
