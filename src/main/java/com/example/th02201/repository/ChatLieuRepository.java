package com.example.th02201.repository;

import com.example.th02201.model.ChatLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLieuRepository extends JpaRepository<ChatLieu, Long> {


    // Phương thức để lấy danh sách chất liệu đã sắp xếp theo tên
    @Query("SELECT cl FROM ChatLieu cl ORDER BY cl.tenChatLieu ASC")
    List<ChatLieu> findAllOrderedByName();

    // Truy vấn để tính tổng số lượng của ChiTietSanPham liên kết với một ChatLieu
    // Giả định ChiTietSanPham có trường 'soLuong' và mối quan hệ 'chatLieu'
    @Query("SELECT COALESCE(SUM(ctsp.soLuongTonKho), 0) FROM ChiTietSanPham ctsp WHERE ctsp.chatLieu.id = :chatLieuId")
    Integer sumSoLuongSanPhamByChatLieuId(@Param("chatLieuId") Long chatLieuId);
}
