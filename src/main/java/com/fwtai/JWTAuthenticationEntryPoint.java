package com.fwtai;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * 未登录认证处理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/11/23 23:42
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public void commence(final HttpServletRequest request,final HttpServletResponse response,final AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);//403,服务器拒绝请求,当然也可以是200前端要注意处理
        final Locale locale = request.getLocale();
        final String language = locale.getLanguage();
        final String country = locale.getCountry();
        final String msg = "统一处理,需要认证才能访问-无权限操作";
        System.out.println(msg);
        final String uri = request.getRequestURI();
        System.out.println(uri);
        if(language.equalsIgnoreCase("zh") || country.equalsIgnoreCase("CN")){
            System.out.println(11111111);
            response.getWriter().write(msg);
        }else{
            System.out.println(222222222);
            response.getWriter().write("统一处理,"+authException.getMessage());
        }
    }
}