package com.shayne.auth;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.shayne.constans.ApiCons;

/**
 * 重写shiro表单过滤器FormAuthenticationFilter中的onAccessDenied方法，在该方法中除了实现原有的功能外，
 * 还需要增加判断，当shiro认证未通过的情况下，并且前端为ajax请求，则将该请求的请求状态置为401
 * 
 * @Author WY
 * @Date 2018年1月9日
 */
public class CustomerFormAuthenticationFilter extends FormAuthenticationFilter {
    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 设置登录页面 */
    public CustomerFormAuthenticationFilter() {
        setLoginUrl(ApiCons.SEPARATOR + ApiCons.LOGIN);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login submission detected. Attempting to execute loggerin.");
                }
                return executeLogin(request, response);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login page view.");
                }
                return true;
            }
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("Attempting to access a path which requires authentication. Forwarding to the "
                        + "Authentication url [" + getLoginUrl() + "]");
            }
            // 判断session里是否有用户信息
            if (httpRequest.getHeader("X-Requested-With") != null && httpRequest.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
                // 如果是ajax请求响应头会有，x-requested-with
                httpResponse.sendError(HttpStatus.UNAUTHORIZED.value());
            } else {
                redirectToLogin(request, response);
            }
            return false;
        }
    }
}
