package com.shayne.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * 菜单操作IdClass
 * @Author WY
 * @Date 2017年12月19日
 */
@Embeddable
public class MenuOperationIds implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = -3982736789116490236L;
    
    private Long operationId;
    private Long menuId;
    public Long getOperationId() {
        return operationId;
    }
    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }
    public Long getMenuId() {
        return menuId;
    }
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((menuId == null) ? 0 : menuId.hashCode());
        result = prime * result + ((operationId == null) ? 0 : operationId.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MenuOperationIds other = (MenuOperationIds) obj;
        if (menuId == null) {
            if (other.menuId != null)
                return false;
        } else if (!menuId.equals(other.menuId))
            return false;
        if (operationId == null) {
            if (other.operationId != null)
                return false;
        } else if (!operationId.equals(other.operationId))
            return false;
        return true;
    }
    
}
