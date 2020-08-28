package com.shayne.domain.vo;

/**
 * 重置密码
 * @Author 王小张
 * @Date 2020年8月27日 下午10:01:24
 */
public class ResetPasswordUserVo {
    
    /** 主键 */
    private Long id;
    
    /** 旧密码 */
    private String oldPassword;
    
    /** 新密码 */
    private String newPassword;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
