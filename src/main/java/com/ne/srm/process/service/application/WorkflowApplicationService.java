package com.ne.srm.process.service.application;

import com.ne.srm.process.service.domain.WorkflowDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowApplicationService {

    @Autowired
    private WorkflowDomainService workflowDomainService;


}
