package com.fwtai.filter;

import com.fwtai.entity.JwtUser;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolJwt;
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
 * 登录认证
 * 登录认证用户名和密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法 ,
 * attemptAuthentication：接收并解析用户凭证。
 * successfulAuthentication：用户成功登录后，这个方法会被调用，我们在这个方法里生成token并返回。
*/
public final class LoginAuthFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;

    private final ToolClient client = new ToolClient();

    public LoginAuthFilter(final AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/loginAuth");//http://127.0.0.1:8090/loginAuth?password=admin&username=admin
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,final HttpServletResponse response) throws AuthenticationException{
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        if(username == null || username.isEmpty()){
            final String json = client.json(202,"请求参数不完整");
            client.responseJson(json,response);
            return null;
        }
        if(password == null || password.isEmpty()){
            final ToolClient client = new ToolClient();
            final String json = client.json(202,"请求参数不完整");
            client.responseJson(json,response);
            return null;
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }

    // 登录认证成功后调用
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(final HttpServletRequest request,final HttpServletResponse response,final FilterChain chain,final Authentication authResult) throws IOException, ServletException{
        final JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        System.out.println("jwtUser:" + jwtUser.getUsername()+"->"+jwtUser.getAuthorities());
        final ArrayList<String> roles = new ArrayList<String>();
        final Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for(GrantedAuthority authority : authorities){
            roles.add(authority.getAuthority());
        }
        final String token = TestJwtUtils.createToken(jwtUser.getUsername(),roles);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        response.setCharacterEncoding("UTF-8");
        final String tokenStr = ToolJwt.TOKEN_PREFIX + token;
        response.setHeader("token",tokenStr);
        final String json = client.json(200,"登录成功");
        client.responseJson(json,response);
    }

    //登录认证失败调用
    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request,final HttpServletResponse response,final AuthenticationException failed) throws IOException, ServletException{
        final String json = client.json(199,"认证失败,账号或密码错误,authentication failed, reason: " + failed.getMessage());
        client.responseJson(json,response);
    }
}