package com.example.th02201.service;


import com.example.th02201.dto.TinhThanhDTO;
import com.example.th02201.model.QuanHuyen;
import com.example.th02201.model.TinhThanh;
import com.example.th02201.repository.QuanHuyenRepository;
import com.example.th02201.repository.TinhThanhRepository;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TinhThanhService {

    private final TinhThanhRepository tinhThanhRepository;
    private final QuanHuyenRepository quanHuyenRepository;

    public TinhThanhService(final TinhThanhRepository tinhThanhRepository,
                            final QuanHuyenRepository quanHuyenRepository) {
        this.tinhThanhRepository = tinhThanhRepository;
        this.quanHuyenRepository = quanHuyenRepository;
    }

    public List<TinhThanhDTO> findAll() {
        final List<TinhThanh> tinhThanhs = tinhThanhRepository.findAll(Sort.by("id"));
        return tinhThanhs.stream()
                .map(tinhThanh -> mapToDTO(tinhThanh, new TinhThanhDTO()))
                .toList();
    }

    public TinhThanhDTO get(final Integer id) {
        return tinhThanhRepository.findById(id)
                .map(tinhThanh -> mapToDTO(tinhThanh, new TinhThanhDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TinhThanhDTO tinhThanhDTO) {
        final TinhThanh tinhThanh = new TinhThanh();
        mapToEntity(tinhThanhDTO, tinhThanh);
        return tinhThanhRepository.save(tinhThanh).getId();
    }

    public void update(final Integer id, final TinhThanhDTO tinhThanhDTO) {
        final TinhThanh tinhThanh = tinhThanhRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tinhThanhDTO, tinhThanh);
        tinhThanhRepository.save(tinhThanh);
    }

    public void delete(final Integer id) {
        tinhThanhRepository.deleteById(id);
    }

    private TinhThanhDTO mapToDTO(final TinhThanh tinhThanh, final TinhThanhDTO tinhThanhDTO) {
        tinhThanhDTO.setId(tinhThanh.getId());
        tinhThanhDTO.setTenTinhThanh(tinhThanh.getTenTinhThanh());
        tinhThanhDTO.setMaVung(tinhThanh.getMaVung());
        return tinhThanhDTO;
    }

    private TinhThanh mapToEntity(final TinhThanhDTO tinhThanhDTO, final TinhThanh tinhThanh) {
        tinhThanh.setTenTinhThanh(tinhThanhDTO.getTenTinhThanh());
        tinhThanh.setMaVung(tinhThanhDTO.getMaVung());
        return tinhThanh;
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final TinhThanh tinhThanh = tinhThanhRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final QuanHuyen tinhThanhQuanHuyen = quanHuyenRepository.findFirstByTinhThanh(tinhThanh);
        if (tinhThanhQuanHuyen != null) {
            referencedWarning.setKey("tinhThanh.quanHuyen.tinhThanh.referenced");
            referencedWarning.addParam(tinhThanhQuanHuyen.getId());
            return referencedWarning;
        }
        return null;
    }
}