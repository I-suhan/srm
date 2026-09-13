package com.ne.srm.process.service.domain;

import com.ne.srm.company.repositoty.ContacterRepository;
import com.ne.srm.process.repository.GoodsReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptDomainService {

    @Autowired
    private GoodsReceiptRepository goodsReceiptRepository;

    @Autowired
    private ContacterRepository contacterRepository;


}
