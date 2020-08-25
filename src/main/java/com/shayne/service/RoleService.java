package com.shayne.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shayne.domain.Role;

/**
 * 角色
 * @Author WY
 * @Date 2017年12月11日
 */
public interface RoleService {

	/**
	 * 查询
	 * @param id
	 * @return
	 */
    Role find(Long id);

    /**
     * 保存
     * @param role
     * @return
     */
	Role save(Role role);
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	void delete(Long id);
	
	/**
	 * 批量删除
	 * @param ids
	 */
	void delete(Set<Long> ids);
	

    /**
     * 查询分页数据
     * @param pageable
     * @param roleName
     * @return
     */
	Page<Role> pageList(Pageable pageable, String roleName);

	/**
	 * 角色菜单授权
	 * @param roleId
	 * @param menuIdSet
	 * @return
	 */
	Role grant(Long roleId, Set<Long> menuIdSet);

	/**
	 * 获取权限
	 * @param roleId
	 * @return
	 */
	String getGrantMenus(Long roleId);

}
