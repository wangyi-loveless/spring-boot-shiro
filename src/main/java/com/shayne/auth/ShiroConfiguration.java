package com.shayne.auth;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.shayne.constans.BaseCons;
import com.shayne.constans.ExceptionCons;
import com.shayne.constans.LoginCons;

/**
 * Shiro配置管理器
 * @Author WY
 * @Date 2017年12月13日
 */
@Configuration
public class ShiroConfiguration {
    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * HashedCredentialsMatcher，这个类是为了对密码进行编码的， 防止密码在数据库里明码保存，当然在登陆认证的时候，
     * 这个类也负责对form里输入的密码进行编码。
     */
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        // 指定散列算法,缺省为md5
        credentialsMatcher.setHashAlgorithmName(BaseCons.SHIRO_ENCRYPT_ALGORITHM_NAME);
        // 散列迭代次数
        credentialsMatcher.setHashIterations(BaseCons.SHIRO_ENCRYPT_HASH_ITERATIONS);
        // storedCredentialsHexEnc表示是否存储散列后的密码为16进制，需要和生成密码时的一样。
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }
    
    /**
     * HashedCredentialsMatcher，这个类是为了对密码进行编码的， 防止密码在数据库里明码保存，当然在登陆认证的时候，
     * 这个类也负责对form里输入的密码进行编码。
     */
    @Bean(name = "simpleCredentialsMatcher")
    public SimpleCredentialsMatcher simpleCredentialsMatcher() {
        SimpleCredentialsMatcher credentialsMatcher = new CustomSimpleCredentialsMatcher();
        return credentialsMatcher;
    }

    /**
     * ShiroRealm，这是个自定义的认证类，继承自AuthorizingRealm，
     * 负责用户的认证和权限的处理，可以参考JdbcRealm的实现。
     */
    @Bean(name = "shiroRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroRealm shiroRealm() {
        ShiroRealm realm = new ShiroRealm();
        realm.setCacheManager(ehCacheManager());
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

     /**
     * EhCacheManager，缓存管理，用户登陆成功后，把用户信息和权限信息缓存起来，
     * 然后每次用户请求时，放入用户的session中，如果不设置这个bean，每个请求都会查询一次数据库。
     */
     @Bean(name = "ehCacheManager")
     @DependsOn("lifecycleBeanPostProcessor")
     public EhCacheManager ehCacheManager() {
         EhCacheManager ehCacheManager = new EhCacheManager();
         ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
         logger.info("##################  读取shiro ehcache 配置  ##################");
         return ehCacheManager;
     }

    /**
     * SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。 //
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(EhCacheManager cacheManager, 
            ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        securityManager.setCacheManager(cacheManager);
        return securityManager;
    }
    
    /**
     * 加载shiroFilter权限控制规则（从数据库读取然后配置）
     */
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean){
        /////////////////////// 下面这些规则配置最好配置到配置文件中 ///////////////////////
        Map<String, Filter> filters = new HashMap<String, Filter>();
        filters.put("authc", new CustomerFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);
        
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        /**
         *  authc：该过滤器下的页面必须验证后才能访问，
         *  它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
         *  这里为了测试，只限制/user，实际开发中请修改为具体拦截的请求规则
         */
        filterChainDefinitionMap.put("/**", "authc");
        logger.info("##################从数据库读取权限规则，加载到shiroFilter中##################");
        
        //登录不拦截
//        filterChainDefinitionMap.put(BaseCons.BASE_PATH + LoginCons.LOGIN, "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }
    
    /**
     * ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。
     * 它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactory();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login"页面
        shiroFilterFactoryBean.setLoginUrl(BaseCons.BASE_PATH + LoginCons.LOGIN);
        // 登录成功后要跳转的连接
//        shiroFilterFactoryBean.setSuccessUrl(BaseCons.BASE_PATH + ApiCons.INDEX);
        //设置没有权限的跳转路径
        shiroFilterFactoryBean.setUnauthorizedUrl(BaseCons.BASE_PATH + ExceptionCons.UNAUTHORIZED);
        //过滤器
        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }

    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
