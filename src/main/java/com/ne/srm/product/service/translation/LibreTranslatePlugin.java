package com.ne.srm.product.service.translation;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** LibreTranslate 翻译插件适配器。 */
public class LibreTranslatePlugin implements TranslationPlugin {
    private final RestClient client;
    private final String apiKey;

    public LibreTranslatePlugin(RestClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    @Override
    public Optional<String> translate(String text, String sourceLanguage, String targetLanguage) {
        if (StrUtil.isBlank(text)) {
            return Optional.empty();
        }
        Map<String, Object> request = new HashMap<>();
        request.put("q", text);
        request.put("source", sourceLanguage);
        request.put("target", targetLanguage);
        request.put("format", "text");
        if (StrUtil.isNotBlank(apiKey)) {
            request.put("api_key", apiKey);
        }
        TranslationResponse response = client.post()
                .uri("/translate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(TranslationResponse.class);
        if (response == null || StrUtil.isBlank(response.translatedText())) {
            throw new IllegalStateException("翻译服务未返回有效译文");
        }
        return Optional.of(response.translatedText());
    }

    /** LibreTranslate 的响应体。 */
    public record TranslationResponse(String translatedText) {}
}
