package com.acong.ssoserver.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截异常并统一处理
 * @param
 * @作者 田应平
 * @QQ 444141300
 * @创建时间 2020/4/2 20:05
*/
@RestControllerAdvice
public class ExceptionMonitor{

    @ExceptionHandler(AccessDeniedException.class)
    public void accessDeniedException(final Exception exception,final HttpServletResponse response) throws IOException{
        System.out.println("方法accessDeniedException:"+exception.getClass());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write("权限不足");
    }

    @ExceptionHandler(Exception.class)
    public void exception(final Exception exception,final HttpServletResponse response)throws IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        exception.printStackTrace();
        System.out.println(exception.getClass());
        response.getWriter().write("系统出现异常");
    }
}