package com.ne.srm.company.repositoty;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.company.entity.Contacter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContacterRepository extends BaseRepository<Contacter, Long> {

    List<Contacter> findByCompany_Id(Long companyId);

}
