package com.shayne.service;

/**
 * 系统用户service
 * @Author WY
 * @Date 2017年12月8日
 */
public interface UserRoleService {

	/**
	 * 通过用户ID查询授权角色ID
	 * @param userId
	 * @return
	 */
	String getGrantRoles(Long userId);
	
	/**
	 * 通过用户ID删除用户授权角色
	 * @param userId
	 * @return
	 */
	void deleteByUserId(Long userId);
}
