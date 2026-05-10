package com.hienbui.unittest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BankRequest {

    @NotBlank(message = "Mã ngân hàng không được bỏ trống.")
    @Size(max = 50, message = "Mã ngân hàng tối đa 50 ký tự.")
    private String code;

    @NotBlank(message = "Tên ngân hàng không được bỏ trống.")
    @Size(max = 255, message = "Tên ngân hàng tối đa 255 ký tự.")
    private String name;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự.")
    private String description;

    public BankRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}