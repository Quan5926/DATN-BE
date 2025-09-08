package com.example.th02201.model;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(name = "tai_khoan")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ten_dang_nhap")
    private String  tenDangNhap;
    @Column(name = "mat_khau_hash")
    private String matKhauHash;
    @Column(name = "email")
    private String email;
    @Column(name = "vai_tro")
    private String vaiTro;
    @Column(name = "trang_thai")
    private Boolean trangThai;
    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayTao;
    @Temporal(TemporalType.DATE)
    @Column(name="ngay_cap_nhat")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayCapNhat;
//    @ManyToOne
//    @JoinColumn(name = "id_chuc_vu", referencedColumnName = "id")
//    private ChucVu chucVu;
}
