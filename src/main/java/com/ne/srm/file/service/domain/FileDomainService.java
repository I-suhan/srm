package com.ne.srm.file.service.domain;

import com.ne.srm.file.entity.SysFile;
import com.ne.srm.file.repositoty.SysFileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/** 文件元数据的领域查询与持久化操作，不读写物理文件。 */
@Service
@RequiredArgsConstructor
public class FileDomainService {
    private final SysFileRepository repository;

    public List<SysFile> findAll() {
        return repository.findAll();
    }

    public SysFile findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("File metadata not found: " + id));
    }

    public SysFile save(SysFile file) {
        return repository.save(file);
    }

    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("File metadata not found: " + id);
        repository.deleteById(id);
    }
}
