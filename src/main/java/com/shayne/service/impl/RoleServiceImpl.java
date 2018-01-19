package com.shayne.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shayne.dao.RoleDao;
import com.shayne.domain.Role;
import com.shayne.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    
    @Override
    public Role find(Long id) {
        return roleDao.findOne(id);
    }

}
