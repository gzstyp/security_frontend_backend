package com.fwtai.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 验证成功当然就是进行鉴权,鉴权操作,无token时会提示'统一处理,需要认证才能访问'
 * 登录成功之后走此类进行鉴权操作
*/
public final class JWTAuthorizationFilter extends BasicAuthenticationFilter{

    public JWTAuthorizationFilter(final AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,final HttpServletResponse response,final FilterChain chain) throws IOException, ServletException{
        final String tokenHeader = request.getHeader(TestJwtUtils.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if(tokenHeader == null || !tokenHeader.startsWith(TestJwtUtils.TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request,response,chain);
    }

    // 这里从token中获取用户信息并新建一个token
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader){
        final String token = tokenHeader.replace(TestJwtUtils.TOKEN_PREFIX,"");
        final String username = TestJwtUtils.getUsername(token);
        final List<String> authorities = TestJwtUtils.getUserRole(token);
        if(username != null && authorities != null){
            return new UsernamePasswordAuthenticationToken(username,null,getAuthorities(authorities));
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final List<String> authorities){
        return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());
    }
}