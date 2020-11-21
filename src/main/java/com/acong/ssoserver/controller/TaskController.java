package com.acong.ssoserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @GetMapping("/getTasks")
    @ResponseBody
    public String listTasks(){
        return "任务列表";
    }

    // http://127.0.0.1:8090/tasks
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN0')")
    //@PreAuthorize("hasAuthority('edit')")//都可以
    public String newTasks(){
        return "创建了一个新的任务";
    }
}