package com.ne.srm.file.repositoty;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.file.entity.SysFile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysFileRepository extends BaseRepository<SysFile, Long> {

    Optional<SysFile> findByFileMd5(String fileMd5);

}
