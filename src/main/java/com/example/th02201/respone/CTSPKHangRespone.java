package com.example.th02201.respone;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CTSPKHangRespone {
    private Integer id;
    private String maKH;
    private String tenKH;
    private String soDT;
    private Date ngayT;
}
