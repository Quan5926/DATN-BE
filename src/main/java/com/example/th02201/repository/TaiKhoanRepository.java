package com.example.th02201.repository;

import com.example.th02201.model.KhachHang;
import com.example.th02201.model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaiKhoanRepository  extends JpaRepository<TaiKhoan, Integer> {
}
