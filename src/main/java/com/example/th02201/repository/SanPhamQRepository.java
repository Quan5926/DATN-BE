package com.example.th02201.repository;
import com.example.th02201.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SanPhamQRepository extends JpaRepository<SanPham, Long> {
}

