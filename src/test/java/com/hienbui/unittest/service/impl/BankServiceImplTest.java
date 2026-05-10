package com.hienbui.unittest.service.impl;

import com.hienbui.unittest.domain.Bank;
import com.hienbui.unittest.dto.BankDto;
import com.hienbui.unittest.dto.request.BankRequest;
import com.hienbui.unittest.dto.search.SearchDto;
import com.hienbui.unittest.exception.ConflictDataException;
import com.hienbui.unittest.exception.InvalidDataException;
import com.hienbui.unittest.exception.ResourceNotFoundException;
import com.hienbui.unittest.repository.BankRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BankServiceImplTest {

    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private BankServiceImpl bankService;

    private Validator validator;

    private UUID bankId;
    private Bank bank;
    private BankRequest request;

    @BeforeEach
    void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        bankId = UUID.randomUUID();

        bank = new Bank();
        bank.setId(bankId);
        bank.setCode("VCB");
        bank.setName("Vietcombank");
        bank.setDescription("Test");

        request = new BankRequest();
        request.setCode("VCB");
        request.setName("Vietcombank");
        request.setDescription("Test");
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Test
    void createBack_success() {

        when(bankRepository.findByCode("VCB"))
                .thenReturn(Collections.emptyList());

        when(bankRepository.save(any(Bank.class)))
                .thenReturn(bank);

        BankDto result = bankService.createBack(request);

        assertNotNull(result);
        assertEquals("VCB", result.getCode());
        assertEquals("Vietcombank", result.getName());

        verify(bankRepository, times(1)).save(any(Bank.class));
    }

    @Test
    void createBack_duplicateCode_throwConflict() {

        when(bankRepository.findByCode("VCB"))
                .thenReturn(List.of(bank));

        assertThrows(
                ConflictDataException.class,
                () -> bankService.createBack(request)
        );

        verify(bankRepository, never()).save(any());
    }

    @Test
    void createBack_blankCode_throwInvalid() {

        request.setCode("Mã ngân hàng không được bỏ trống. Mã ngân hàng không được bỏ trống. Mã ngân hàng không được bỏ trống.");
        request.setName("");

        Set<ConstraintViolation<BankRequest>> violations = validator.validate(request);
        assertThat(violations)
                .anyMatch(v -> v.getMessage().startsWith("Mã ngân hàng tối đa"))
                .anyMatch(v -> v.getMessage().equals("Tên ngân hàng không được bỏ trống."))
        ;
    }

    @Test
    void createBack_blankCode_throwMaxSize() {

        request.setCode("Mã ngân hàng không được bỏ trống. Mã ngân hàng không được bỏ trống. Mã ngân hàng không được bỏ trống.");

        Set<ConstraintViolation<BankRequest>> violations = validator.validate(request);
        assertThat(violations)
                .anyMatch(v -> v.getMessage().equals("Mã ngân hàng tối đa 50 ký tự."));
    }

    @Test
    void createBack_blankName_throwInvalid() {

        request.setName("");

        assertThrows(
                InvalidDataException.class,
                () -> bankService.createBack(request)
        );
    }

    @Test
    void createBack_nullDto_throwInvalid() {

        assertThrows(
                InvalidDataException.class,
                () -> bankService.createBack(null)
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Test
    void updateBack_success() {

        when(bankRepository.findById(bankId))
                .thenReturn(Optional.of(bank));

        when(bankRepository.findByIdAndCode(bankId, "VCB"))
                .thenReturn(Collections.emptyList());

        when(bankRepository.save(any(Bank.class)))
                .thenReturn(bank);

        BankDto result = bankService.updateBack(bankId, request);

        assertNotNull(result);
        assertEquals(bankId, result.getId());

        verify(bankRepository, times(1)).save(any(Bank.class));
    }

    @Test
    void updateBack_nullId_throwInvalid() {

        assertThrows(
                InvalidDataException.class,
                () -> bankService.updateBack(null, request)
        );
    }

    @Test
    void updateBack_notFound_throwResourceNotFound() {

        when(bankRepository.findById(bankId))
                .thenReturn(Optional.empty());

        when(bankRepository.findByIdAndCode(bankId, "VCB"))
                .thenReturn(Collections.emptyList());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bankService.updateBack(bankId, request)
        );
    }

    @Test
    void updateBack_duplicateCode_throwConflict() {

        when(bankRepository.findByIdAndCode(bankId, "VCB"))
                .thenReturn(List.of(bank));

        assertThrows(
                ConflictDataException.class,
                () -> bankService.updateBack(bankId, request)
        );
    }

    @Test
    void updateBack_blankName_throwInvalid() {

        request.setName("");

        assertThrows(
                InvalidDataException.class,
                () -> bankService.updateBack(bankId, request)
        );
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    @Test
    void getById_success() {

        when(bankRepository.findById(bankId))
                .thenReturn(Optional.of(bank));

        BankDto result = bankService.getById(bankId);

        assertNotNull(result);
        assertEquals(bankId, result.getId());
    }

    @Test
    void getById_nullId_throwInvalid() {

        assertThrows(
                InvalidDataException.class,
                () -> bankService.getById(null)
        );
    }

    @Test
    void getById_notFound_throwResourceNotFound() {

        when(bankRepository.findById(bankId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bankService.getById(bankId)
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    @Test
    void deleteBack_success() {

        when(bankRepository.findById(bankId))
                .thenReturn(Optional.of(bank));

        bankService.deleteBack(bankId);

        verify(bankRepository, times(1)).delete(bank);
    }

    @Test
    void deleteBack_nullId_throwInvalid() {

        assertThrows(
                InvalidDataException.class,
                () -> bankService.deleteBack(null)
        );
    }

    @Test
    void deleteBack_notFound_throwResourceNotFound() {

        when(bankRepository.findById(bankId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bankService.deleteBack(bankId)
        );
    }

    // =====================================================
    // PAGING
    // =====================================================

    @Test
    void findAllBack_success() {

        SearchDto dto = new SearchDto();
        dto.setPageIndex(1);
        dto.setPageSize(10);

        Page<Bank> page = new PageImpl<>(List.of(bank));

        when(bankRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<BankDto> result = bankService.findAllBack(dto);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("VCB", result.getContent().get(0).getCode());
    }

    @Test
    void findAllBack_nullDto_success() {

        Page<Bank> page = new PageImpl<>(List.of(bank));

        when(bankRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<BankDto> result = bankService.findAllBack(null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findAllBack_pageIndexLessThan1_useDefault() {

        SearchDto dto = new SearchDto();
        dto.setPageIndex(0);
        dto.setPageSize(10);

        Page<Bank> page = new PageImpl<>(List.of(bank));

        when(bankRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<BankDto> result = bankService.findAllBack(dto);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void findAllBack_pageSizeInvalid_useDefault() {

        SearchDto dto = new SearchDto();
        dto.setPageIndex(1);
        dto.setPageSize(0);

        Page<Bank> page = new PageImpl<>(List.of(bank));

        when(bankRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        Page<BankDto> result = bankService.findAllBack(dto);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}