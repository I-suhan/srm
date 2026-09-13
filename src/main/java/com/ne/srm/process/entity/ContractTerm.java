package com.ne.srm.process.entity;

import com.ne.srm.auth.entity.User;
import com.ne.srm.company.entity.CompanyEntity;
import jakarta.persistence.*;
import lombok.Data;

import static com.ne.srm.common.constant.BizConstant.DEFAULT_TERM;

@Data
@Entity
public class ContractTerm {

    @Id
    private Long id;

    private String term;

    @OneToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    private int version;

    private Long createdTime;

    private Long updatedTime;

    @OneToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    public ContractTerm create(){
        long time = System.currentTimeMillis();

        ContractTerm contractTerm = new ContractTerm();
        contractTerm.setTerm(DEFAULT_TERM);
        contractTerm.setCreatedTime(time);
        contractTerm.setUpdatedTime(time);
        return contractTerm;
    }

    // 临时修改条款
    public ContractTerm modify(ContractTerm term, String text){
        long time = System.currentTimeMillis();
        term.setTerm(text);
        //term.setUpdatedBy();
        term.setUpdatedTime(time);

        return term;
    }

    public ContractTerm update(ContractTerm term, String text){
        long time = System.currentTimeMillis();
        term.setTerm(text);
        term.setVersion(term.getVersion() + 1);
        term.setCreatedTime(time);
        term.setUpdatedTime(time);
        return term;
    }

    public ContractTerm() {}

}
