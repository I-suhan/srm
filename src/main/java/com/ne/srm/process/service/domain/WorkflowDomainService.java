package com.ne.srm.process.service.domain;

import com.ne.srm.process.repository.ContractRepository;
import com.ne.srm.process.repository.ContractStatusLogRepository;
import com.ne.srm.process.repository.OrderRepository;
import com.ne.srm.process.repository.StateTransitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowDomainService {

    @Autowired
    private StateTransitionRepository stateTransitionRepository;

    @Autowired
    private ContractStatusLogRepository contractStatusLogRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private OrderRepository orderRepository;
}
