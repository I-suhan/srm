package com.ne.srm.process.service.application;

import com.ne.srm.process.entity.*;
import com.ne.srm.process.service.domain.ProcurementDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/** 采购流程基础 CRUD 用例入口；当前不执行状态机或审批规则。 */
@Service
@RequiredArgsConstructor
public class ProcurementApplicationService {
    private final ProcurementDomainService domain;

    public List<OrderEntity> orders() {
        return domain.orders();
    }

    public OrderEntity order(Long id) {
        return domain.order(id);
    }

    public OrderEntity create(OrderEntity value) {
        value.setId(null);
        return domain.save(value);
    }

    public OrderEntity updateOrder(Long id, OrderEntity value) {
        domain.order(id);
        value.setId(id);
        return domain.save(value);
    }

    public void deleteOrder(Long id) {
        domain.deleteOrder(id);
    }

    public List<ContractEntity> contracts() {
        return domain.contracts();
    }

    public ContractEntity contract(Long id) {
        return domain.contract(id);
    }

    public ContractEntity create(ContractEntity value) {
        value.setId(null);
        return domain.save(value);
    }

    public ContractEntity updateContract(Long id, ContractEntity value) {
        domain.contract(id);
        value.setId(id);
        return domain.save(value);
    }

    public void deleteContract(Long id) {
        domain.deleteContract(id);
    }

    public List<ContractItem> items(Long contractId) {
        return contractId == null ? domain.allContractItems() : domain.contractItems(contractId);
    }

    public ContractItem item(Long id) {
        return domain.contractItem(id);
    }

    public ContractItem create(ContractItem value) {
        value.setId(null);
        return domain.save(value);
    }

    public ContractItem updateItem(Long id, ContractItem value) {
        domain.contractItem(id);
        value.setId(id);
        return domain.save(value);
    }

    public void deleteItem(Long id) {
        domain.deleteContractItem(id);
    }

    public List<ContractTerm> terms() {
        return domain.terms();
    }

    public ContractTerm term(Long id) {
        return domain.term(id);
    }

    public ContractTerm create(ContractTerm value) {
        value.setId(null);
        return domain.save(value);
    }

    public ContractTerm updateTerm(Long id, ContractTerm value) {
        domain.term(id);
        value.setId(id);
        return domain.save(value);
    }

    public void deleteTerm(Long id) {
        domain.deleteTerm(id);
    }

    public List<GoodsReceipt> receipts(Long contractId) {
        return domain.receipts(contractId);
    }

    public GoodsReceipt receipt(Long id) {
        return domain.receipt(id);
    }

    public GoodsReceipt create(GoodsReceipt value) {
        value.setId(0);
        return domain.save(value);
    }

    public GoodsReceipt updateReceipt(Long id, GoodsReceipt value) {
        domain.receipt(id);
        value.setId(Math.toIntExact(id));
        return domain.save(value);
    }

    public void deleteReceipt(Long id) {
        domain.deleteReceipt(id);
    }
}
