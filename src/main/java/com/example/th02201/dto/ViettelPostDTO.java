package com.example.th02201.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Lớp DTO để ánh xạ dữ liệu trả về từ Viettel Post API.
 */
public class ViettelPostDTO {

    @Data
    public static class Province {
        @JsonProperty("PROVINCE_ID")
        private Integer provinceId;
        @JsonProperty("PROVINCE_NAME")
        private String provinceName;
        @JsonProperty("PROVINCE_CODE")
        private String provinceCode;
    }

    @Data
    public static class District {
        @JsonProperty("DISTRICT_ID")
        private Integer districtId;
        @JsonProperty("DISTRICT_NAME")
        private String districtName;
        @JsonProperty("PROVINCE_ID")
        private Integer provinceId;
        @JsonProperty("DISTRICT_CODE")
        private String districtCode;
    }

    @Data
    public static class Ward {
        @JsonProperty("WARDS_ID")
        private Integer wardsId;
        @JsonProperty("WARDS_NAME")
        private String wardsName;
        @JsonProperty("DISTRICT_ID")
        private Integer districtId;
        @JsonProperty("WARDS_CODE")
        private String wardsCode;
    }

    @Data
    public static class ViettelPostResponse<T> {
        private int status;
        private boolean error;
        private String message;
        private T data;
    }

    @Data
    public static class ViettelPostLoginResponse {
        private int status;
        private boolean error;
        private String message;
        private LoginData data;
    }

    @Data
    public static class LoginData {
        private String token;
    }

    @Data
    public static class ViettelPostListResponse<T> {
        private int status;
        private boolean error;
        private String message;
        private List<T> data;
    }
}
