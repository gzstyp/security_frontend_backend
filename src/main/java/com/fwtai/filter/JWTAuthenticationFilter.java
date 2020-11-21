package com.fwtai.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fwtai.entity.JwtUser;
import com.fwtai.model.LoginUser;
import com.fwtai.utils.JwtTokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法 ,
 * attemptAuthentication：接收并解析用户凭证。
 * successfulAuthentication：用户成功登录后，这个方法会被调用，我们在这个方法里生成token并返回。
*/
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(final AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        super.setUsernameParameter("username");//若使用了request.getInputStream()，则不好使!!!
        super.setFilterProcessesUrl("/loginAuth");//http://127.0.0.1:8090/loginAuth?password=admin&username=admin
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,final HttpServletResponse response) throws AuthenticationException{
        // 从输入流中获取到登录的信息
        try{
            String username = request.getParameter("username");
            final LoginUser loginUser = new ObjectMapper().readValue(request.getInputStream(),LoginUser.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(),loginUser.getPassword()));
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,HttpServletResponse response,FilterChain chain,Authentication authResult) throws IOException, ServletException{
        final JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        System.out.println("jwtUser:" + jwtUser.toString());
        final ArrayList<String> roles = new ArrayList<String>();
        final Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for(GrantedAuthority authority : authorities){
            roles.add(authority.getAuthority());
        }
        final String token = TestJwtUtils.createToken(jwtUser.getUsername(),roles);
        //String token = JwtTokenUtils.createToken(jwtUser.getUsername(), false);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        final String tokenStr = JwtTokenUtils.TOKEN_PREFIX + token;
        response.setHeader("token",tokenStr);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,HttpServletResponse response,AuthenticationException failed) throws IOException, ServletException{
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}