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
 * 字典表
 * @Author 王小张
 * @Date 2020年8月26日 下午2:09:37
 */
@Entity
@Table(indexes= {
		@Index(name="typeCodeIndex", columnList = "typeCode", unique=false),
        @Index(name="dicNameIndex", columnList = "dicName", unique=false),
        @Index(name="dicCodeIndex", columnList = "dicCode", unique=true)
})
public class Dictionary implements Serializable {

	/** @Field @serialVersionUID  */
	private static final long serialVersionUID = 1712554400432674144L;

	/** 自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length=20)
    private Long id;
    
    /** 字典-类型编码  */
    @NotBlank(message = "字典类型编码不能为空")
    @Column(length=64, nullable = false)
    private String typeCode;
    
    /** 字典-编码  */
    @NotBlank(message = "字典的编码不能为空")
    @Column(length=64, nullable = false)
    private String dicCode;
    
    /** 字典类型-名称  */
    @NotBlank(message = "字典的名称不能为空")
    @Column(length=64, nullable=false)
    private String dicName;
    
    /** 字典-排序  */
    @Column(length=6)
    private Integer dicSort;
    
    /**
     * 字典类型-创建时间
     */
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

	public String getDicCode() {
		return dicCode;
	}

	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public Integer getDicSort() {
		return dicSort;
	}

	public void setDicSort(Integer dicSort) {
		this.dicSort = dicSort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
