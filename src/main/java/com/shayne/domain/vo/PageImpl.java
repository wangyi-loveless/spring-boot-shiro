package com.shayne.domain.vo;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页对象
 * @Author WY
 * @Date 2018年1月5日
 */
public class PageImpl implements Pageable, Serializable{

    /** serialVersionUID */
    private static final long serialVersionUID = 5600701558954267803L;

    /** 第几页 */
    private Integer page;
    
    /** 页数据条数 */
    private Integer limit;
    
    /** 排序规则 */
    private Sort sort;
    
    public PageImpl() {
        super();
    }

    public PageImpl(Integer page, Integer limit) {
        super();
        this.page = page;
        this.limit = limit;
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public int getOffset() {
        return (page-1)*limit;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

}
