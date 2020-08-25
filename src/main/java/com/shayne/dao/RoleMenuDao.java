package com.shayne.dao;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shayne.domain.RoleMenu;

/**
 * 系统角色DAO
 * @Author WY
 * @Date 2017年12月7日
 */
public interface RoleMenuDao extends JpaRepository<RoleMenu, Long>, JpaSpecificationExecutor<RoleMenu> {

	/**
	 * 通过角色名称删除
	 * @param roleId
	 */
	@Transactional
	void deleteByRoleId(Long roleId);
	
	/**
	 * 根据菜单ID查询
	 * @param menuId
	 * @return
	 */
	Long countByMenuId(Long menuId);

	/**
	 * 通过角色ID查询角色菜单授权
	 * @param roleIdSet
	 * @return
	 */
	List<RoleMenu> findByRoleIdIn(Set<Long> roleIdSet);
}
