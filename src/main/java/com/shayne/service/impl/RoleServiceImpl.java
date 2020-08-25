package com.shayne.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shayne.dao.MenuDao;
import com.shayne.dao.RoleDao;
import com.shayne.dao.RoleMenuDao;
import com.shayne.domain.Menu;
import com.shayne.domain.Role;
import com.shayne.domain.RoleMenu;
import com.shayne.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    
    @Autowired
    private MenuDao menuDao;
    
    @Autowired
    private RoleMenuDao roleMenuDao;
    
    @Override
    public Role find(Long id) {
        return roleDao.findOne(id);
    }

	@Override
	public Role save(Role role) {
		return roleDao.save(role);
	}

	@Override
	public void delete(Long id) {
		roleDao.delete(id);
	}

	@Override
	public void delete(Set<Long> ids) {
		List<Role> roles = roleDao.findByIdIn(ids);
		roleDao.deleteInBatch(roles);
	}

	@Override
	public Page<Role> pageList(Pageable pageable, String roleName) {
		if(StringUtils.isBlank(roleName)) {
			return roleDao.findAll(pageable);
		}
		return roleDao.findByRoleNameLike(roleName, pageable);
	}

	@Override
	public Role grant(Long roleId, Set<Long> menuIdSet) {
		List<Menu> menuList = menuDao.findByIdIn(menuIdSet);
		Set<RoleMenu> menuSets = new HashSet<RoleMenu>();
		if(CollectionUtils.isNotEmpty(menuList)) {
			for (Menu menu : menuList) {
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setMenuId(menu.getId());
				roleMenu.setRoleId(roleId);
				menuSets.add(roleMenu);
			}
		}
		Role role = roleDao.findOne(roleId);
		if(null != role) {
			roleMenuDao.deleteByRoleId(roleId);
			role.setRoleMenuSet(menuSets);
			return roleDao.save(role);
		}
		return null;
	}

	@Override
	public String getGrantMenus(Long roleId) {
		StringBuffer sb = new StringBuffer();
		Role role = roleDao.findOne(roleId);
		if(null != role) {
			Set<RoleMenu> roleMenuSet = role.getRoleMenuSet();
			for (RoleMenu roleMenu : roleMenuSet) {
				sb.append(roleMenu.getMenuId() + ",");
			}
			String menuSets = sb.toString();
			if(menuSets.length()>1 && menuSets.contains(",")) {
				menuSets = menuSets.substring(0, menuSets.lastIndexOf(","));
			}
			return menuSets;
		}
		return null;
	}
}
