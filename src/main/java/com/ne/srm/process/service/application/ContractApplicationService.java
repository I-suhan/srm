package com.ne.srm.process.service.application;

import com.ne.srm.process.service.domain.ContractDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractApplicationService {

    @Autowired
    private ContractDomainService contractDomainService;


}
