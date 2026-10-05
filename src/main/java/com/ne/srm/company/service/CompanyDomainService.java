package com.ne.srm.company.service;

import com.ne.srm.company.repositoty.CompanyRepository;
import com.ne.srm.company.repositoty.ContacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyDomainService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ContacterRepository contacterRepository;


}
