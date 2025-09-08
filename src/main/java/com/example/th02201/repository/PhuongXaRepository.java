package com.example.th02201.repository;

import com.example.th02201.model.PhuongXa;
import com.example.th02201.model.QuanHuyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhuongXaRepository extends JpaRepository<PhuongXa, Integer> {

    PhuongXa findFirstByQuanHuyen(QuanHuyen quanHuyen);

}
