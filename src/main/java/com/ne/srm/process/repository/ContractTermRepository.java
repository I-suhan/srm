package com.ne.srm.process.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.process.entity.ContractTerm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractTermRepository extends BaseRepository<ContractTerm, Long> {

    List<ContractTerm> findByCompany_IdOrderByVersionDesc(Long companyId);

}
