package com.ne.srm.file.service.domain;

import com.ne.srm.file.entity.BusinessAttachment;
import com.ne.srm.file.repositoty.BusinessAttachmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/** 业务附件关联的领域查询与持久化操作。 */
@Service
@RequiredArgsConstructor
public class AttachmentDomainService {
    private final BusinessAttachmentRepository repository;

    public List<BusinessAttachment> findAll() {
        return repository.findAll();
    }

    public List<BusinessAttachment> findByBusiness(String type, Long id) {
        return repository.findByBusinessTypeCodeAndBusinessId(type, id);
    }

    public BusinessAttachment findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Attachment not found: " + id));
    }

    public BusinessAttachment save(BusinessAttachment item) {
        return repository.save(item);
    }

    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Attachment not found: " + id);
        repository.deleteById(id);
    }
}
