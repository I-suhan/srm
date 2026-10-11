package com.ne.srm.file.service.application;

import com.ne.srm.file.entity.SysFile;
import com.ne.srm.file.service.domain.FileDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/** 文件元数据应用用例入口。 */
@Service
@RequiredArgsConstructor
public class FileApplicationService {
    private final FileDomainService domainService;

    public List<SysFile> findAll() {
        return domainService.findAll();
    }

    public SysFile findById(Long id) {
        return domainService.findById(id);
    }

    public SysFile create(SysFile file) {
        file.setId(null);
        return domainService.save(file);
    }

    public SysFile update(Long id, SysFile file) {
        domainService.findById(id);
        file.setId(id);
        return domainService.save(file);
    }

    public void delete(Long id) {
        domainService.delete(id);
    }
}
