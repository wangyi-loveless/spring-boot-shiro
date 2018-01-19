package com.shayne.domain.vo;

import java.io.Serializable;

/**
 * 结果封装类
 * @param <T>
 * @Author WY
 * @Date 2017年12月20日
 */
public class ApiResult<T> implements Serializable{

    /** serialVersionUID */
    private static final long serialVersionUID = -972628776180847864L;

    /** 调用结果 */
    private Boolean result;
    
    /** 提示信息 */
    private String msg;
    
    /** 返回数据 */
    private T data;
    
    public ApiResult(Boolean result, String msg, T data) {
        super();
        this.result = result;
        this.msg = msg;
        this.data = data;
    }
    
    public ApiResult(T data) {
        this.result = true;
        this.msg = "success";
        this.data = data;
    }
    
    public ApiResult() {
        super();
    }
    
    /**
     * 返回成功
     * @return
     * ApiResult
     */
    public static ApiResult<String> success() {
        ApiResult<String> result = new ApiResult<String>();
        result.setResult(true);
        result.setMsg("success");
        result.setData(new String());
        return result;
    }
    
    /**
     * 返回成功
     * @param <T>
     * @param <T>
     * @return
     * ApiResult
     */
    public ApiResult<T> success(T data) {
        ApiResult<T> result = new ApiResult<T>(true, "success", data);
        return result;
    }
    
    /**
     * 返回成功
     * @return
     * ApiResult
     */
    public static ApiResult<String> fail(String msg) {
        ApiResult<String> result = new ApiResult<String>();
        result.setResult(false);
        result.setMsg(msg);
        result.setData(new String());
        return result;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
