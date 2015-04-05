package com.iblagojevic.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * simple POJO representing User of the trading system
 * User has and ID and a bank account number to which the money is transferred
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 8582670339658611416L;
    private Long id;
    private String bankAccountNo;

    public User() {}

    public User(Long id, String bankAccountNo) {
        this.id = id;
        this.bankAccountNo = bankAccountNo;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "bank_account_no", nullable = true, length = 100)
    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }
}
