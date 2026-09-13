package com.ne.srm.process.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.process.entity.ContractEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends BaseRepository<ContractEntity, Long> {

    Optional<ContractEntity> findByContractNo(String contractNo);

    List<ContractEntity> findByContractStatusCode(String statusCode);

    List<ContractEntity> findByBuyerCompany_Id(Long companyId);

    List<ContractEntity> findBySupplierCompany_Id(Long companyId);

}