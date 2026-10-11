package com.ne.srm.dict.controller;

import com.ne.srm.common.module.Result;
import com.ne.srm.dict.entity.SysDictItem;
import com.ne.srm.dict.entity.SysDictType;
import com.ne.srm.dict.service.application.DictApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class DictController {
    private final DictApplicationService service;

    /** 查询全部字典类型。 */
    @GetMapping("/types")
    public Result types() {
        return Result.ok(service.getTypes());
    }

    /** 按主键查询字典类型。 */
    @GetMapping("/types/{id}")
    public Result type(@PathVariable Long id) {
        return Result.ok(service.getType(id));
    }

    /** 新建字典类型。 */
    @PostMapping("/types")
    public Result createType(@RequestBody SysDictType item) {
        return Result.ok(service.createType(item));
    }

    /** 更新字典类型。 */
    @PutMapping("/types/{id}")
    public Result updateType(@PathVariable Long id, @RequestBody SysDictType item) {
        return Result.ok(service.updateType(id, item));
    }

    /** 删除字典类型。 */
    @DeleteMapping("/types/{id}")
    public Result deleteType(@PathVariable Long id) {
        service.deleteType(id);
        return Result.ok();
    }

    /** 查询字典项；可选传入 dictCode 过滤。 */
    @GetMapping("/items")
    public Result items(@RequestParam(required = false) String dictCode) {
        return Result.ok(service.getItems(dictCode));
    }

    /** 按主键查询字典项。 */
    @GetMapping("/items/{id}")
    public Result item(@PathVariable Long id) {
        return Result.ok(service.getItem(id));
    }

    /** 按字典编码和项编码精确查询。 */
    @GetMapping("/items/by-code")
    public Result item(@RequestParam String dictCode, @RequestParam String itemCode) {
        return Result.ok(service.getItem(dictCode, itemCode));
    }

    /** 新建字典项。 */
    @PostMapping("/items")
    public Result createItem(@RequestBody SysDictItem item) {
        return Result.ok(service.createItem(item));
    }

    /** 更新字典项。 */
    @PutMapping("/items/{id}")
    public Result updateItem(@PathVariable Long id, @RequestBody SysDictItem item) {
        return Result.ok(service.updateItem(id, item));
    }

    /** 删除字典项。 */
    @DeleteMapping("/items/{id}")
    public Result deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
        return Result.ok();
    }
}
