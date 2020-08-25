package com.shayne.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shayne.domain.Role;

/**
 * 系统角色DAO
 * @Author WY
 * @Date 2017年12月7日
 */
public interface RoleDao extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

	/**
	 * 通过ID查询集合数据
	 * @param ids
	 * @return
	 */
	List<Role> findByIdIn(Set<Long> ids);

	/**
	 * 通过名称模糊查询
	 * @param roleName
	 * @param pageable 
	 * @return
	 */
	Page<Role> findByRoleNameLike(String roleName, Pageable pageable);
}
