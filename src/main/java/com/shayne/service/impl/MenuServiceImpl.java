package com.shayne.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shayne.constans.ConfigCons;
import com.shayne.dao.MenuDao;
import com.shayne.dao.RoleDao;
import com.shayne.dao.RoleMenuDao;
import com.shayne.domain.Menu;
import com.shayne.domain.Role;
import com.shayne.domain.RoleMenu;
import com.shayne.domain.User;
import com.shayne.domain.UserRole;
import com.shayne.service.MenuService;

@Service
public class MenuServiceImpl implements  MenuService {

    @Autowired
    private MenuDao menuDao;
    
    @Autowired
    private RoleMenuDao roleMenuDao;
    
    @Autowired
    private RoleDao roleDao;

    @Override
    public Menu findByName(String name) {
        return menuDao.findByName(name);
    }

    @Override
    public Menu save(Menu menu) {
        return menuDao.save(menu);
    }

    @Override
    public void delete(Long id) {
        menuDao.delete(id);
    }

    @Override
    public List<Menu> find() {
        return menuDao.findByOrderBySequenceAsc();
    }

    @Override
    public List<Menu> findByPid(Long pid) {
        return menuDao.findByPidOrderBySequenceAsc(pid);
    }

    @Override
    public Page<Menu> find(Pageable pageable, String name) {
    	if(StringUtils.isBlank(name)) {
    		return menuDao.findAll(pageable);
    	}
    	return menuDao.findByNameLike(name, pageable);
    }

	@Override
	public Menu findOne(Long id) {
		return menuDao.findOne(id);
	}

	@Override
	public void delete(Set<Long> ids) {
		List<Menu> menus = menuDao.findByIdIn(ids);
		if(null != menus) {
			menuDao.deleteInBatch(menus);
		}
	}

	@Override
	public List<Menu> findByUser(User user) {
		if(null == user) {
			return null;
		}
		Set<UserRole> userRoleSet = user.getUserRoleSet();
		if(null == userRoleSet) {
			return null;
		}
		Set<Long> roleIdSet = new HashSet<Long>();
		for (UserRole userRole : userRoleSet) {
			roleIdSet.add(userRole.getRoleId());
		}
		List<RoleMenu> roleMenuList = roleMenuDao.findByRoleIdIn(roleIdSet);
		Set<Long> menuIdSet = new HashSet<Long>();
		for (RoleMenu roleMenu : roleMenuList) {
			menuIdSet.add(roleMenu.getMenuId());
		}
		// 用户角色
		Set<UserRole> userRoleSets = user.getUserRoleSet();
		
		// 角色名称集合
		Set<String> roleNameSet = getRolesByUserRole(userRoleSets);
		
		List<Menu> menuList = null;
		if(null != roleNameSet && roleNameSet.contains(ConfigCons.ROLE_ADMIN)) {
			menuList = menuDao.findAll();
		}else {
			menuList = menuDao.findByIdIn(menuIdSet);
		}
		return menuList;
	}
	
	 /**
     * 通过UserRole查询角色名称集合
     * @param userRoleSets
     * @return
     */
    private Set<String> getRolesByUserRole(Set<UserRole> userRoleSets){
    	Set<String> roleNameSet = new HashSet<String>();
    	if(null != userRoleSets && userRoleSets.getClass().getName() == Set.class.getName() 
    			&& !userRoleSets.isEmpty()) {
    		for (UserRole userRole : userRoleSets) {
    			Long roleId = userRole.getRoleId();
    			Role role = roleDao.findOne(roleId);
    			if(null != role) {
    				roleNameSet.add(role.getRoleName());
    			}
    		}
    	}
    	return roleNameSet;
    }
}
