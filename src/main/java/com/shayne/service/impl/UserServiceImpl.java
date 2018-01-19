package com.shayne.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shayne.dao.RoleDao;
import com.shayne.dao.UserDao;
import com.shayne.domain.Role;
import com.shayne.domain.User;
import com.shayne.domain.UserRole;
import com.shayne.service.UserService;

@Service
public class UserServiceImpl implements  UserService {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private RoleDao roleDao;
    
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }

    @Override
    public User find(Long id) {
        return userDao.findOne(id);
    }

    @Override
    @Transactional
    public User grant(Long userid, Set<Long> roleIds) {
        User user = userDao.findOne(userid);
        if(null == user) {
            return user;
        }
        Set<UserRole> userRoleSet = new HashSet<>();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userid);
            Role role = roleDao.findOne(roleId);
            if(null == role) {
                continue;
            }
            userRole.setRoleId(roleId);
            userRoleSet.add(userRole);
        }
        user.setUserRoleSet(userRoleSet);
        return userDao.save(user);
    }

    @Override
    public List<User> find() {
        return userDao.findAll();
    }
}
