package com.hienbui.unittest.controller;

import com.hienbui.unittest.dto.BankDto;
import com.hienbui.unittest.dto.request.BankRequest;
import com.hienbui.unittest.dto.response.ResponseData;
import com.hienbui.unittest.dto.search.SearchDto;
import com.hienbui.unittest.service.BankService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/banks")
public class RestBankController {

    @Autowired
    private BankService bankService;

    /**
     * Create
     */
    @PostMapping
    public ResponseData<BankDto> create(
            @Valid @RequestBody BankRequest dto
    ) {
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Thêm mới ngân hàng thành công.",
                bankService.createBack(dto)
        );
    }

    /**
     * Update
     */
    @PutMapping("/{id}")
    public ResponseData<BankDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody BankRequest dto
    ) {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Chỉnh sửa ngân hàng thành công.",
                bankService.updateBack(id, dto)
        );
    }

    /**
     * Delete
     */
    @DeleteMapping("/{id}")
    public ResponseData<Void> delete(
            @PathVariable UUID id
    ) {
        bankService.deleteBack(id);

        return new ResponseData<>(
                HttpStatus.NO_CONTENT.value(),
                "Xóa ngân hàng thành công."
        );
    }

    /**
     * Get detail
     */
    @GetMapping("/{id}")
    public ResponseData<BankDto> getById(
            @PathVariable UUID id
    ) {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy chi tiết ngân hàng thành công.",
                bankService.getById(id)
        );
    }

    /**
     * Paging search
     */
    @PostMapping("/search")
    public ResponseData<Page<BankDto>> pagingSearch(
            @RequestBody SearchDto dto
    ) {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy danh sách ngân hàng thành công.",
                bankService.findAllBack(dto)
        );
    }
}