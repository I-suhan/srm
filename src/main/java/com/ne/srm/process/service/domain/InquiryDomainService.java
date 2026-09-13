package com.ne.srm.process.service.domain;

import com.ne.srm.process.repository.InquiryRepository;
import com.ne.srm.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryDomainService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private ProductRepository productRepository;


}
