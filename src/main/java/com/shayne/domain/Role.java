package com.shayne.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * 系统角色表
 * @Author WY
 * @Date 2017年12月7日
 */
@Entity
@Table(indexes= {
        @Index(name="roleNameIndex", columnList = "roleName", unique=false)
})
public class Role implements Serializable {
    
    /** serialVersionUID */
    private static final long serialVersionUID = 5752801777579013356L;

    /** 自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length=20)
    private Long id;
    
    /** 角色名称  */
    @Column(length=64)
    private String roleName;
   
    /** 角色对应多个菜单  */
    @OneToMany(cascade = CascadeType.ALL, 
    		mappedBy = "roleId", fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private Set<RoleMenu> roleMenuSet;
    
    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<RoleMenu> getRoleMenuSet() {
        return roleMenuSet;
    }

    public void setRoleMenuSet(Set<RoleMenu> roleMenuSet) {
        this.roleMenuSet = roleMenuSet;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
