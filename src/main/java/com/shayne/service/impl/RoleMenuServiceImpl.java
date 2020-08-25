package com.shayne.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shayne.dao.RoleMenuDao;
import com.shayne.service.RoleMenuService;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    @Autowired
    private RoleMenuDao roleMenuDao;

	@Override
	public Long contByMenuId(Long menuId) {
		return roleMenuDao.countByMenuId(menuId);
	}
    
}
