package com.ne.srm.product.service.translation;

import java.util.Optional;

/** Translation provider boundary so the application is not coupled to one vendor. */
public interface TranslationPlugin {
    Optional<String> translate(String text, String sourceLanguage, String targetLanguage);
}
