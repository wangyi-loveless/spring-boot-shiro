package com.shayne.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shayne.constans.ApiCons;
import com.shayne.domain.User;
import com.shayne.domain.vo.ApiResult;
import com.shayne.service.UserService;

/**
 * 用户管理控制器：用户增删改查操作
 * @Author WY
 * @Date 2017年12月16日
 */
@RestController
@RequestMapping(value=ApiCons.USER_OPER)
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    
    /**
     * 新增用户
     * @param user
     * @return
     * String
     */
    @PostMapping
    public ApiResult<User> add(@RequestBody User user) {
        //用户信息加密
        user = super.encrypt(user);
        //用户信息保存
        user = userService.save(user);
        return new ApiResult<>(user);
    }
    
    /**
     * 删除用户
     * @param sysUser
     * @return
     * String
     */
    @DeleteMapping(value="{id}")
    public ApiResult<String> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ApiResult.success();
    }
    
    /**
     * 查询用户
     * @param id
     * @return
     * User
     */
    @GetMapping(value="{id}")
    public ApiResult<User> find(@PathVariable("id") Long id) {
        User user = userService.find(id);
        return new ApiResult<>(user);
    }
    
    /**
     * 查询用户
     * @return
     * String
     */
    @GetMapping
    public ApiResult<List<User>> find() {
        List<User> list = userService.find();
        return new ApiResult<>(list);
    }
    
    /**
     * 用户授权
     * @param sysUser
     * @return
     * String
     */
    @PutMapping(value="{userId}")
    public ApiResult<User> grant(@PathVariable("userId") Long userId,
            @RequestParam("roleIds") String roleIds) {
        Set<Long> roleIdSet = new HashSet<Long>();
        String[] roleids = roleIds.split(",");
        for (String roleid : roleids) {
            roleIdSet.add(new Long(roleid));
        }
        User user = userService.grant(userId, roleIdSet);
        return new ApiResult<>(user);
    }
}
