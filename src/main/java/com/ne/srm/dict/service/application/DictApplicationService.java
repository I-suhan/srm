package com.ne.srm.dict.service.application;

import com.ne.srm.dict.entity.SysDictItem;
import com.ne.srm.dict.entity.SysDictType;
import com.ne.srm.dict.service.domain.DictDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/** 字典应用用例入口。 */
@Service
@RequiredArgsConstructor
public class DictApplicationService {
    private final DictDomainService domainService;

    public List<SysDictType> getTypes() {
        return domainService.getTypes();
    }

    public SysDictType getType(Long id) {
        return domainService.getType(id);
    }

    public SysDictType createType(SysDictType item) {
        return domainService.saveType(item);
    }

    public SysDictType updateType(Long id, SysDictType item) {
        domainService.getType(id);
        item.setId(id.intValue());
        return domainService.saveType(item);
    }

    public void deleteType(Long id) {
        domainService.deleteType(id);
    }

    public List<SysDictItem> getItems(String code) {
        return code == null ? domainService.getAllItems() : domainService.getItems(code);
    }

    public SysDictItem getItem(String code, String itemCode) {
        return domainService.getItem(code, itemCode);
    }

    public SysDictItem getItem(Long id) {
        return domainService.getItem(id);
    }

    public SysDictItem createItem(SysDictItem item) {
        return domainService.saveItem(item);
    }

    public SysDictItem updateItem(Long id, SysDictItem item) {
        domainService.getItem(id);
        item.setId(id);
        return domainService.saveItem(item);
    }

    public void deleteItem(Long id) {
        domainService.deleteItem(id);
    }
}
