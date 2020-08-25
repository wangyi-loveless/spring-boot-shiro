package com.shayne.domain.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户角色授权VO
 * @Author 王小张
 * @Date 2020年8月25日 下午2:43:10
 */
public class UserRoleVo {

    @NotBlank(message = "用户不能为空")
	private Long userId;
	
	private String roleIds;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
}
