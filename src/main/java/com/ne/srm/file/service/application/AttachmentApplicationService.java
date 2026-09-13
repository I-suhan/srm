package com.ne.srm.file.service.application;

import com.ne.srm.file.service.domain.AttachmentDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentApplicationService {

    @Autowired
    private AttachmentDomainService attachmentDomainService;


}
