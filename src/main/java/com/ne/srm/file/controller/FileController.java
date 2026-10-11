package com.ne.srm.file.controller;

import com.ne.srm.common.module.Result;
import com.ne.srm.file.entity.BusinessAttachment;
import com.ne.srm.file.entity.SysFile;
import com.ne.srm.file.service.application.AttachmentApplicationService;
import com.ne.srm.file.service.application.FileApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileApplicationService fileService;
    private final AttachmentApplicationService attachmentService;

    /** 查询文件元数据列表。 */
    @GetMapping
    public Result files() {
        return Result.ok(fileService.findAll());
    }

    /** 按主键查询文件元数据。 */
    @GetMapping("/{id}")
    public Result file(@PathVariable Long id) {
        return Result.ok(fileService.findById(id));
    }

    /** 登记文件元数据，不接收文件二进制内容。 */
    @PostMapping
    public Result createFile(@RequestBody SysFile file) {
        return Result.ok(fileService.create(file));
    }

    /** 更新文件元数据。 */
    @PutMapping("/{id}")
    public Result updateFile(@PathVariable Long id, @RequestBody SysFile file) {
        return Result.ok(fileService.update(id, file));
    }

    /** 删除文件元数据，不执行物理文件删除。 */
    @DeleteMapping("/{id}")
    public Result deleteFile(@PathVariable Long id) {
        fileService.delete(id);
        return Result.ok();
    }

    /** 查询附件；业务类型和业务 ID 必须同时传入或同时省略。 */
    @GetMapping("/attachments")
    public Result attachments(@RequestParam(required = false) String businessTypeCode,
            @RequestParam(required = false) Long businessId) {
        if ((businessTypeCode == null) != (businessId == null))
            throw new IllegalArgumentException("businessTypeCode and businessId must be provided together");
        return Result.ok(businessTypeCode == null ? attachmentService.findAll()
                : attachmentService.findByBusiness(businessTypeCode, businessId));
    }

    /** 按主键查询业务附件。 */
    @GetMapping("/attachments/{id}")
    public Result attachment(@PathVariable Long id) {
        return Result.ok(attachmentService.findById(id));
    }

    /** 创建业务对象与文件元数据之间的关联。 */
    @PostMapping("/attachments")
    public Result createAttachment(@RequestBody BusinessAttachment item) {
        return Result.ok(attachmentService.create(item));
    }

    /** 更新业务附件关联信息。 */
    @PutMapping("/attachments/{id}")
    public Result updateAttachment(@PathVariable Long id, @RequestBody BusinessAttachment item) {
        return Result.ok(attachmentService.update(id, item));
    }

    /** 删除附件关联，不删除文件元数据或物理文件。 */
    @DeleteMapping("/attachments/{id}")
    public Result deleteAttachment(@PathVariable Long id) {
        attachmentService.delete(id);
        return Result.ok();
    }
}
