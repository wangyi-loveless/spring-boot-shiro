package com.shayne.constans;

/**
 * 异常API常量类
 * @Author WY
 * @Date 2017年12月19日
 */
public interface ExceptionCons {
    /** 权限不足  */
    public static final String UNAUTHORIZED = "401";
    /** 拒绝访问  */
    public static final String FORBIDDEN = "403";
    /** 路径不存在  */
    public static final String NOT_FOUND = "404";
    /** 系统错误  */
    public static final String INTERNAL_SERVER_ERROR = "500";
    /** 错误页面路径  */
    public static final String EXCEPTION_PAGE_DIR = "exception/";
}
