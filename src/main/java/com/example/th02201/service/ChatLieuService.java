package com.example.th02201.service;


import com.example.th02201.dto.ChatLieuDTO;
import com.example.th02201.model.ChatLieu;
import com.example.th02201.model.ChiTietSanPham;
import com.example.th02201.repository.ChatLieuRepository;
import com.example.th02201.repository.ChiTietSanPhamRepository;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatLieuService {

    private final ChatLieuRepository chatLieuRepository;
    // Giả định ChiTietSanPhamRepository tồn tại và được inject đúng kiểu
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    public ChatLieuService(final ChatLieuRepository chatLieuRepository,
                           final ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.chatLieuRepository = chatLieuRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    public List<ChatLieuDTO> findAll() {
        // Lấy tất cả các ChatLieu entities
        final List<ChatLieu> chatLieus = chatLieuRepository.findAll(Sort.by("id"));
        // Ánh xạ chúng sang DTOs, tính toán soLuongSanPham cho từng cái
        return chatLieus.stream()
                .map(chatLieu -> mapToDTO(chatLieu, new ChatLieuDTO()))
                .toList();
    }

    public ChatLieuDTO get(final Long id) {
        // Tìm ChatLieu theo ID và ánh xạ sang DTO, tính toán soLuongSanPham
        return chatLieuRepository.findById(id)
                .map(chatLieu -> mapToDTO(chatLieu, new ChatLieuDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ChatLieuDTO chatLieuDTO) {
        final ChatLieu chatLieu = new ChatLieu();
        mapToEntity(chatLieuDTO, chatLieu);
        return chatLieuRepository.save(chatLieu).getId();
    }

    public void update(final Long id, final ChatLieuDTO chatLieuDTO) {
        final ChatLieu chatLieu = chatLieuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(chatLieuDTO, chatLieu);
        chatLieuRepository.save(chatLieu);
    }

    public void delete(final Long id) {
        chatLieuRepository.deleteById(id);
    }

    private ChatLieuDTO mapToDTO(final ChatLieu chatLieu, final ChatLieuDTO chatLieuDTO) {
        chatLieuDTO.setId(chatLieu.getId());
        chatLieuDTO.setTenChatLieu(chatLieu.getTenChatLieu());
        chatLieuDTO.setMaChatLieu(chatLieu.getMaChatLieu());

        // Tính toán và thiết lập soLuongSanPham
        Integer totalQuantity = chatLieuRepository.sumSoLuongSanPhamByChatLieuId(chatLieu.getId());
        chatLieuDTO.setSoLuongSanPham(totalQuantity != null ? totalQuantity : 0);
        chatLieuDTO.setNgayTao(chatLieu.getNgayTao());
        chatLieuDTO.setNgayCapNhat(chatLieu.getNgayCapNhat());


        return chatLieuDTO;
    }

    private ChatLieu mapToEntity(final ChatLieuDTO chatLieuDTO, final ChatLieu chatLieu) {
        chatLieu.setTenChatLieu(chatLieuDTO.getTenChatLieu());
        chatLieu.setMaChatLieu(chatLieuDTO.getMaChatLieu());
        // Không ánh xạ soLuongSanPham từ DTO sang Entity vì nó là trường được tính toán
        return chatLieu;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Optional<ChatLieu> chatLieuOptional = chatLieuRepository.findById(id);
        if (chatLieuOptional.isEmpty()) {
            throw new NotFoundException();
        }
        final ChatLieu chatLieu = chatLieuOptional.get();

        // Giả định findFirstByChatLieu tồn tại trong ChiTietSanPhamRepository
        // và entity ChiTietSanPham có trường 'chatLieu'
        final ChiTietSanPham chatLieuChiTietSanPham = chiTietSanPhamRepository.findFirstByChatLieu(chatLieu);
        if (chatLieuChiTietSanPham != null) {
            referencedWarning.setKey("chatLieu.chiTietSanPham.chatLieu.referenced");
            referencedWarning.addParam(chatLieuChiTietSanPham.getId());
            return referencedWarning;
        }
        return null;
    }
}

