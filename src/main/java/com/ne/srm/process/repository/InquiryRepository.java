package com.ne.srm.process.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.process.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends BaseRepository<InquiryEntity, Long> {

    Optional<InquiryEntity> findByInquiryNo(String inquiryNo);

    List<InquiryEntity> findByStatusCode(int statusCode);

}
