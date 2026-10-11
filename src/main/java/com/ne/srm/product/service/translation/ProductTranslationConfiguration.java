package com.ne.srm.product.service.translation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import java.util.concurrent.Executor;

/** 翻译插件与商品异步任务线程池配置。 */
@Configuration
@EnableAsync
public class ProductTranslationConfiguration {
    @Bean
    TranslationPlugin translationPlugin(
            RestClient.Builder restClientBuilder,
            @Value("${translation.libretranslate.base-url:}") String baseUrl,
            @Value("${translation.libretranslate.api-key:}") String apiKey) {
        if (!StringUtils.hasText(baseUrl)) {
            // 未配置翻译服务时使用空实现，保留本地开发及既有调用的可用性。
            return (text, source, target) -> Optional.empty();
        }
        return new LibreTranslatePlugin(restClientBuilder.baseUrl(baseUrl).build(), apiKey);
    }

    @Bean("productTranslationExecutor")
    Executor productTranslationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("product-translation-");
        executor.initialize();
        return executor;
    }
}
