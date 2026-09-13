package com.ne.srm.process.service.domain;

import com.ne.srm.company.repositoty.CompanyRepository;
import com.ne.srm.company.repositoty.ContacterRepository;
import com.ne.srm.process.repository.ContractItemRepository;
import com.ne.srm.process.repository.ContractTermRepository;
import com.ne.srm.process.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractDomainService {

    @Autowired
    private ContacterRepository contacterRepository;

    @Autowired
    private ContractItemRepository contractItemRepository;

    @Autowired
    private ContractTermRepository contractTermRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CompanyRepository companyRepository;


}
