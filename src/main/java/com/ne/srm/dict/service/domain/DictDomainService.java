package com.ne.srm.dict.service.domain;

import com.ne.srm.dict.entity.SysDictItem;
import com.ne.srm.dict.repository.SysDictItemRepository;
import com.ne.srm.dict.repository.SysDictTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictDomainService {

    private final SysDictItemRepository itemRepository;

    private final SysDictTypeRepository typeRepository;

    public List<SysDictItem> getItems(String dictCode) {
        return itemRepository.findByDictCodeOrderBySortNo(dictCode);
    }

    public SysDictItem getItem(String dictCode,String itemCode) {
        return itemRepository
                .findByDictCodeAndItemCode(dictCode,itemCode)
                .orElseThrow();
    }
}
