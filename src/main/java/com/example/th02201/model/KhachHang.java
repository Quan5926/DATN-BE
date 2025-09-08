package com.example.th02201.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Table(name = "khach_hangg")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ten_khach_hang")
    private String tenKhachHang;
    @Column(name = "so_dien_thoai")
    private String soDienThoai;
    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;
    @Column(name = "ma_khach_hang", insertable = false, updatable = false)
    private String maKhachHang;

    @Column(name = "ngay_sinh")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date ngaySinh;

    @Column(name = "ngay_tao")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date ngayCapNhat;

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan", referencedColumnName = "id", nullable = true)
    private TaiKhoan taiKhoan;
}