package com.example.th02201.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhuongXaDTO {

    private Integer id;

    @NotNull
    @Size(max = 100)
    private String tenPhuongXa;

    @Size(max = 10)
    private String maXa;

    @NotNull
    private Integer quanHuyen;
}