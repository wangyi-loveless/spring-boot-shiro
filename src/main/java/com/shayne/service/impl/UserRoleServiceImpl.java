package com.shayne.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shayne.dao.UserRoleDao;
import com.shayne.domain.UserRole;
import com.shayne.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements  UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

	@Override
	public String getGrantRoles(Long userId) {
		List<UserRole> userRoleList= userRoleDao.findByUserId(userId);
		String roleIds = "";
		if(!userRoleList.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			for (UserRole userRole : userRoleList) {
				Long roleId = userRole.getRoleId();
				if(null != roleId) {
					sb.append(userRole.getRoleId() + ",");
				}
				roleIds = sb.toString();
			}
			if(roleIds.length()>0 && roleIds.contains(",")) {
				roleIds = roleIds.substring(0,roleIds.lastIndexOf(","));
			}
		}
		return roleIds;
	}

	@Override
	public void deleteByUserId(Long userId) {
		userRoleDao.deleteByUserId(userId);
	}
   
}
