package com.ne.srm.process.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.process.entity.ContractItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractItemRepository extends BaseRepository<ContractItem, Long> {

    List<ContractItem> findByContract_Id(Long contractId);

}
