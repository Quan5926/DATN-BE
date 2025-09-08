package com.example.th02201.controller;

import com.example.th02201.util.ReferencedException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.th02201.repository.*;
import com.example.th02201.service.*;
import com.example.th02201.dto.*;
import com.example.th02201.model.*;
import com.example.th02201.util.NotFoundException;
import com.example.th02201.util.ReferencedWarning;

import java.util.List;

@RestController
@RequestMapping(value = "/api/mauSacs", produces = MediaType.APPLICATION_JSON_VALUE)
public class MauSacController {

    private final MauSacService mauSacService;

    public MauSacController(MauSacService mauSacService) {
        this.mauSacService = mauSacService;
    }

    // Endpoint để tạo màu sắc mới
    @PostMapping
    public ResponseEntity<Long> createMauSac(@RequestBody @Valid final MauSacDTO mauSacDTO) {
        final Long createdId = mauSacService.create(mauSacDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    // Endpoint để lấy tất cả màu sắc
    @GetMapping
    public ResponseEntity<List<MauSacDTO>> getAllMauSacs() {
        return ResponseEntity.ok(mauSacService.findAll());
    }

    // Endpoint để lấy màu sắc theo ID
    @GetMapping("/{id}")
    public ResponseEntity<MauSacDTO> getMauSacById(@PathVariable(name = "id") final Long id) {
        MauSacDTO mauSacDTO = mauSacService.findById(id);
        if (mauSacDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(mauSacDTO);
    }

    // Endpoint để cập nhật màu sắc
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMauSac(@PathVariable(name = "id") final Long id,
                                             @RequestBody @Valid final MauSacDTO mauSacDTO) {
        mauSacService.update(id, mauSacDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Endpoint để xóa màu sắc
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMauSac(@PathVariable(name = "id") final Long id) {
        mauSacService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Xử lý các ngoại lệ (ví dụ: mã màu đã tồn tại, ID không tìm thấy)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Thêm @ExceptionHandler cho ReferencedException
    @ExceptionHandler(ReferencedException.class)
    public ResponseEntity<String> handleReferencedException(ReferencedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // Dùng mã 409 Conflict
    }

    // Thêm @ExceptionHandler cho NotFoundException
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}