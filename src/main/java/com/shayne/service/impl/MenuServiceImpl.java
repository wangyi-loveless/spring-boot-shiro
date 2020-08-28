package com.shayne.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.shayne.constans.ConfigCons;
import com.shayne.dao.MenuDao;
import com.shayne.dao.RoleDao;
import com.shayne.dao.RoleMenuDao;
import com.shayne.dao.UserRoleDao;
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
    
    @Autowired
    private UserRoleDao userRoleDao;

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
		
		// 通过用户ID查询角色用户管理关系
		List<UserRole> userRoleList = userRoleDao.findByUserId(user.getId());
		if(null == userRoleList) {
			return null;
		}
		
		// 角色ID集合
		Set<Long> roleIdSet = new HashSet<Long>();
		for (UserRole userRole : userRoleList) {
			roleIdSet.add(userRole.getRoleId());
		}
		
		// 通过角色ID集合，查询角色菜单关联关系
		List<RoleMenu> roleMenuList = roleMenuDao.findByRoleIdIn(roleIdSet);
		Set<Long> menuIdSet = new HashSet<Long>();
		for (RoleMenu roleMenu : roleMenuList) {
			menuIdSet.add(roleMenu.getMenuId());
		}
		
		// 角色名称集合
		Set<String> roleNameSet = getRoles(roleIdSet);
		
		List<Menu> menuList = null;
		if(null != roleNameSet && roleNameSet.contains(ConfigCons.ROLE_ADMIN)) {
			Sort sort = new Sort(Direction.ASC, "sequence");
			menuList = menuDao.findAll(sort);
		}else {
			menuList = menuDao.findByIdInOrderBySequenceAsc(menuIdSet);
		}
		return menuList;
	}
	
	 /**
	  * 查询角色集合
	  * @param Set<Long> roleIdSet
	  * @return Set<String>
	  */
    private Set<String> getRoles(Set<Long> roleIdSet){
    	Set<String> roleNameSet = new HashSet<String>();
    	if(null != roleIdSet && roleIdSet.size()>0) {
    		List<Role> roleList = roleDao.findByIdIn(roleIdSet);
    		for (Role role : roleList) {
    			if(null != role) {
    				roleNameSet.add(role.getRoleName());
    			}
    		}
    	}
    	return roleNameSet;
    }
}
