package com.shayne.constans;

/**
 * api常量类
 * @Author WY
 * @Date 2017年12月16日
 */
public interface ApiCons {
    
    public static final String SEPARATOR = "/";
    
    /** 页面 */
    public static final String VIEW = "view" + SEPARATOR;
    
    /** 数据 */
    public static final String OPER = "oper" + SEPARATOR;
    
    /** 首页  */
    public static final String INDEX = "index";

    /** 系统用户管理  */
    public static final String USER = "user";
    /** 系统用户管理  */
    public static final String USER_OPER = OPER + USER;
    /** 系统用户管理  */
    public static final String USER_VIEW = VIEW + USER;
    
    /** 系统菜单管理  */
    public static final String MENU = "menu";
    /** 系统菜单管理  */
    public static final String MENU_OPER = OPER + MENU;
    /** 系统菜单管理  */
    public static final String MENU_VIEW = VIEW + MENU;
    
    /** 系统角色管理  */
    public static final String ROLE = "role";
    /** 系统角色管理  */
    public static final String ROLE_OPER = OPER + ROLE;
    /** 系统角色管理  */
    public static final String ROLE_VIEW = OPER + ROLE;
}
