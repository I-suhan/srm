package com.ne.srm.company.repositoty;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.company.entity.CompanyEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends BaseRepository<CompanyEntity, Long> {

    Optional<CompanyEntity> findByCompanyCode(String companyCode);

    List<CompanyEntity> findByCompanyTypeCode(String companyTypeCode);

}
