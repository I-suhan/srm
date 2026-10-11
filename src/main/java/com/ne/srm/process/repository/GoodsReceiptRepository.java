package com.ne.srm.process.repository;

import com.ne.srm.common.module.BaseRepository;
import com.ne.srm.process.entity.GoodsReceipt;
import java.util.List;

public interface GoodsReceiptRepository extends BaseRepository<GoodsReceipt, Integer> {
    /** 按合同主键查询收货记录。 */
    List<GoodsReceipt> findByContract_Id(Long contractId);

}
