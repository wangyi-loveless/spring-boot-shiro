package com.shayne.auth;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * 自定义shiro过滤器过滤规则
 * @Author WY
 * @Date 2017年12月19日
 */
public class ShiroFilterFactory extends ShiroFilterFactoryBean {
    // 对ShiroFilter来说，需要直接忽略的请求
    private Set<String> ignoreExt;

    public ShiroFilterFactory() {
        super();
        ignoreExt = new HashSet<>();
        ignoreExt.add(".jpg");
        ignoreExt.add(".png");
        ignoreExt.add(".gif");
        ignoreExt.add(".ico");
        ignoreExt.add(".bmp");
        ignoreExt.add(".js");
        ignoreExt.add(".css");
        ignoreExt.add(".map");
    }

    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        org.apache.shiro.mgt.SecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            String msg = "SecurityManager property must be set.";
            throw new BeanInitializationException(msg);
        }

        if (!(securityManager instanceof WebSecurityManager)) {
            String msg = "The security manager does not implement the WebSecurityManager interface.";
            throw new BeanInitializationException(msg);
        }
        
        FilterChainManager manager = createFilterChainManager();
        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);
        return new SpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
    }

    /**
     * 内部类继承AbstractShiroFilter，隐藏实现过滤规则
     * @Author WY
     * @Date 2017年12月13日
     */
    private final class SpringShiroFilter extends AbstractShiroFilter {
        
        /**
         * SpringShiroFilter 
         * @param webSecurityManager
         * @param resolver
         */
        protected SpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
            super();
            
            if (webSecurityManager == null) {
                throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
            }
            setSecurityManager(webSecurityManager);
            if (resolver != null) {
                setFilterChainResolver(resolver);
            }
        }

        /**
         * 过滤器规则
         */
        @Override
        protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse,
                FilterChain chain) throws ServletException, IOException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String uri = request.getRequestURI().toLowerCase();
            /** 因为ShiroFilter 拦截所有请求（在上面我们配置了urlPattern 为 *，
             * 当然你也可以在那里精确的添加要处理的路径，这样就不需要这个类了），
             * 而在每次请求里面都做了session的读取和更新访问时间等操作，这样在集群部署session共享的情况下，数量级的加大了处理量负载。
             * 所以我们这里将一些能忽略的请求忽略掉。
             * 当然如果你的集群系统使用了动静分离处理，静态资料的请求不会到Filter这个层面，便可以忽略。
             */
            boolean flag = true;
            int idx = 0;
            if ((idx = uri.lastIndexOf(".")) > 0) {
                String suffix = uri.substring(idx);
                //后缀名过滤，静态文件可以直接访问
                if (ignoreExt.contains(suffix)) {
                    flag = false;
                }
            }else {
                //自定义可以匿名访问的路径
            }
            if (flag) {
                super.doFilterInternal(servletRequest, servletResponse, chain);
            } else {
                chain.doFilter(servletRequest, servletResponse);
            }
        }

    }
}
