package com.ne.srm.file.service.application;

import com.ne.srm.file.entity.BusinessAttachment;
import com.ne.srm.file.service.domain.AttachmentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/** 业务附件应用用例入口。 */
@Service
@RequiredArgsConstructor
public class AttachmentApplicationService {
    private final AttachmentDomainService domainService;

    public List<BusinessAttachment> findAll() {
        return domainService.findAll();
    }

    public List<BusinessAttachment> findByBusiness(String type, Long id) {
        return domainService.findByBusiness(type, id);
    }

    public BusinessAttachment findById(Long id) {
        return domainService.findById(id);
    }

    public BusinessAttachment create(BusinessAttachment item) {
        item.setId(null);
        return domainService.save(item);
    }

    public BusinessAttachment update(Long id, BusinessAttachment item) {
        domainService.findById(id);
        item.setId(id);
        return domainService.save(item);
    }

    public void delete(Long id) {
        domainService.delete(id);
    }
}
