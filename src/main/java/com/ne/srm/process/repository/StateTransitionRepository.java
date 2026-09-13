package com.ne.srm.process.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.process.entity.StateTransition;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateTransitionRepository
        extends BaseRepository<StateTransition, Long> {

    Optional<StateTransition>
    findByBusinessTypeCodeAndFromStatusAndToStatus(
            String businessTypeCode,
            String fromStatus,
            String toStatus
    );

    List<StateTransition>
    findByBusinessTypeCode(String businessTypeCode);

}
