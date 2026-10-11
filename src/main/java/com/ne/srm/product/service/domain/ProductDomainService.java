package com.ne.srm.product.service.domain;

import com.ne.srm.product.entity.ProductEntity;
import com.ne.srm.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/** 商品领域查询与持久化操作。 */
@Service
@RequiredArgsConstructor
public class ProductDomainService {
    private final ProductRepository productRepository;

    public List<ProductEntity> findAll() {
        return productRepository.findAll();
    }

    public ProductEntity findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    public ProductEntity save(ProductEntity product) {

        return productRepository.save(product);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id))
            throw new EntityNotFoundException("Product not found: " + id);
        productRepository.deleteById(id);
    }
}
