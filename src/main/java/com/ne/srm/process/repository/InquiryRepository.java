package com.ne.srm.process.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.process.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends BaseRepository<InquiryEntity, Long> {

    List<InquiryEntity> findByInquiryNo(String inquiryNo);

    List<InquiryEntity> findByStatusCode(int statusCode);

    /** 查询所有非空询价编号并去重，供批次选择器使用。 */
    @Query("select distinct i.inquiryNo from InquiryEntity i where i.inquiryNo is not null order by i.inquiryNo")
    List<String> findAllInquiryNo();

}
