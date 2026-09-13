package com.ne.srm.auth.config.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name="security.token-mode",
        havingValue="header"
)
public class HeaderTokenResolver implements TokenResolver {

    @Override
    public String resolve(HttpServletRequest request){

        String header = request.getHeader("Authorization");

        if(header == null){
            return null;
        }

        if(!header.startsWith("Bearer ")){
            return null;
        }

        return header.substring(7);

    }

    @Override
    public void remove(HttpServletResponse response) {

    }

}
