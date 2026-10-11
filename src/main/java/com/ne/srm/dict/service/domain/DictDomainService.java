package com.ne.srm.dict.service.domain;

import com.ne.srm.dict.entity.SysDictItem;
import com.ne.srm.dict.entity.SysDictType;
import com.ne.srm.dict.repository.SysDictItemRepository;
import com.ne.srm.dict.repository.SysDictTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/** 字典类型和字典项的领域查询及持久化操作。 */
@Service
@RequiredArgsConstructor
public class DictDomainService {
    private final SysDictItemRepository itemRepository;
    private final SysDictTypeRepository typeRepository;

    public List<SysDictItem> getItems(String code) {
        return itemRepository.findByDictCodeOrderBySortNo(code);
    }

    public SysDictItem getItem(String code, String itemCode) {
        return itemRepository.findByDictCodeAndItemCode(code, itemCode)
                .orElseThrow(() -> new EntityNotFoundException("Dictionary item not found"));
    }

    public List<SysDictType> getTypes() {
        return typeRepository.findAll();
    }

    public SysDictType getType(Long id) {
        return typeRepository.findById(id.intValue())
                .orElseThrow(() -> new EntityNotFoundException("Dictionary type not found: " + id));
    }

    public SysDictType saveType(SysDictType type) {
        return typeRepository.save(type);
    }

    public void deleteType(Long id) {
        if (!typeRepository.existsById(id.intValue()))
            throw new EntityNotFoundException("Dictionary type not found: " + id);
        typeRepository.deleteById(id.intValue());
    }

    public List<SysDictItem> getAllItems() {
        return itemRepository.findAll();
    }

    public SysDictItem getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dictionary item not found: " + id));
    }

    public SysDictItem saveItem(SysDictItem item) {
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id))
            throw new EntityNotFoundException("Dictionary item not found: " + id);
        itemRepository.deleteById(id);
    }
}
