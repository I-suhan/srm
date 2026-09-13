package com.ne.srm.product.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends BaseRepository<ProductEntity, Long> {

    Optional<ProductEntity> findBySku(String sku);

    Page<ProductEntity> findByNameZhContaining(
            String keyword,
            Pageable pageable
    );

}
