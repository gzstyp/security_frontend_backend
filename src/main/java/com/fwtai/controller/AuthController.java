package com.fwtai.controller;

import com.fwtai.entity.User;
import com.fwtai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by echisan on 2018/6/23
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // http://127.0.0.1:8090/auth/register
    @PostMapping("/register")
    public String registerUser(@RequestBody Map<String,String> registerUser){
        final User user = new User();
        user.setUsername(registerUser.get("username"));
        user.setPassword(bCryptPasswordEncoder.encode(registerUser.get("password")));
        userService.save(user);
        return "success";
    }

    //http://127.0.0.1:8090/auth/console
    @GetMapping("/console")
    public String console(){
        return "console";
    }
}