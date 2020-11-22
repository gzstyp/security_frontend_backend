package com.fwtai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public void commence(final HttpServletRequest request,final HttpServletResponse response,final AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String reason = "统一处理,原因："+authException.getMessage();
        response.getWriter().write(new ObjectMapper().writeValueAsString(reason));
    }
}