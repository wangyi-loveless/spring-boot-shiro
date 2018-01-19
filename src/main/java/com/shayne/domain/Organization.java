package com.shayne.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 机构实体类
 * @Author WY
 * @Date 2017年12月12日
 */
@Entity
@Table(indexes= {
        @Index(name="codeIndex", columnList = "code", unique=true),
        @Index(name="nameIndex", columnList = "name", unique=false),
        @Index(name="areaIndex", columnList = "areaId", unique=false),
        @Index(name="pidIndex", columnList = "pid", unique=false)
})
public class Organization implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 4016538445914632902L;

    @Id
    @Column(length=20)
    private Long id;
    
    @Column(length=20)
    private String code;
    
    @Column(length=64)
    private String name;
    
    @Column(length=20)
    private Long pid;
    
    @Column(length=20)
    private Long areaId;

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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }
}
