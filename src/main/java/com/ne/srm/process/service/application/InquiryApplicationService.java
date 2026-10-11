package com.ne.srm.process.service.application;

import com.ne.srm.process.service.domain.InquiryDomainService;
import com.ne.srm.process.entity.InquiryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InquiryApplicationService {

    @Autowired
    private InquiryDomainService inquiryDomainService;

    public List<InquiryEntity> findAll() {
        return inquiryDomainService.findAll();
    }

    public InquiryEntity findById(Long id) {
        return inquiryDomainService.findById(id);
    }

    public InquiryEntity create(InquiryEntity item) {
        item.setId(null);
        return inquiryDomainService.save(item);
    }

    public InquiryEntity update(Long id, InquiryEntity item) {
        inquiryDomainService.findById(id);
        item.setId(id);
        return inquiryDomainService.save(item);
    }

    public void delete(Long id) {
        inquiryDomainService.delete(id);
    }

}
