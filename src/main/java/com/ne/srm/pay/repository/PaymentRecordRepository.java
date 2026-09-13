package com.ne.srm.pay.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.pay.entity.PaymentRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRecordRepository extends BaseRepository<PaymentRecord, Long> {

    List<PaymentRecord> findByContract_Id(Long contractId);

    Optional<PaymentRecord> findByPaymentNo(String paymentNo);

}
