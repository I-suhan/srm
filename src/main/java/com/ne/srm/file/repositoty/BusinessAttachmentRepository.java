package com.ne.srm.file.repositoty;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.file.entity.BusinessAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessAttachmentRepository
        extends BaseRepository<BusinessAttachment, Long> {

    List<BusinessAttachment> findByBusinessTypeCodeAndBusinessId(
            String businessTypeCode,
            Long businessId
    );

    List<BusinessAttachment> findByAttachmentCategoryCode(
            String categoryCode
    );

    List<BusinessAttachment> findByAttachmentTypeCode(
            String attachmentTypeCode
    );

}
