package com.shayne.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;

/**
 * 角色菜单对应关系实体类
 * @Author WY
 * @Date 2017年12月12日
 */
@Entity
@IdClass(value=RoleMenuIds.class)
public class RoleMenu implements Serializable {


    /** serialVersionUID */
    private static final long serialVersionUID = -3777344385410559543L;

    @Id
    @JoinColumn(name = "menuId", nullable = false)
    @Column(length=20)
    private Long menuId;
    
    @Id
    @JoinColumn(name = "roleId", nullable = false)
    @Column(length=20)
    private Long roleId;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
