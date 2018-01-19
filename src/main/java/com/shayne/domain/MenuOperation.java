package com.shayne.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;

/**
 * 菜单操作对应关系实体类
 * @Author WY
 * @Date 2017年12月19日
 */
@Entity
@IdClass(value=MenuOperationIds.class)
public class MenuOperation implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6314143140921653759L;

    @Id
    @JoinColumn(name = "menuId", nullable = false)
    @Column(length=20)
    private Long menuId;
    
    @Id
    @JoinColumn(name = "operationId", nullable = false)
    @Column(length=20)
    private Long operationId;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }
}
