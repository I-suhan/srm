package com.ne.srm.product.service.application;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.ne.srm.product.service.translation.ProductTranslationService;

import cn.hutool.core.bean.BeanUtil;
import com.ne.srm.product.entity.ProductEntity;
import com.ne.srm.product.service.domain.ProductDomainService;
import com.ne.srm.product.service.translation.ProductTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/** 商品应用用例入口，负责组织商品 CRUD 操作。 */
@Service
@RequiredArgsConstructor
public class ProductApplicationService {
    private final ProductDomainService domainService;
    private final ProductTranslationService translationService;

    public List<ProductEntity> findAll() {
        return domainService.findAll();
    }

    public ProductEntity findById(Long id) {
        return domainService.findById(id);
    }

    public ProductEntity create(ProductEntity entity) {
        entity.setId(null);
        return translationService.translateAndSave(entity).join();
    }

    public ProductEntity update(Long id, ProductEntity entity) {
        ProductEntity dest = domainService.findById(id);
        entity.setId(id);
        BeanUtil.copyProperties(entity, dest, "id");
        return domainService.save(dest);
    }

    public void delete(Long id) {
        domainService.delete(id);
    }
}
