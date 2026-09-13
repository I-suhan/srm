package com.ne.srm.pay.service.application;

import com.ne.srm.pay.service.domain.PaymentDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentApplicationService {

    @Autowired
    private PaymentDomainService paymentDomainService;


}
