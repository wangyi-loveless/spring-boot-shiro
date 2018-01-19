package com.shayne.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 地区-省市县实体类
 * @Author WY
 * @Date 2017年12月12日
 */
@Entity
@Table(indexes= {
        @Index(name="nameIndex", columnList = "name", unique=false),
        @Index(name="pidIndex", columnList = "pid", unique=false)
})
public class Area implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -5079635610677447087L;

    @Id
    @Column(length=20)
    private Long id;
    
    @Column(length=128)
    private String name;
    
    @Column(length=20)
    private Long pid;

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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
