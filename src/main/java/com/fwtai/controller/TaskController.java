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

    @GetMapping("/getTasks")
    @ResponseBody
    public String listTasks(){
        return "任务列表";
    }

    // http://127.0.0.1:8090/tasks/newTasks
    @PostMapping("/newTasks")
    @PreAuthorize("hasAuthority('ADMIN')")
    //@PreAuthorize("hasAuthority('edit')")//都可以
    public String newTasks(){
        return "创建了一个新的任务";
    }

    // http://127.0.0.1:8090/tasks/addTask //提示权限不足
    @PostMapping("/addTask")
    @PreAuthorize("hasAuthority('ADMIN0')")
    public String addTask(){
        return "创建了一个新的任务";
    }
}