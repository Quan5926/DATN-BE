package com.example.th02201.repository;
import com.example.th02201.model.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SizeRepository extends JpaRepository<KichCo, Integer> {
}

