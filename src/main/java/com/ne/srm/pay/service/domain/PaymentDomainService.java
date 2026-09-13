package com.ne.srm.pay.service.domain;

import com.ne.srm.company.repositoty.ContacterRepository;
import com.ne.srm.pay.repository.PaymentRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDomainService {

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Autowired
    private ContacterRepository contacterRepository;


}
