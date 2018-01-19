package com.shayne.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.shayne.constans.BaseCons;
import com.shayne.constans.ExceptionCons;
import com.shayne.domain.User;
import com.shayne.util.EncryptComponent;

/**
 * 控制器基类
 * @Author WY
 * @Date 2017年12月13日
 */
@Controller
public abstract class BaseController {
    
    /** 日志对象   */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /** 名称 */
    @Value(value="${application.name}")
    protected String APPLICATION_NAME;
    
    /** session user name */
    protected final String SESSION_USERNAME = "user";
    
    @Autowired
    protected EncryptComponent md5Util;
    
    /**
     * 获取当前登录用户
     * @return User
     */
    protected User getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        if(null == subject) {
            return null;
        }
        Object object = subject.getPrincipal();
        if(null == object) {
            return null;
        }
        User user = null;
        try {
            user = (User) object;
        } catch (Exception e) {
            logger.error("获取session用户发生异常：" + e);
        }
        return user;
    }
    
    /**
     * 用户加密
     * @param user
     * @return
     * User
     */
    protected User encrypt(User user) {
        return md5Util.encrypt(user, BaseCons.SHIRO_ENCRYPT_ALGORITHM_NAME, BaseCons.SHIRO_ENCRYPT_HASH_ITERATIONS);
    }
    
    /**
     * 获取ModelAndView
     * @param name
     * @return
     * ModelAndView
     */
    protected ModelAndView baseModelAndView(String name) {
        ModelAndView mv = new ModelAndView(name);
        mv.addObject(BaseCons.APPLICATION_NAME, APPLICATION_NAME);
        return mv;
    }
    
    /**
     * 获取ModelAndView
     * @param name
     * @return
     * ModelAndView
     */
    protected ModelAndView baseExceptionModelAndView(String name) {
        ModelAndView mv = baseModelAndView(BaseCons.EXCEPTION_PATH + name);
        mv.addObject(BaseCons.APPLICATION_NAME, APPLICATION_NAME);
        return mv;
    }
    
    /**
     * 获取 ModelAndView
     * @param name
     * @return
     * ModelAndView
     */
    protected ModelAndView baseUserModelAndView(String name) {
        ModelAndView mv = baseModelAndView(name);
        //如果不是异常页面，则添加用户信息到返回页面
        User user = getCurrentUser();
        if(user == null) {
            mv.setViewName(ExceptionCons.UNAUTHORIZED);
        }
        mv.addObject(SESSION_USERNAME, user);
        return mv;
    }
    
}
