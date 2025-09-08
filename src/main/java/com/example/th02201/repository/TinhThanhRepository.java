package com.example.th02201.repository;

import com.example.th02201.model.TinhThanh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface TinhThanhRepository extends JpaRepository<TinhThanh, Integer> {
}