package com.ne.srm.dict.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.dict.entity.SysDictType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysDictTypeRepository extends BaseRepository<SysDictType, Integer> {

    Optional<SysDictType> findByDictCode(String dictCode);

}
