package com.ne.srm.process.service.domain;

import com.ne.srm.process.entity.InquiryEntity;
import com.ne.srm.process.repository.InquiryRepository;
import com.ne.srm.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryDomainService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<String> findAllInquiryNo() {
        return inquiryRepository.findAllInquiryNo();
    }

    public List<InquiryEntity> findByInquiryNum(String num) {
        return inquiryRepository.findByInquiryNo(num);
    }

    public List<InquiryEntity> findAll() {
        return inquiryRepository.findAll();
    }

    public InquiryEntity findById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inquiry not found: " + id));
    }

    public InquiryEntity save(InquiryEntity inquiry) {
        return inquiryRepository.save(inquiry);
    }

    public void delete(Long id) {
        if (!inquiryRepository.existsById(id))
            throw new EntityNotFoundException("Inquiry not found: " + id);
        inquiryRepository.deleteById(id);
    }
}
