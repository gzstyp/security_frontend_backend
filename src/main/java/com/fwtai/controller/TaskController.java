package com.fwtai.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    // http://127.0.0.1:8090/tasks/getTasks
    @GetMapping("/getTasks")
    @ResponseBody
    public String listTasks(){
        return "任务列表";
    }

    // http://127.0.0.1:8090/tasks/newTasks
    @PostMapping("/newTasks")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")//无效
    //@PreAuthorize("hasRole('ADMIN')")//无效
    @PreAuthorize("hasAuthority('ADMIN')")//当为@PreAuthorize("hasAuthority('XXX')")时角色权限名大小写都的，主要和数据库对应即可
    //@PreAuthorize("hasAuthority('edit')")//都可以
    public String newTasks(){
        return "创建了一个新的任务";
    }

    // http://127.0.0.1:8090/tasks/addTask //提示:权限不足
    @PostMapping("/addTask")
    @PreAuthorize("hasAuthority('ADMIN0')")
    public String addTask(){
        return "创建了一个新的任务";
    }

    // http://127.0.0.1:8090/tasks/admin
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN88')")//此时的 ADMIN88 可加可不加前缀ROLE_都可以的,此时数据库保存的角色名必须是以ROLE_前缀
    public String admin(){
        return "操作成功,此时的 ADMIN 可加可不加前缀 ROLE_都可以的";
    }
}