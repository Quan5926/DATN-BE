package com.example.th02201.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Table(name = "nhan_vienn")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ten_nhan_vien")
    private String tenNhanVien;
    @Column(name = "ngay_sinh")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date ngaySinh;
    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;
    @Column(name = "so_dien_thoai")
    private String soDienThoai;
    @Column(name = "cccd")
    private String cCCD;
    @Column(name = "dia_chi_so_nha_ten_duong")
    private String diaChiSoNhaTenDuong;
    @Column(name = "dia_chi_phuong_xa")
    private String diaChiPhuongXa;
    @Column(name = "dia_chi_quan_huyen")
    private String diaChiQuanHuyen;
    @Column(name = "dia_chi_tinh_thanh")
    private String diaChiTinhThanh;
    @Column(name = "ngay_tao")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date ngayTao;
    @Column(name = "ngay_cap_nhat")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date ngayCapNhat;
    @Column(name = "trang_thai")
    private Boolean trangThai;
    @Column(name = "ma_nhan_vien", insertable = false, updatable = false)
    private String maNhanVien;

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan", referencedColumnName = "id", nullable = true)
    private TaiKhoan taiKhoan;

    @ManyToOne
    @JoinColumn(name = "id_chuc_vu", referencedColumnName = "id", nullable = true)
    private ChucVu chucVu;
}