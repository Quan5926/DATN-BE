package com.example.th02201.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuanHuyenDTO {

    private Integer id;

    @NotNull
    @Size(max = 100)
    private String tenQuanHuyen;

    @Size(max = 10)
    private String maHuyen;

    @NotNull
    private Integer tinhThanh;
}