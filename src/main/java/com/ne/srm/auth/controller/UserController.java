package com.ne.srm.auth.controller;

import com.ne.srm.auth.service.UserService;
import com.ne.srm.common.module.Result;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService){
        this.userService=userService;
    }



    /**
     * 禁用用户
     */
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('user:disable')")
    public Result disable(@PathVariable Long id){
        userService.disable(id);
        return Result.ok();
    }



    /**
     * 启用用户
     */
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('user:disable')")
    public Result enable(@PathVariable Long id){
        userService.enable(id);
        return Result.ok();

    }

    /**
     * 查看用户列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('user:list')")
    public String list(){
        return "user list";
    }

    /**
     * 新增用户
     */
    @PostMapping
    @PreAuthorize("hasAuthority('user:add')")
    public String add(){

        return "user add";

    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public String delete(){
        return "user delete";

    }

}
