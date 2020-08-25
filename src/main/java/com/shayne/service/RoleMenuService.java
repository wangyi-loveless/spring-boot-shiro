package com.shayne.service;

/**
 * 角色
 * @Author WY
 * @Date 2017年12月11日
 */
public interface RoleMenuService {

	/**
	 * 通过菜单ID查询菜单授权数据
	 * @param menuId
	 * @return
	 */
	Long contByMenuId(Long menuId);

}
