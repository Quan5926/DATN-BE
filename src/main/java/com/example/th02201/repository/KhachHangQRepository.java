package com.example.th02201.repository;
import com.example.th02201.model.KhachHang;
import com.example.th02201.model.KhachHangQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface KhachHangQRepository extends JpaRepository<KhachHangQ, UUID> {
    Optional<KhachHangQ> findBySoDienThoai(String soDienThoai);
    Optional<KhachHangQ> findByTenKhachHang(String tenKhachHang); // Hữu ích để tìm "Khách lẻ"
}

