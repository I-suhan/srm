package com.ne.srm.process.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.process.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends BaseRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderNo(String orderNo);

    List<OrderEntity> findByStatusCode(String statusCode);

}
