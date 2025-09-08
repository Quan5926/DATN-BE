package com.example.th02201.repository;
import com.example.th02201.model.TrangThai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TrangThaiRepository extends JpaRepository<TrangThai, Integer> {

    // Phương thức tìm kiếm trạng thái theo tên trạng thái
    Optional<TrangThai> findByTenTrangThai(String tenTrangThai);

    @Query("SELECT tt FROM TrangThai tt WHERE LOWER(tt.tenTrangThai) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TrangThai> searchByTenTrangThai(@Param("keyword") String keyword);
}
