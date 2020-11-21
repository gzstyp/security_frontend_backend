package com.fwtai.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class User {

    private Integer id;

    private String username;

    private String password;

    private List<String> authorities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());
    }

    public void setAuthorities(final List<String> list){
        this.authorities = list;
    }
}