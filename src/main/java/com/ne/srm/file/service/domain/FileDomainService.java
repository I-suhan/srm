package com.ne.srm.file.service.domain;

import com.ne.srm.file.repositoty.SysFileRepository;
import jakarta.persistence.Access;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileDomainService {

    @Autowired
    private SysFileRepository sysFileRepository;


}
