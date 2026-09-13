package com.ne.srm.process.service.application;

import com.ne.srm.process.service.domain.ReceiptDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptApplicationService {

    @Autowired
    private ReceiptDomainService receiptDomainService;


}
