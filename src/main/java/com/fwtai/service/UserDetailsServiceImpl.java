package com.fwtai.service;

import com.fwtai.dao.UserDao;
import com.fwtai.entity.JwtUser;
import com.fwtai.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private UserDao dao;
	
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = new User();
        final HashMap<String,Object> map = dao.findByUsername(username);//User包含角色和权限
        user.setId(Integer.parseInt(map.get("id").toString()));
        user.setUsername(String.valueOf(map.get("username")));
        user.setPassword(String.valueOf(map.get("password")));
        final Object roles = map.get("roles");
        if(roles != null){
            final String value = String.valueOf(roles);
            final List<String> list = Arrays.asList(value.split(","));
            user.setAuthorities(list);
        }
        return new JwtUser(user);
    }
}