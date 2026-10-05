package com.ne.srm.process.controller;

import com.ne.srm.process.entity.InquiryEntity;
import com.ne.srm.process.service.domain.InquiryDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryDomainService inquiryDomainService;

    public InquiryController(InquiryDomainService inquiryDomainService) {
        this.inquiryDomainService = inquiryDomainService;
    }

    @GetMapping("/batches")
    public List<String> getAllInquiryNum() {
        return inquiryDomainService.findAllInquiryNo();
    }

    @GetMapping("/getByBatch")
    public List<InquiryEntity> getAllInquiryByBatch(@RequestParam String num) {
        return inquiryDomainService.findByInquiryNum(num);
    }
}
