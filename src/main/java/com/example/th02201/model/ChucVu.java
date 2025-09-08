package com.example.th02201.model;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(name = "chuc_vu")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class ChucVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ten_chuc_vu")
    private String tenChucVu;
    @Column(name = "ma_chuc_vu")
    private String maChucVu;
    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayTao;
    @Column(name = "ngay_cap_nhat")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngaycapnhat;
}
