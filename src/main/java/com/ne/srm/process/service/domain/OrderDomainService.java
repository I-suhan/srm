package com.ne.srm.process.service.domain;

import com.ne.srm.process.repository.InquiryRepository;
import com.ne.srm.process.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDomainService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InquiryRepository inquiryRepository;


}
