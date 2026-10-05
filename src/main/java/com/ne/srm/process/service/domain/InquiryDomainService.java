package com.ne.srm.process.service.domain;

import com.ne.srm.process.entity.InquiryEntity;
import com.ne.srm.process.repository.InquiryRepository;
import com.ne.srm.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryDomainService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<String> findAllInquiryNo(){
        return inquiryRepository.findAllInquiryNo();
    }


    public List<InquiryEntity> findByInquiryNum(String num) {
        return inquiryRepository.findByInquiryNo(num);
    }
}
