package com.shayne.domain.vo;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 登录用户VO
 * @Author WY
 * @Date 2017年12月19日
 */
public class UserVo {
    
    /** 主键 */
    private Long id;
    
    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
    
    /** 记住我 */
    @Valid
    private String rememberMe;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(String rememberMe) {
        this.rememberMe = rememberMe;
    }
}
