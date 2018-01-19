package com.shayne.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 操作实体类
 * @Author WY
 * @Date 2017年12月7日
 */
@Entity
@Table(indexes= {
        @Index(name="codeIndex", columnList="code",unique=false),
        @Index(name="nameIndex", columnList="name",unique=false)
        })
public class Operation implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -1965464576358632295L;

    /** 自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length=20)
    private Long id;
    
    /** 权限代码 */
    @Column(length=16)
    private String code;
    
    /** 权限名称 */
    @Column(length=32)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
