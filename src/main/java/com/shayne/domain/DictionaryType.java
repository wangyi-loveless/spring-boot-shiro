package com.shayne.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 字典类型表
 * @Author 王小张
 * @Date 2020年8月26日 上午11:54:02
 */
@Entity
@Table(indexes= {
        @Index(name="typeNameIndex", columnList = "typeName", unique=false),
        @Index(name="typeCodeIndex", columnList = "typeCode", unique=true)
})
public class DictionaryType implements Serializable {

    /**
	 * @Field @serialVersionUID 
	 */
	private static final long serialVersionUID = -1544041781605789589L;

	/** 自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length=20)
    private Long id;
    
    /** 字典类型-编码  */
    @NotBlank(message = "字典类型的编码不能为空")
    @Column(length=64, nullable = false, unique = true)
    private String typeCode;
    
    /** 字典类型-名称  */
    @NotBlank(message = "字典类型的名称不能为空")
    @Column(length=64, nullable=false)
    private String typeName;
    
    /** 字典类型-排序  */
    @Column(length=6)
    private Integer typeSort;
    
    /** 字典类型-创建时间 */
    private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getTypeSort() {
		return typeSort;
	}

	public void setTypeSort(Integer typeSort) {
		this.typeSort = typeSort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
