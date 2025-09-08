package com.example.th02201.service;

import com.example.th02201.dto.QuanHuyenDTO;
import com.example.th02201.model.PhuongXa;
import com.example.th02201.model.QuanHuyen;
import com.example.th02201.model.TinhThanh;
import com.example.th02201.repository.PhuongXaRepository;
import com.example.th02201.repository.QuanHuyenRepository;
import com.example.th02201.repository.TinhThanhRepository;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuanHuyenService {

    private final QuanHuyenRepository quanHuyenRepository;
    private final TinhThanhRepository tinhThanhRepository;
    private final PhuongXaRepository phuongXaRepository;

    public QuanHuyenService(final QuanHuyenRepository quanHuyenRepository,
                            final TinhThanhRepository tinhThanhRepository,
                            final PhuongXaRepository phuongXaRepository) {
        this.quanHuyenRepository = quanHuyenRepository;
        this.tinhThanhRepository = tinhThanhRepository;
        this.phuongXaRepository = phuongXaRepository;
    }

    public List<QuanHuyenDTO> findAll() {
        final List<QuanHuyen> quanHuyens = quanHuyenRepository.findAll(Sort.by("id"));
        return quanHuyens.stream()
                .map(quanHuyen -> mapToDTO(quanHuyen, new QuanHuyenDTO()))
                .toList();
    }

    public QuanHuyenDTO get(final Integer id) {
        return quanHuyenRepository.findById(id)
                .map(quanHuyen -> mapToDTO(quanHuyen, new QuanHuyenDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final QuanHuyenDTO quanHuyenDTO) {
        final QuanHuyen quanHuyen = new QuanHuyen();
        mapToEntity(quanHuyenDTO, quanHuyen);
        return quanHuyenRepository.save(quanHuyen).getId();
    }

    public void update(final Integer id, final QuanHuyenDTO quanHuyenDTO) {
        final QuanHuyen quanHuyen = quanHuyenRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(quanHuyenDTO, quanHuyen);
        quanHuyenRepository.save(quanHuyen);
    }

    public void delete(final Integer id) {
        quanHuyenRepository.deleteById(id);
    }

    private QuanHuyenDTO mapToDTO(final QuanHuyen quanHuyen, final QuanHuyenDTO quanHuyenDTO) {
        quanHuyenDTO.setId(quanHuyen.getId());
        quanHuyenDTO.setTenQuanHuyen(quanHuyen.getTenQuanHuyen());
        quanHuyenDTO.setMaHuyen(quanHuyen.getMaHuyen());
        quanHuyenDTO.setTinhThanh(quanHuyen.getTinhThanh() == null ? null : quanHuyen.getTinhThanh().getId());
        return quanHuyenDTO;
    }

    private QuanHuyen mapToEntity(final QuanHuyenDTO quanHuyenDTO, final QuanHuyen quanHuyen) {
        quanHuyen.setTenQuanHuyen(quanHuyenDTO.getTenQuanHuyen());
        quanHuyen.setMaHuyen(quanHuyenDTO.getMaHuyen());
        final TinhThanh tinhThanh = quanHuyenDTO.getTinhThanh() == null ? null : tinhThanhRepository.findById(quanHuyenDTO.getTinhThanh())
                .orElseThrow(() -> new NotFoundException("tinhThanh not found"));
        quanHuyen.setTinhThanh(tinhThanh);
        return quanHuyen;
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final QuanHuyen quanHuyen = quanHuyenRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final PhuongXa quanHuyenPhuongXa = phuongXaRepository.findFirstByQuanHuyen(quanHuyen);
        if (quanHuyenPhuongXa != null) {
            referencedWarning.setKey("quanHuyen.phuongXa.quanHuyen.referenced");
            referencedWarning.addParam(quanHuyenPhuongXa.getId());
            return referencedWarning;
        }
        return null;
    }
}