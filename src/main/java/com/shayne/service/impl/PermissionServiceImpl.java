package com.shayne.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shayne.dao.PermissionDao;
import com.shayne.domain.Operation;
import com.shayne.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    
    @Override
    public Operation find(Long id) {
        return permissionDao.findOne(id);
    }

}
