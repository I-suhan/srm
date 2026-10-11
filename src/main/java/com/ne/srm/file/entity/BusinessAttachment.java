package com.ne.srm.file.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class BusinessAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private SysFile sysFile;

    private String businessTypeCode;//CONTRACT / INVOICE / PRODUCT / PAYMENT / INQUIRY

    private Long businessId;

    private String attachmentCategoryCode;

    private String attachmentTypeCode;

    private String remark;

    private Long createTime;
    private Long updateTime;

}
