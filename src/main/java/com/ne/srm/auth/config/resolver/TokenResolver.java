package com.ne.srm.auth.config.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenResolver {

    String resolve(HttpServletRequest request);

    void remove(HttpServletResponse response);


}
