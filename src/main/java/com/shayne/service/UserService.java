package com.shayne.service;

import java.util.List;
import java.util.Set;

import com.shayne.domain.User;

/**
 * 系统用户service
 * @Author WY
 * @Date 2017年12月8日
 */
public interface UserService {

    /**
     * 查询
     * @param username
     * @return
     * SysUser
     */
    User findByUsername(String username);
    
    /**
     * 新增
     * @param sysUser
     * @return
     * SysUser
     */
    User save(User sysUser);
    
    /**
     * 删除用户
     * @return
     * Long
     */
    void delete(Long id);
    
    /**
     * 查询用户
     * @param id
     * @return
     * SysUser
     */
    User find(Long id);
    
    /**
     * 授权
     * @param userId
     * @param roleId
     * @return
     * User
     */
    User grant(Long userId, Set<Long> roleIds);
    
    /**
     * 查询所有用户
     * @return
     * Set<User>
     */
    List<User> find();
}
