package com.example.th02201.model;

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
public class KhachHangCTSP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ten_khach_hang")
    private String tenKH;
    @Column(name = "so_dien_thoai")
    private String soDT;
    @Column(name = "ma_khach_hang", insertable = false, updatable = false)
    private String maKH;
    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayT;
    @ManyToOne
    @JoinColumn(name = "id_tai_khoan", referencedColumnName = "id", nullable = true)
    private TaiKhoan taiKhoan;

}

