package com.ne.srm.product.service.translation;

import cn.hutool.core.util.StrUtil;
import com.ne.srm.product.entity.ProductEntity;
import com.ne.srm.product.service.domain.ProductDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/** 在独立线程中补齐商品名称和描述的多语言内容并持久化。 */
@Service
@RequiredArgsConstructor
public class ProductTranslationService {
    private final TranslationPlugin translationPlugin;
    private final ProductDomainService domainService;

    /** 翻译尚未填写的语言字段；全部完成后才写入数据库。 */
    @Async("productTranslationExecutor")
    public CompletableFuture<ProductEntity> translateAndSave(ProductEntity product) {
        translateFields(product, "nameZh", "namePl");
        translateFields(product, "descriptionZh", "descriptionPl");
        return CompletableFuture.completedFuture(domainService.save(product));
    }

    private void translateFields(ProductEntity product, String zhField, String plField) {
        String zh = value(product, zhField);
        String pl = value(product, plField);
        String sourceLanguage;
        String sourceText;
        if (StrUtil.isNotBlank(zh)) {
            sourceLanguage = "zh";
            sourceText = zh;
        } else if (StrUtil.isNotBlank(pl)) {
            sourceLanguage = "pl";
            sourceText = pl;
        } else {
            return;
        }

        if (StrUtil.isBlank(zh)) {
            put(product, zhField, translated(sourceText, sourceLanguage, "zh"));
        }
        if (StrUtil.isBlank(pl)) {
            put(product, plField, translated(sourceText, sourceLanguage, "pl"));
        }
    }

    private String translated(String text, String source, String target) {
        if (source.equals(target)) {
            return text;
        }
        return translationPlugin.translate(text, source, target).orElse(text);
    }

    private String value(ProductEntity product, String field) {
        return switch (field) {
            case "nameZh" -> product.getNameZh();
            case "namePl" -> product.getNamePl();
            case "descriptionZh" -> product.getDescriptionZh();
            case "descriptionPl" -> product.getDescriptionPl();
            default -> throw new IllegalArgumentException("Unsupported field: " + field);
        };
    }

    private void put(ProductEntity product, String field, String text) {
        switch (field) {
            case "nameZh" -> product.setNameZh(text);
            case "namePl" -> product.setNamePl(text);
            case "descriptionZh" -> product.setDescriptionZh(text);
            case "descriptionPl" -> product.setDescriptionPl(text);
            default -> throw new IllegalArgumentException("Unsupported field: " + field);
        }
    }
}
