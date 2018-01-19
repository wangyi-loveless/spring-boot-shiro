package com.shayne.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session Listener 自定义实现
 * @Author WY
 * @Date 2018年1月9日
 */
public class CustomSessionListener implements SessionListener {
    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public void onStart(Session session) {
        System.err.println("会话创建：" + session.getId());
        logger.info("会话创建：" + session.getId()); 
    }

    @Override
    public void onStop(Session session) {
        System.err.println("退出会话：" + session.getId());
        logger.info("退出会话：" + session.getId()); 
    }

    @Override
    public void onExpiration(Session session) {
        System.err.println("会话过期：" + session.getId());
        logger.info("会话过期：" + session.getId()); 
    }
}
