package com.example.th02201.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KhachHangRequest {
    //liet ke cac thuoc tinh muon add
    private Integer taiKhoanID;
    private String tenKhachHang;
    private String soDienThoai;
    private Boolean gioiTinh;
    private Date ngaySinh;
    //    private String maKhachHang;
    private Date ngayTao;
    private Date ngayCapNhat;
    private Boolean trangThai;
}
