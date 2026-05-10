package com.hienbui.unittest.service;

import com.hienbui.unittest.dto.BankDto;
import com.hienbui.unittest.dto.request.BankRequest;
import com.hienbui.unittest.dto.search.SearchDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BankService {
    BankDto createBack(@Valid BankRequest request);

    BankDto updateBack(UUID id, @Valid BankRequest request);

    BankDto getById(UUID id);

    void deleteBack(UUID id);

    Page<BankDto> findAllBack(SearchDto dto);
}
