package com.ne.srm.process.controller;

import com.ne.srm.process.entity.InquiryEntity;
import com.ne.srm.process.service.domain.InquiryDomainService;
import com.ne.srm.process.service.application.InquiryApplicationService;
import com.ne.srm.common.module.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryDomainService inquiryDomainService;
    private final InquiryApplicationService inquiryApplicationService;

    public InquiryController(InquiryDomainService inquiryDomainService, InquiryApplicationService inquiryApplicationService) {
        this.inquiryDomainService = inquiryDomainService;
        this.inquiryApplicationService = inquiryApplicationService;
    }

    /** 查询所有询价批次号。 */
    @GetMapping("/batches")
    public List<String> getAllInquiryNum() {
        return inquiryDomainService.findAllInquiryNo();
    }

    /** 根据询价批次号查询询价记录。 */
    @GetMapping("/getByBatch")
    public List<InquiryEntity> getAllInquiryByBatch(@RequestParam String num) {
        return inquiryDomainService.findByInquiryNum(num);
    }

    /** 查询询价记录列表。 */
    @GetMapping
    public Result list() { return Result.ok(inquiryApplicationService.findAll()); }

    /** 按主键查询询价详情。 */
    @GetMapping("/{id}")
    public Result get(@PathVariable Long id) { return Result.ok(inquiryApplicationService.findById(id)); }

    /** 新建询价记录。 */
    @PostMapping
    public Result create(@RequestBody InquiryEntity inquiry) { return Result.ok(inquiryApplicationService.create(inquiry)); }

    /** 全量更新询价记录。 */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody InquiryEntity inquiry) { return Result.ok(inquiryApplicationService.update(id, inquiry)); }

    /** 删除询价记录。 */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) { inquiryApplicationService.delete(id); return Result.ok(); }
}
