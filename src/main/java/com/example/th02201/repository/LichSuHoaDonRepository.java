package com.example.th02201.repository;
import com.example.th02201.model.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

/**
 * Interface Repository cho bảng 'lich_su_hoa_don'.
 * Cung cấp các phương thức CRUD cơ bản và các truy vấn tùy chỉnh cho Entity LichSuHoaDon.
 */
@Repository
public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer> {
    // Bạn có thể thêm các phương thức truy vấn tùy chỉnh ở đây nếu cần.
    // Ví dụ:
    // List<LichSuHoaDon> findByHoaDon_Id(UUID hoaDonId);
    // List<LichSuHoaDon> findByNhanVienThucHien_Id(Integer nhanVienId);
}
