package com.ne.srm.company.entity;

import com.ne.srm.common.module.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CompanyEntity extends BaseEntity {

    @Id
    private Long id;

    private String companyCode;

    private String companyName;

    private String companyTypeCode;

    private String address;

    private String phone;

    private String taxNo;

    //开户行名称
    private String bankName;

    //银行代码
    private String bankIdCode;

    //银行账户
    private String bankAccount;

    private Boolean exportLicense;

    private String shopName;

    private String shopContact;

}
