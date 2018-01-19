package com.shayne.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 * @param <T>
 * @Author WY
 * @Date 2018年1月5日
 */
public class PageData<T> implements Serializable{

    private static final long serialVersionUID = -8646159837367756004L;
    private Integer code;
    private String msg;
    private Long count;
    private List<T> data;
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }
}
