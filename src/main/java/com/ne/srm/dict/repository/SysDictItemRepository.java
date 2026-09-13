package com.ne.srm.dict.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.dict.entity.SysDictItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysDictItemRepository extends BaseRepository<SysDictItem, Long> {

    List<SysDictItem> findByDictCodeOrderBySortNo(String dictCode);

    Optional<SysDictItem> findByDictCodeAndItemCode(
            String dictCode,
            String itemCode
    );

}
