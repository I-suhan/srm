package com.ne.srm.process.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.process.entity.ContractStatusLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractStatusLogRepository
        extends BaseRepository<ContractStatusLog, Long> {

    List<ContractStatusLog> findByContract_IdOrderByCreatedTimeDesc(
            Long contractId
    );

}
