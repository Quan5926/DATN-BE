package com.example.th02201.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // Tự động tạo getters, setters, toString, equals và hashCode
@NoArgsConstructor // Tự động tạo constructor không tham số
@AllArgsConstructor // Tự động tạo constructor với tất cả các tham số
@Builder
public class TrangThaiDTO {

    private Integer id;

    @NotNull
    @Size(max = 50)
    private String tenTrangThai;

//    @Size(max = 255)
//    private String moTa;
}
