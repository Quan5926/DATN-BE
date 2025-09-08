package com.example.th02201.repository;


import com.example.th02201.dto.DiaChiKhachHangDTO;
import com.example.th02201.model.DiaChiKhachHang;
import com.example.th02201.model.KhachHang;
import com.example.th02201.model.PhuongXa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DiaChiKhachHangRepository extends JpaRepository<DiaChiKhachHang, Integer> {

    DiaChiKhachHang findFirstByKhachHang(KhachHang khachHang);

    DiaChiKhachHang findFirstByPhuongXa(PhuongXa phuongXa);

    @Query("SELECT new com.example.th02201.dto.DiaChiKhachHangDTO("
            + "d.id, d.tenNguoiNhan, d.soDienThoaiNguoiNhan, d.soNhaTenDuong, d.ghiChu, d.laDiaChiMacDinh, "
            + "d.ngayTao, d.ngayCapNhat, d.khachHang.id, d.phuongXa.id, "
            + "p.tenPhuongXa, q.id, q.tenQuanHuyen, q.maHuyen, t.id, t.tenTinhThanh, t.maVung) "
            + "FROM DiaChiKhachHang d "
            + "JOIN d.phuongXa p "
            + "JOIN p.quanHuyen q "
            + "JOIN q.tinhThanh t")
    List<DiaChiKhachHangDTO> findAllWithDetails();

    @Query("SELECT new com.example.th02201.dto.DiaChiKhachHangDTO("
            + "d.id, d.tenNguoiNhan, d.soDienThoaiNguoiNhan, d.soNhaTenDuong, d.ghiChu, d.laDiaChiMacDinh, "
            + "d.ngayTao, d.ngayCapNhat, d.khachHang.id, d.phuongXa.id, "
            + "p.tenPhuongXa, q.id, q.tenQuanHuyen, q.maHuyen, t.id, t.tenTinhThanh, t.maVung) "
            + "FROM DiaChiKhachHang d "
            + "JOIN d.phuongXa p "
            + "JOIN p.quanHuyen q "
            + "JOIN q.tinhThanh t "
            + "WHERE d.id = :id")
    Optional<DiaChiKhachHangDTO> findByIdWithDetails(@Param("id") Integer id);

}
