package com.ne.srm.file.service.application;

import com.ne.srm.file.service.domain.FileDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileApplicationService {

    @Autowired
    private FileDomainService fileDomainService;


}
