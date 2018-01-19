package com.shayne.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;

/**
 * 用户角色关联表
 * @Author WY
 * @Date 2017年12月7日
 */
@Entity
@IdClass(value=UserRoleIds.class)
public class UserRole implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -8697419091211191522L;
    
    @Id
    @JoinColumn(name = "userId", nullable = false)
    @Column(length=20)
    private Long userId;
    
    @Id
    @JoinColumn(name = "roleId", nullable = false)
    @Column(length=20)
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
