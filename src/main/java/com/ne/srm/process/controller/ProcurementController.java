package com.ne.srm.process.controller;

import com.ne.srm.common.module.Result;
import com.ne.srm.process.entity.*;
import com.ne.srm.process.service.application.ProcurementApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 采购流程基础资料接口；写入接口当前按实体字段直接新增或全量更新。 */
@RestController
@RequiredArgsConstructor
public class ProcurementController {
    private final ProcurementApplicationService service;

    /** 订单列表。 */
    @GetMapping("/orders")
    public Result orders() {
        return Result.ok(service.orders());
    }

    @GetMapping("/orders/{id}")
    public Result order(@PathVariable Long id) {
        return Result.ok(service.order(id));
    }

    /** 新建订单，订单 ID 由数据库生成。 */
    @PostMapping("/orders")
    public Result createOrder(@RequestBody OrderEntity item) {
        return Result.ok(service.create(item));
    }

    /** 全量更新订单。 */
    @PutMapping("/orders/{id}")
    public Result updateOrder(@PathVariable Long id, @RequestBody OrderEntity item) {
        return Result.ok(service.updateOrder(id, item));
    }

    /** 删除订单；存在合同引用时可能受数据库约束限制。 */
    @DeleteMapping("/orders/{id}")
    public Result deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
        return Result.ok();
    }

    /** 合同列表。 */
    @GetMapping("/contracts")
    public Result contracts() {
        return Result.ok(service.contracts());
    }

    @GetMapping("/contracts/{id}")
    public Result contract(@PathVariable Long id) {
        return Result.ok(service.contract(id));
    }

    /** 新建合同。 */
    @PostMapping("/contracts")
    public Result createContract(@RequestBody ContractEntity item) {
        return Result.ok(service.create(item));
    }

    /** 全量更新合同。 */
    @PutMapping("/contracts/{id}")
    public Result updateContract(@PathVariable Long id, @RequestBody ContractEntity item) {
        return Result.ok(service.updateContract(id, item));
    }

    /** 删除合同；存在明细或收货记录时可能受数据库约束限制。 */
    @DeleteMapping("/contracts/{id}")
    public Result deleteContract(@PathVariable Long id) {
        service.deleteContract(id);
        return Result.ok();
    }

    /** 查询合同明细，可传 contractId 过滤。 */
    @GetMapping("/contract-items")
    public Result items(@RequestParam(required = false) Long contractId) {
        return Result.ok(service.items(contractId));
    }

    @GetMapping("/contract-items/{id}")
    public Result item(@PathVariable Long id) {
        return Result.ok(service.item(id));
    }

    /** 新建合同明细。 */
    @PostMapping("/contract-items")
    public Result createItem(@RequestBody ContractItem item) {
        return Result.ok(service.create(item));
    }

    /** 更新合同明细。 */
    @PutMapping("/contract-items/{id}")
    public Result updateItem(@PathVariable Long id, @RequestBody ContractItem item) {
        return Result.ok(service.updateItem(id, item));
    }

    /** 删除合同明细。 */
    @DeleteMapping("/contract-items/{id}")
    public Result deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
        return Result.ok();
    }

    /** 查询合同条款列表。 */
    @GetMapping("/contract-terms")
    public Result terms() {
        return Result.ok(service.terms());
    }

    @GetMapping("/contract-terms/{id}")
    public Result term(@PathVariable Long id) {
        return Result.ok(service.term(id));
    }

    /** 新建合同条款。 */
    @PostMapping("/contract-terms")
    public Result createTerm(@RequestBody ContractTerm term) {
        return Result.ok(service.create(term));
    }

    /** 更新合同条款。 */
    @PutMapping("/contract-terms/{id}")
    public Result updateTerm(@PathVariable Long id, @RequestBody ContractTerm term) {
        return Result.ok(service.updateTerm(id, term));
    }

    /** 删除合同条款。 */
    @DeleteMapping("/contract-terms/{id}")
    public Result deleteTerm(@PathVariable Long id) {
        service.deleteTerm(id);
        return Result.ok();
    }

    /** 查询收货记录，可传 contractId 过滤。 */
    @GetMapping("/receipts")
    public Result receipts(@RequestParam(required = false) Long contractId) {
        return Result.ok(service.receipts(contractId));
    }

    @GetMapping("/receipts/{id}")
    public Result receipt(@PathVariable Long id) {
        return Result.ok(service.receipt(id));
    }

    /** 登记收货记录。 */
    @PostMapping("/receipts")
    public Result createReceipt(@RequestBody GoodsReceipt receipt) {
        return Result.ok(service.create(receipt));
    }

    /** 更新收货记录。 */
    @PutMapping("/receipts/{id}")
    public Result updateReceipt(@PathVariable Long id, @RequestBody GoodsReceipt receipt) {
        return Result.ok(service.updateReceipt(id, receipt));
    }

    /** 删除收货记录。 */
    @DeleteMapping("/receipts/{id}")
    public Result deleteReceipt(@PathVariable Long id) {
        service.deleteReceipt(id);
        return Result.ok();
    }
}
