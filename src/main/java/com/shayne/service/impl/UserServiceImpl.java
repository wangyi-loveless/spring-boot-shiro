package com.shayne.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shayne.dao.RoleDao;
import com.shayne.dao.UserDao;
import com.shayne.dao.UserRoleDao;
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
    
    @Autowired
    private UserRoleDao userRoleDao;
    
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
    public User grant(Long userId, Set<Long> roleIds) {
    	if(null == userId) {
    		return null;
    	}
		User user = userDao.findOne(userId);
		if(null == user) {
			return null;
		}
		
    	Set<UserRole> userRoleSet = new HashSet<>();
    	for (Long roleId : roleIds) {
    		UserRole userRole = new UserRole();
    		userRole.setUserId(userId);
    		Role role = roleDao.findOne(roleId);
    		if(null == role) {
    			continue;
    		}
    		userRole.setRoleId(roleId);
    		userRoleSet.add(userRole);
    	}
		// 清掉历史授权
		userRoleDao.deleteByUserId(userId);
		
		user.setUserRoleSet(userRoleSet);
		user = userDao.save(user);
    	return user;
    	
    }

    @Override
    public List<User> find() {
        return userDao.findAll();
    }

	@Override
	public Page<User> pageList(Pageable pageable, String username) {
		if(StringUtils.isBlank(username)) {
    		return userDao.findAll(pageable);
    	}
    	return userDao.findByUsernameLike(username, pageable);
	}

	@Override
	public void delete(Set<Long> ids) {
		if(null != ids) {
			List<User> userList = userDao.findByIdIn(ids);
			userDao.deleteInBatch(userList);
		}
	}
}
