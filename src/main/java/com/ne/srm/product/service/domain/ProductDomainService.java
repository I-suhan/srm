package com.ne.srm.product.service.domain;

import com.ne.srm.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDomainService {

    @Autowired
    private ProductRepository productRepository;


}
