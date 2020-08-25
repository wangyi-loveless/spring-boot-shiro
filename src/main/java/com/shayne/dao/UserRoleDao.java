package com.shayne.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shayne.domain.UserRole;

/**
 * 系统角色DAO
 * @Author WY
 * @Date 2017年12月7日
 */
public interface UserRoleDao extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {

	/**
	 * 通过用户ID删除
	 * @param roleId
	 */
	@Transactional
	void deleteByUserId(Long userId);
	
	/**
	 * 根据角色ID查询
	 * @param roleId
	 * @return
	 */
	Long countByRoleId(Long roleId);

	/**
	 * 通过用户ID查询
	 * @param userId
	 * @return
	 */
	List<UserRole> findByUserId(Long userId);
}
