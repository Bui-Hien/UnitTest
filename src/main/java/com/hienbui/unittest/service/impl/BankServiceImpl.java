package com.hienbui.unittest.service.impl;

import com.hienbui.unittest.domain.Bank;
import com.hienbui.unittest.dto.BankDto;
import com.hienbui.unittest.dto.request.BankRequest;
import com.hienbui.unittest.dto.search.SearchDto;
import com.hienbui.unittest.exception.ConflictDataException;
import com.hienbui.unittest.exception.InvalidDataException;
import com.hienbui.unittest.exception.ResourceNotFoundException;
import com.hienbui.unittest.repository.BankRepository;
import com.hienbui.unittest.service.BankService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private Validator validator;

    @Override
    public BankDto createBack(@Valid BankRequest dto) {
        validate(dto);
        validateRequest(null, dto);

        String code = dto.getCode().strip();

        Bank entity = new Bank();
        entity.setName(dto.getName());
        entity.setCode(code);
        entity.setDescription(dto.getDescription());

        entity = bankRepository.save(entity);

        return new BankDto(entity);
    }

    @Override
    public BankDto updateBack(UUID id, @Valid BankRequest dto) {
        validate(dto);
        if (id == null) {
            throw new InvalidDataException("Id là trường bắt buộc.");
        }

        validateRequest(id, dto);

        Bank entity = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin ngân hàng với id: " + id));

        String code = dto.getCode().strip();

        entity.setName(dto.getName());
        entity.setCode(code);
        entity.setDescription(dto.getDescription());

        entity = bankRepository.save(entity);

        return new BankDto(entity);
    }

    @Override
    public BankDto getById(UUID id) {
        if (id == null) {
            throw new InvalidDataException("Id là trường bắt buộc.");
        }

        Bank entity = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin ngân hàng với id: " + id));

        return new BankDto(entity);
    }

    @Override
    public void deleteBack(UUID id) {
        if (id == null) {
            throw new InvalidDataException("Id là trường bắt buộc.");
        }

        Bank entity = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin ngân hàng với id: " + id));

        bankRepository.delete(entity);
    }

    @Override
    public Page<BankDto> findAllBack(SearchDto dto) {

        if (dto == null) {
            dto = new SearchDto();
        }

        int pageIndex = dto.getPageIndex() != null && dto.getPageIndex() < 1 ? 0 : dto.getPageIndex() - 1;
        int pageSize = dto.getPageSize()!=null && dto.getPageSize() <= 0 ? 10 : dto.getPageSize();

        Sort sort = Sort.by(Sort.Direction.DESC, "code");

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        Page<Bank> page = bankRepository.findAll(pageable);

        return page.map(BankDto::new);
    }

    private void validate(Object dto) {
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new InvalidDataException(violations.iterator().next().getMessage());
        }
    }

    private void validateRequest(UUID id, BankRequest dto) {

        if (dto == null) {
            throw new InvalidDataException("Dữ liệu đầu vào không hợp lệ.");
        }

        String code = dto.getCode().strip();

        if (id == null) {
            if (!bankRepository.findByCode(code).isEmpty()) {
                throw new ConflictDataException("Mã ngân hàng đã được sử dụng.");
            }
        } else {
            if (!bankRepository.findByIdAndCode(id, code).isEmpty()) {
                throw new ConflictDataException("Mã ngân hàng đã được sử dụng.");
            }
        }
    }
}