package com.ne.srm.company.service.application;

import com.ne.srm.company.service.domain.CompanyDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyApplicationService {

    @Autowired
    private CompanyDomainService companyDomainService;


}
