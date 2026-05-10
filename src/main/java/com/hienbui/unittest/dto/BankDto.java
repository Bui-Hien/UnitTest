package com.hienbui.unittest.dto;

import com.hienbui.unittest.domain.Bank;

public class BankDto extends BaseObjectDto {
    public BankDto() {
    }

    public BankDto(Bank entity) {
        super(entity);
    }

    public BankDto(String code, String name, String description) {
        this.setCode(code);
        this.setName(name);
        this.setDescription(description);
    }
}
