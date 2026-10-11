package com.ne.srm.process.service.domain;

import com.ne.srm.process.entity.*;
import com.ne.srm.process.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/** 订单、合同、合同明细、条款及收货记录的领域持久化操作。 */
@Service
@RequiredArgsConstructor
public class ProcurementDomainService {
    private final OrderRepository orders;
    private final ContractRepository contracts;
    private final ContractItemRepository items;
    private final ContractTermRepository terms;
    private final GoodsReceiptRepository receipts;

    public List<OrderEntity> orders() {
        return orders.findAll();
    }

    public OrderEntity order(Long id) {
        return orders.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
    }

    public OrderEntity save(OrderEntity value) {
        return orders.save(value);
    }

    public void deleteOrder(Long id) {
        order(id);
        orders.deleteById(id);
    }

    public List<ContractEntity> contracts() {
        return contracts.findAll();
    }

    public ContractEntity contract(Long id) {
        return contracts.findById(id).orElseThrow(() -> new EntityNotFoundException("Contract not found: " + id));
    }

    public ContractEntity save(ContractEntity value) {
        return contracts.save(value);
    }

    public void deleteContract(Long id) {
        contract(id);
        contracts.deleteById(id);
    }

    public List<ContractItem> contractItems(Long contractId) {
        return items.findByContract_Id(contractId);
    }

    public List<ContractItem> allContractItems() {
        return items.findAll();
    }

    public ContractItem contractItem(Long id) {
        return items.findById(id).orElseThrow(() -> new EntityNotFoundException("Contract item not found: " + id));
    }

    public ContractItem save(ContractItem value) {
        return items.save(value);
    }

    public void deleteContractItem(Long id) {
        contractItem(id);
        items.deleteById(id);
    }

    public List<ContractTerm> terms() {
        return terms.findAll();
    }

    public ContractTerm term(Long id) {
        return terms.findById(id).orElseThrow(() -> new EntityNotFoundException("Contract term not found: " + id));
    }

    public ContractTerm save(ContractTerm value) {
        return terms.save(value);
    }

    public void deleteTerm(Long id) {
        term(id);
        terms.deleteById(id);
    }

    public List<GoodsReceipt> receipts(Long contractId) {
        return contractId == null ? receipts.findAll() : receipts.findByContract_Id(contractId);
    }

    public GoodsReceipt receipt(Long id) {
        return receipts.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("Goods receipt not found: " + id));
    }

    public GoodsReceipt save(GoodsReceipt value) {
        return receipts.save(value);
    }

    public void deleteReceipt(Long id) {
        receipt(id);
        receipts.deleteById(Math.toIntExact(id));
    }
}
