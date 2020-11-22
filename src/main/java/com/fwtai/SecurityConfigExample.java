package com.fwtai;
/*
import com.fwtai.tool.ToolClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.annotation.Resource;
import java.util.Collection;

//示例代码,请无删除
@Configuration
public class SecurityConfigExample extends WebSecurityConfigurerAdapter{

    @Resource
    private ToolClient client;

    @Bean
    protected PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    //角色继承,即admin角色拥有user的角色,RoleHierarchy是个接口
    @Bean
    protected RoleHierarchy roleHierarchy() {
        final RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return hierarchy;
    }

    @Override
    public void configure(final WebSecurity web) throws Exception{
        web.ignoring()//忽略
        .antMatchers("/css/**","/images/**");
    }

    */
/*//*
/基于内存数据库
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
        .withUser("root").password("123").roles("admin")
        .and().withUser("user").password("123").roles("user");
    }*//*


    //基于内存数据库,它和上面的是一样的!!!
    @Override
    @Bean
    protected UserDetailsService userDetailsService(){
        final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("root").password("123").roles("admin").build());//角色为 root 的授权操作
        manager.createUser(User.withUsername("user").password("123").roles("user").build());//角色为 user 的授权操作
        return manager;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception{
        http.authorizeRequests()//开启认证
        .antMatchers("/admin/**").hasRole("admin")//配置拦截规则,即拥有admin角色的才能访问以/admin/开头的接口
        .antMatchers("/user/**").hasRole("user")//配置拦截规则,即拥有user角色的才能访问以/user/开头的接口
        .anyRequest()//任何请求
        .authenticated()//登录认证之后才能访问,注意配置顺序,顺序很重要,它和shiro是一样的
        .and()//此时又回到上面第1行的 'authorizeRequests()'
        .formLogin()//表单登录
        .loginProcessingUrl("/loginAuth")//post请求:http://192.168.3.108:88/loginAuth
        .usernameParameter("userName")//请求时登录账号参数名,即表单的name
        .passwordParameter("passWord")//登录密码参数名
        .loginPage("/login.html")//指定登录页面的url[若没有额外的指定的话默认的登录接口就是它,只是页面的get请求,而登录接口是post请求]

        //成功的回调,request可以客户端跳转[重定向];response可以服务器端跳转,当然可以返回json;authentication包含登录成功的数据信息;此时不会跳转了,要跳转只需在客户端做处理即可;
        .successHandler((request,response,authentication)->{// 它是接口 org.springframework.security.web.authentication.AuthenticationSuccessHandler 的内部类实现
            final Object principal = authentication.getPrincipal();//用户信息
            final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();//权限和角色信息
            final Object details = authentication.getDetails();
            final String json = client.queryJson(principal);
            client.responseJson(json,response);//返回:{"msg":"操作成功","data":{"accountNonExpired":true,"accountNonLocked":true,"authorities":[{"authority":"ROLE_admin"}],"credentialsNonExpired":true,"enabled":true,"username":"root"},"code":200}
        })
        //.successForwardUrl("/succeed")//登录成功的跳转url,其实它的服务端跳转,即URL路径不变,它是post请求方式;它有个特点就是不管你哪一个url页面过来，登录成功后都会跳转到/succeed
        //.defaultSuccessUrl("/success")//登录成功的重定向,它是重定向!!!,不是跳转,它会在登录成功后重定向到之前跳转过来的url页面,说明它会记录重定向的url地址;它会从哪里来就跳转哪里去,若在登录页面来的它会跳转到/success去;是get请求;它和successForwardUrl()不能同时使用,即只能二选一
        //.defaultSuccessUrl("/success",true)//这个重载的方法会和successForwardUrl一样的效果;

        //失败的回调,request可以客户端跳转[重定向];response可以服务器端跳转,当然可以返回json;authenticationException包含登录失败的数据信息;此时不会跳转了,要跳转只需在客户端做处理即可;
        .failureHandler((request,response,authenticationException)->{//它是接口 org.springframework.security.web.authentication.AuthenticationFailureHandler 内部类实现
            final String message = authenticationException.getMessage();//用户名或密码错误
            if(authenticationException instanceof LockedException){
                client.responseJson(client.json(199,"账号被锁定"),response);
                return;
            }
            if(authenticationException instanceof CredentialsExpiredException){
                client.responseJson(client.json(199,"密码已过期"),response);
                return;
            }
            if(authenticationException instanceof AccountExpiredException){
                client.responseJson(client.json(199,"账号已过期"),response);
                return;
            }
            if(authenticationException instanceof DisabledException){
                client.responseJson(client.json(199,"账号被禁用"),response);
                return;
            }
            client.responseJson(client.json(199,"账号或密码错误"),response);//返回:{"msg":"Bad credentials","code":199}
        })
        //.failureUrl("/failure")//登录失败的回调;它的重定向(重定向是客户端跳转)
        //.failureForwardUrl("/fail")//登录失败时回调;它是服务端跳转;
        .permitAll()//表示与登录相关的都统统放行[不包含css样式及图片,包含登录页面url;登录接口url;登录失败的回调url；登录成功的回调url]

        .and()//此时又回到上面第1行的 'authorizeRequests()'
        .logout()//配置退出登录
        .logoutUrl("/exit")//退出登录的url,默认是get方式,若要该成post方式请用下面方式处理
        //.logoutRequestMatcher(new AntPathRequestMatcher("/exit","POST"))
        //.logoutSuccessUrl("/login.html")//退出成功的处理
        .logoutSuccessHandler((request,response,authentication)->{//实现接口 org.springframework.security.web.authentication.logout.LogoutSuccessHandler 内部类
            final String json = client.json(200,"退出注销成功");
            client.responseJson(json,response);
        })
        //.invalidateHttpSession(true)//是否让Session失败,默认是true
        //.clearAuthentication(true)//清除认证登录信息,默认是true
        .permitAll()//表示与退出相关的都统统放行

        .and()//此时又回到上面第1行的 'authorizeRequests()'
        .csrf()
        .disable()
        //未登录认证处理
        .exceptionHandling()
        .authenticationEntryPoint((request,response,authException)->{
            final String token = request.getHeader("token");
            final String userName = request.getParameter("userName");
            System.out.println(userName);
            System.out.println(token);
            client.responseJson(client.json(401,"token无效或已过期,请重新登录"),response);
        });
    }
}*/
