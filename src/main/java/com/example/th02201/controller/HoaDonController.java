package com.example.th02201.controller;

import com.example.th02201.dto.HoaDonDetailDTO;
import com.example.th02201.dto.HoaDonRequestDTO;
import com.example.th02201.dto.PaymentRequestDTO;
import com.example.th02201.dto.SanPhamTraLaiDTO;
import com.example.th02201.service.HoaDonService;
import com.example.th02201.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller để quản lý các API liên quan đến Hóa đơn (Invoices).
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173"})
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private PaymentService paymentService;

    /**
     * Endpoint kiểm tra trạng thái hoạt động của backend.
     * @return Một chuỗi thông báo.
     */
    @GetMapping("/")
    public String home() {
        return "Backend is running!";
    }

    /**
     * Lấy danh sách tất cả các hóa đơn.
     * @return ResponseEntity chứa danh sách HoaDonDetailDTO.
     */
    @GetMapping("/invoices")
    public ResponseEntity<List<HoaDonDetailDTO>> getAllInvoices() {
        List<HoaDonDetailDTO> invoices = hoaDonService.getAllInvoices();
        System.out.println("Số hóa đơn tìm thấy: " + invoices.size());
        return ResponseEntity.ok(invoices);
    }

    /**
     * Lấy thông tin chi tiết một hóa đơn theo ID.
     * @param id ID của hóa đơn.
     * @return ResponseEntity chứa HoaDonDetailDTO nếu tìm thấy, ngược lại trả về 404 Not Found.
     */
    @GetMapping("/invoices/{id}")
    public ResponseEntity<HoaDonDetailDTO> getInvoiceById(@PathVariable UUID id) {
        Optional<HoaDonDetailDTO> invoice = hoaDonService.getInvoiceById(id);
        return invoice.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cập nhật trạng thái của hóa đơn.
     * @param id ID của hóa đơn.
     * @param payload Map chứa newStatusId và ghiChu.
     * @return ResponseEntity chứa HoaDonDetailDTO đã cập nhật.
     */
    @PatchMapping("/invoices/{id}/status")
    public ResponseEntity<HoaDonDetailDTO> updateInvoiceStatus(@PathVariable UUID id, @RequestBody Map<String, Object> payload) {
        // Sử dụng Optional để xử lý an toàn
        Integer newStatusId = Optional.ofNullable(payload.get("newStatusId"))
                .filter(Number.class::isInstance)
                .map(n -> ((Number) n).intValue())
                .orElse(null);
        String ghiChu = (String) payload.get("ghiChu");

        if (newStatusId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Optional<HoaDonDetailDTO> updatedInvoice = hoaDonService.updateInvoiceStatus(id, newStatusId, ghiChu);
            return updatedInvoice.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi cập nhật trạng thái hóa đơn: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.err.println("Lỗi server khi cập nhật trạng thái hóa đơn: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Hủy một hóa đơn.
     * @param id ID của hóa đơn.
     * @param payload Map chứa lý do hủy (ghiChu).
     * @return ResponseEntity chứa HoaDonDetailDTO đã hủy.
     */
    @PatchMapping("/invoices/{id}/cancel")
    public ResponseEntity<HoaDonDetailDTO> cancelInvoice(@PathVariable UUID id, @RequestBody Map<String, String> payload) {
        String ghiChu = payload.get("ghiChu");
        try {
            Optional<HoaDonDetailDTO> canceledInvoice = hoaDonService.cancelInvoice(id, ghiChu);
            return canceledInvoice.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi hủy hóa đơn: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.err.println("Lỗi server khi hủy hóa đơn: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Thêm một khoản thanh toán cho hóa đơn.
     * @param id ID của hóa đơn.
     * @param paymentRequestDTO DTO chứa thông tin thanh toán.
     * @return ResponseEntity chứa HoaDonDetailDTO đã cập nhật.
     */
    @PostMapping("/invoices/{id}/payment")
    public ResponseEntity<?> addPayment(@PathVariable UUID id, @RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            paymentService.addPayment(id, paymentRequestDTO);
            Optional<HoaDonDetailDTO> updatedInvoice = hoaDonService.getInvoiceById(id);

            if (updatedInvoice.isPresent()) {
                return ResponseEntity.ok(updatedInvoice.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Không tìm thấy hóa đơn với ID: " + id));
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi khi thêm thanh toán: " + e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            System.err.println("Lỗi server khi thêm thanh toán: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Hoàn tiền cho hóa đơn.
     * @param id ID của hóa đơn.
     * @param payload Map chứa số tiền hoàn và ghi chú.
     * @return ResponseEntity chứa HoaDonDetailDTO đã cập nhật.
     */
    @PostMapping("/invoices/{id}/refund")
    public ResponseEntity<HoaDonDetailDTO> refundInvoice(@PathVariable UUID id, @RequestBody Map<String, Object> payload) {
        try {
            BigDecimal refundAmount = Optional.ofNullable(payload.get("refundAmount"))
                    .filter(o -> o instanceof Number || o instanceof String)
                    .map(o -> new BigDecimal(o.toString()))
                    .orElseThrow(() -> new IllegalArgumentException("Số tiền hoàn không hợp lệ."));
            String refundNote = (String) payload.get("refundNote");

            if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(null);
            }
            if (refundNote == null || refundNote.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            Optional<HoaDonDetailDTO> updatedInvoice = hoaDonService.refundInvoice(id, refundAmount, refundNote);
            return updatedInvoice.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            System.err.println("Lỗi định dạng số tiền hoàn: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.err.println("Lỗi khi hoàn tiền: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Trả lại sản phẩm trong một hóa đơn.
     * @param id ID của hóa đơn.
     * @param payload Map chứa lý do trả hàng và danh sách sản phẩm.
     * @return ResponseEntity chứa HoaDonDetailDTO đã cập nhật.
     */
    @PostMapping("/invoices/{id}/return-products")
    public ResponseEntity<HoaDonDetailDTO> returnProducts(@PathVariable UUID id, @RequestBody Map<String, Object> payload) {
        try {
            String returnReason = (String) payload.get("reason");
            List<Map<String, Object>> rawItems = (List<Map<String, Object>>) payload.get("items");

            // Tinh chỉnh logic chuyển đổi, làm cho nó rõ ràng hơn
            List<SanPhamTraLaiDTO> itemsToReturnDto = rawItems.stream()
                    .map(itemMap -> {
                        SanPhamTraLaiDTO dto = new SanPhamTraLaiDTO();
                        Object idObj = itemMap.get("id");
                        if (idObj == null) {
                            throw new IllegalArgumentException("ID của chi tiết hóa đơn không được rỗng.");
                        }

                        // Chuyển đổi ID từ Number hoặc String sang Integer an toàn
                        Integer chiTietHoaDonId;
                        if (idObj instanceof Number) {
                            chiTietHoaDonId = ((Number) idObj).intValue();
                        } else if (idObj instanceof String) {
                            try {
                                chiTietHoaDonId = Integer.parseInt((String) idObj);
                            } catch (NumberFormatException e) {
                                throw new IllegalArgumentException("Định dạng ID chi tiết hóa đơn không hợp lệ.", e);
                            }
                        } else {
                            throw new IllegalArgumentException("Kiểu dữ liệu ID chi tiết hóa đơn không hợp lệ.");
                        }

                        dto.setChiTietHoaDonId(chiTietHoaDonId);

                        // Xử lý số lượng trả lại an toàn hơn
                        Object returnQuantityObj = itemMap.get("returnQuantity");
                        if (returnQuantityObj instanceof Number) {
                            dto.setSoLuongTra(((Number) returnQuantityObj).intValue());
                        } else {
                            throw new IllegalArgumentException("Số lượng trả lại không hợp lệ.");
                        }

                        return dto;
                    })
                    .collect(Collectors.toList());

            if (returnReason == null || returnReason.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            if (itemsToReturnDto.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            Optional<HoaDonDetailDTO> updatedInvoice = hoaDonService.returnProducts(id, itemsToReturnDto, returnReason);
            return updatedInvoice.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi trả hàng: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.err.println("Lỗi server khi trả hàng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Xóa một hóa đơn.
     * @param id ID của hóa đơn.
     * @return ResponseEntity 204 No Content nếu xóa thành công, ngược lại 404 Not Found.
     */
    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) {
        boolean deleted = hoaDonService.deleteInvoice(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    /**
     * Lớp hỗ trợ để trả về phản hồi lỗi tùy chỉnh.
     */
    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
