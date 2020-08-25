package com.shayne.domain;

import java.io.Serializable;
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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 菜单实体类
 * @Author WY
 * @Date 2017年12月12日
 */
@Entity
@Table(indexes= {
        @Index(name="nameIndex", columnList = "name", unique=true),
        @Index(name="pidIndex", columnList = "pid", unique=false)
})
public class Menu implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -3182098309633779069L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length=20)
    private Long id;
    
    @Column(length=128)
    @NotBlank
    private String name;
    
    @Column(length=256)
    private String url;
    
    @Column(length=16)
    private Integer sequence;
    
    @Column(length=20)
    private Long pid = 0l;

    @Column(length=8)
    private Integer status;
    
    /** 菜单对应多个角色  */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menuId", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("id ASC")
    private Set<RoleMenu> roleMenuSet;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<RoleMenu> getRoleMenuSet() {
        return roleMenuSet;
    }

    public void setRoleMenuSet(Set<RoleMenu> roleMenuSet) {
        this.roleMenuSet = roleMenuSet;
    }
}
