package com.example.th02201.respone;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class NhanVienRespone {
    //liet ke tat ca cac yeu cau muon hien thi
    private Integer id;
    private String  tenNhanVien;
    private Date ngaySinh;
    private Boolean gioiTinh;
    private String soDienThoai;
    private String maNhanVien;
    private String cCCD;
    private String diaChiSoNhaTenDuong;
    private String diaChiPhuongXa;
    private String diaChiQuanHuyen;
    private String diaChiTinhThanh;
    private Date ngayTao;
    private Date ngayCapNhat;
    private Boolean trangThai;

}
