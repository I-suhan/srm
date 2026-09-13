package com.ne.srm.process.service.application;

import com.ne.srm.pay.repository.PaymentRecordRepository;
import com.ne.srm.process.repository.ContractRepository;
import com.ne.srm.process.repository.InquiryRepository;
import com.ne.srm.process.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardApplicationService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InquiryRepository inquiryRepository;


}
