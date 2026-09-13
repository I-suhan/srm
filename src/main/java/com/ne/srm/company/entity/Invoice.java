package com.ne.srm.company.entity;

import com.ne.srm.common.module.BaseEntity;
import com.ne.srm.dict.entity.SysDictItem;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice extends BaseEntity {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_type_id")
    private SysDictItem invoiceCode;

    private Double invoiceExtra;

    private Double actualExtra;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

}
