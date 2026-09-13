package com.ne.srm.company.entity;

import com.ne.srm.common.module.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * 联系人
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Contacter extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    //收件人信息
    private String name;

    private String phone;

    private String address;

    private String email;

    private String remark;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
}
