package com.example.th02201.respone;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class KhachHangRespone {
    //liet ke tat ca cac yeu cau muon hien thi
    private Integer id;
    private String tenKhachHang;
    private String soDienThoai;
    private Boolean gioiTinh;
    private Date ngaySinh;
    private String maKhachHang;
    private Date ngayTao;
    private Date ngayCapNhat;
}
