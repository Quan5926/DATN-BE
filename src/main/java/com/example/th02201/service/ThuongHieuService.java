package com.example.th02201.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.th02201.repository.*;
import com.example.th02201.dto.*;
import com.example.th02201.model.*;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedWarning;

import java.util.List;
import java.util.Optional;

@Service
public class ThuongHieuService {

    private final ThuongHieuRepository thuongHieuRepository;
    // Giả định SanPhamRepository tồn tại và được inject đúng kiểu
    private final SanPhamRepository sanPhamRepository;

    public ThuongHieuService(final ThuongHieuRepository thuongHieuRepository,
                             final SanPhamRepository sanPhamRepository) {
        this.thuongHieuRepository = thuongHieuRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    public List<ThuongHieuDTO> findAll() {
        // Lấy tất cả các ThuongHieu entities
        final List<ThuongHieu> thuongHieus = thuongHieuRepository.findAll(Sort.by("id"));
        // Ánh xạ chúng sang DTOs, tính toán soLuongSanPham cho từng cái
        return thuongHieus.stream()
                .map(thuongHieu -> mapToDTO(thuongHieu, new ThuongHieuDTO()))
                .toList();
    }

    public ThuongHieuDTO get(final Long id) {
        // Tìm ThuongHieu theo ID và ánh xạ sang DTO, tính toán soLuongSanPham
        return thuongHieuRepository.findById(id)
                .map(thuongHieu -> mapToDTO(thuongHieu, new ThuongHieuDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ThuongHieuDTO thuongHieuDTO) {
        final ThuongHieu thuongHieu = new ThuongHieu();
        mapToEntity(thuongHieuDTO, thuongHieu);
        return thuongHieuRepository.save(thuongHieu).getId();
    }

    public void update(final Long id, final ThuongHieuDTO thuongHieuDTO) {
        final ThuongHieu thuongHieu = thuongHieuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(thuongHieuDTO, thuongHieu);
        thuongHieuRepository.save(thuongHieu);
    }

    public void delete(final Long id) {
        thuongHieuRepository.deleteById(id);
    }

    private ThuongHieuDTO mapToDTO(final ThuongHieu thuongHieu, final ThuongHieuDTO thuongHieuDTO) {
        thuongHieuDTO.setId(thuongHieu.getId());
        thuongHieuDTO.setTenThuongHieu(thuongHieu.getTenThuongHieu());
        thuongHieuDTO.setMaThuongHieu(thuongHieu.getMaThuongHieu());
        thuongHieuDTO.setNgayTao(thuongHieu.getNgayTao());
        thuongHieuDTO.setNgayCapNhat(thuongHieu.getNgayCapNhat());

        // Tính toán và thiết lập soLuongSanPham
        Integer totalQuantity = thuongHieuRepository.sumSoLuongSanPhamByThuongHieuId(thuongHieu.getId());
        thuongHieuDTO.setSoLuongSanPham(totalQuantity != null ? totalQuantity : 0);

        return thuongHieuDTO;
    }

    private ThuongHieu mapToEntity(final ThuongHieuDTO thuongHieuDTO, final ThuongHieu thuongHieu) {
        thuongHieu.setTenThuongHieu(thuongHieuDTO.getTenThuongHieu());
        thuongHieu.setMaThuongHieu(thuongHieuDTO.getMaThuongHieu());
        // Không ánh xạ soLuongSanPham từ DTO sang Entity vì nó là trường được tính toán
        return thuongHieu;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Optional<ThuongHieu> thuongHieuOptional = thuongHieuRepository.findById(id);
        if (thuongHieuOptional.isEmpty()) {
            throw new NotFoundException();
        }
        final ThuongHieu thuongHieu = thuongHieuOptional.get();

        // Giả định findFirstByThuongHieu tồn tại trong SanPhamRepository
        // và entity SanPham có trường 'thuongHieu'
        final SanPham thuongHieuSanPham = sanPhamRepository.findFirstByThuongHieu(thuongHieu);
        if (thuongHieuSanPham != null) {
            referencedWarning.setKey("thuongHieu.sanPham.thuongHieu.referenced");
            referencedWarning.addParam(thuongHieuSanPham.getId());
            return referencedWarning;
        }
        return null;
    }
}
