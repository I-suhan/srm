package com.ne.srm.file.service.domain;

import com.ne.srm.file.repositoty.BusinessAttachmentRepository;
import com.ne.srm.file.repositoty.SysFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentDomainService {

    @Autowired
    private BusinessAttachmentRepository businessRepository;

    @Autowired
    private SysFileRepository sysFileRepository;


}
