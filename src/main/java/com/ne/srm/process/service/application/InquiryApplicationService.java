package com.ne.srm.process.service.application;

import com.ne.srm.process.service.domain.InquiryDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryApplicationService {

    @Autowired
    private InquiryDomainService inquiryDomainService;


}
