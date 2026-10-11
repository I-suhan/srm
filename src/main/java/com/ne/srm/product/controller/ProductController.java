package com.ne.srm.product.controller;

import com.ne.srm.common.module.Result;
import com.ne.srm.product.entity.ProductEntity;
import com.ne.srm.product.service.application.ProductApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductApplicationService service;
    /** 查询商品列表。 */
    @GetMapping public Result list() { return Result.ok(service.findAll()); }
    /** 查询商品详情。 */
    @GetMapping("/{id}") public Result get(@PathVariable Long id) { return Result.ok(service.findById(id)); }
    /** 新建商品，主键由数据库生成。 */
    @PostMapping public Result create(@RequestBody ProductEntity item) { return Result.ok(service.create(item)); }
    /** 全量更新指定商品。 */
    @PutMapping("/{id}") public Result update(@PathVariable Long id, @RequestBody ProductEntity item) { return Result.ok(service.update(id, item)); }
    /** 删除商品；有关联数据时可能被数据库约束拒绝。 */
    @DeleteMapping("/{id}") public Result delete(@PathVariable Long id) { service.delete(id); return Result.ok(); }
}
