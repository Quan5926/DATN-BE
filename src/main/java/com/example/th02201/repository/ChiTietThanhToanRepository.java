package com.example.th02201.repository;
import com.example.th02201.model.ChiTietThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChiTietThanhToanRepository extends JpaRepository<ChiTietThanhToan, UUID> {
    List<ChiTietThanhToan> findByHoaDonId(UUID hoaDonId);
}
