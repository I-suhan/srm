package com.ne.srm.product.service.application;

import com.ne.srm.product.service.domain.ProductDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductApplicationService {

    @Autowired
    private ProductDomainService productDomainService;


}
