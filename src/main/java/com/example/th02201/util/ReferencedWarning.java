package com.example.th02201.util;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReferencedWarning {

    // Đây là các trường ban đầu, có thể dùng cho cảnh báo chung hoặc tham số.
    // Nếu không dùng, có thể xem xét loại bỏ.
    private String key = null;
    private ArrayList<Object> params = new ArrayList<>();

    // Danh sách để lưu trữ các thông báo cảnh báo cụ thể (ví dụ: "Ảnh sản phẩm (3)")
    private List<String> warnings = new ArrayList<>(); //

    public void addParam(final Long param) {
        params.add(param);
    }

    public void addParam(final UUID param) {
        params.add(param);
    }

    public void addParam(final Integer param) {
        params.add(param);
    }

    /**
     * Thêm một thông báo cảnh báo cụ thể về số lượng tham chiếu.
     * Ví dụ: addWarning("Ảnh sản phẩm", 3) sẽ thêm "Ảnh sản phẩm (3)" vào danh sách.
     * @param description Mô tả loại tham chiếu (ví dụ: "Ảnh sản phẩm", "Hóa đơn chi tiết").
     * @param count Số lượng tham chiếu.
     */
    public void addWarning(String description, int count) { //
        this.warnings.add(description + " (" + count + ")");
    }

    /**
     * Kiểm tra xem có bất kỳ cảnh báo nào đã được thêm vào hay không.
     * @return true nếu có cảnh báo, ngược lại là false.
     */
    public boolean hasWarnings() { //
        return !warnings.isEmpty();
    }

    /**
     * Chuyển đổi tất cả các cảnh báo thành một thông điệp chuỗi duy nhất.
     * Kết hợp cả thông điệp chung (key/params) và các cảnh báo cụ thể.
     * @return Chuỗi thông báo cảnh báo.
     */
    public String toMessage() { //
        StringBuilder messageBuilder = new StringBuilder();

        // Xử lý thông báo chung từ key và params (nếu có)
        if (key != null) {
            messageBuilder.append(key);
            if (!params.isEmpty()) {
                messageBuilder.append(",").append(params.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(",")));
            }
        }

        // Xử lý các thông báo cảnh báo cụ thể
        if (!warnings.isEmpty()) {
            if (messageBuilder.length() > 0) {
                messageBuilder.append("; "); // Dấu phân cách nếu có cả hai loại thông báo
            }
            messageBuilder.append(warnings.stream()
                    .collect(Collectors.joining("; "))); // Nối các cảnh báo cụ thể
        }
        return messageBuilder.toString();
    }
}