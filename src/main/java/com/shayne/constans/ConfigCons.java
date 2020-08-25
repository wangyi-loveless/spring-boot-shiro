package com.shayne.constans;

/**
 * 基础常量类
 * @Author WY
 * @Date 2017年12月16日
 */
public interface ConfigCons {

    /** 应用名称 */
    public static final String  APPLICATION_NAME = "application_name";
    
    /** 异常提示--超时 */
    public static final String  EXCEPTION_TIMEOUT_MSG = "服务器开小差了，请稍后重试，或及时联系客服人员！";
    
    /** 异常提示--系统错误 */
    public static final String  EXCEPTION_ERROR_MSG = "服务器发生了一点小意外，"
            + "可能遇到了它暂时无法解决的问题，请稍后重试，或及时联系客服人员！";
    
    /** admin角色名称  */
    public static final String ROLE_ADMIN = "admin";
    
    /** 指定散列算法  */
    public static final String SHIRO_ENCRYPT_ALGORITHM_NAME = "MD5";
    
    /** 散列迭代次数  */
    public static final Integer SHIRO_ENCRYPT_HASH_ITERATIONS = 2;
    
}
