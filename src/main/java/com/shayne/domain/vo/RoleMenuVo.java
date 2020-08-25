package com.shayne.domain.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 角色菜单VO
 * @Author 王小张
 * @Date 2020年8月20日 下午7:33:37
 */
public class RoleMenuVo {

	/**
	 * 角色ID
	 */
	@NotBlank(message = "角色ID不能为空")
	private String roleId;
	
	/**
	 * 菜单ID，逗号分隔
	 */
	@NotBlank(message = "菜单ID不能为空")
	private String menuIds;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
}
