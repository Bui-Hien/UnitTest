package com.hienbui.unittest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Entity đại diện cho thông tin **Bank** (ngân hàng).
 * *
 *
 * @author buixuanhienmy@gmail.com
 */
@Table(
        name = "tbl_bank",
        indexes = {
                @Index(name = "idx_bank_code_voided", columnList = "code, voided")
        }
)
@Entity
public class Bank extends BaseObject {
    public Bank() {
        super();
    }
}
