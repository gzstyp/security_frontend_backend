package com.fwtai.config;

import com.fwtai.JWTAuthenticationEntryPoint;
import com.fwtai.filter.JWTAuthenticationFilter;
import com.fwtai.filter.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception{
        web.ignoring()//忽略
            .antMatchers("/css/**","/images/**");
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()//开启认证
            .anyRequest()//任何请求
            .authenticated()//登录认证之后才能访问,注意配置顺序,顺序很重要,它和shiro是一样的
            .and()//此时又回到上面第1行的 'authorizeRequests()'
            .formLogin()//表单登录
            .usernameParameter("username")//请求时登录账号参数名,即表单的name,若使用了request.getInputStream()，则不好使!!!
            .passwordParameter("password")//登录密码参数名,若使用了request.getInputStream()，则不好使!!!
            .loginPage("/login.html")//指定登录页面的url[若没有额外的指定的话默认的登录接口就是它,只是页面的get请求,而登录接口是post请求]
            .permitAll()
            .and()
            .cors().and().csrf().disable()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))
            // 不需要session
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new JWTAuthenticationEntryPoint());
    }

    /*@Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeRequests()
            // 测试用资源，需要验证了的用户才能访问
            .antMatchers("/tasks/**")
            .authenticated()
            .antMatchers(HttpMethod.DELETE, "/tasks/**")
            .hasRole("ADMIN")
            // 其他都放行了
            .anyRequest().permitAll()
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))
            // 不需要session
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new JWTAuthenticationEntryPoint());
    }*/

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}