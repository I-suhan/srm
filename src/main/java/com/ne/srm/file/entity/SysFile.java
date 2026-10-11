package com.ne.srm.file.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SysFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;//文件名
    private String filePath;//文件路径

    private Long fileSize;//文件大小

    private String contentType;//文件类型 pdf/doc...

    private String fileMd5;//文件md5

    private Long createTime;

    private String createBy;

}
