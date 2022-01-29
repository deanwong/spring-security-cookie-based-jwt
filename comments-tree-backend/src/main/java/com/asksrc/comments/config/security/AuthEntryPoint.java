package com.asksrc.comments.config.security;

import com.asksrc.comments.config.error.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
/**
 * @Author wangding
 * @Date 2022/1/24
 * @Description handle unauthorized exception thrown by spring security
 */
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Unauthorized", authException);
        log.warn("Unauthorized error: {}", apiError);
        jackson2ObjectMapperBuilder.build().writeValue(response.getWriter(), apiError);
    }

}
