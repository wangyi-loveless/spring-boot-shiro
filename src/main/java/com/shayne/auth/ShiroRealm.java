package com.shayne.auth;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.shayne.domain.Role;
import com.shayne.domain.User;
import com.shayne.domain.UserRole;
import com.shayne.service.RoleService;
import com.shayne.service.UserService;

/**
 * 认证类
 * @Author WY
 * @Date 2017年12月12日
 */
public class ShiroRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    /**
     * 权限认证，为当前登录的Subject授予角色和权限 
     * @see 经测试：本例中该方法的调用时机为需授权资源被访问时 
     * @see 经测试：并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache 
     * @see 经测试：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，
     * Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("##################执行Shiro权限认证##################");
        //获取当前登录输入的用户名，等价于(String) principalCollection.fromRealm(getName()).iterator().next();
        String loginName = (String) super.getAvailablePrincipal(principalCollection); 
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userService.findByUsername(loginName);

        // 把principals放session中 key=userId value=principals
        SecurityUtils.getSubject().getSession().setAttribute(String.valueOf(user.getId()),
                SecurityUtils.getSubject().getPrincipals());

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 赋予角色
        for (UserRole userRole : user.getUserRoleSet()) {
            Long roleId = userRole.getRoleId();
            Role role = roleService.find(roleId);
            if(null == role) {
                continue;
            }
            info.addRole(role.getRoleName());
            
            // 赋予权限
//            for (RolePermission rolePermission : role.getRolePermissionSet()) {
//                Long permissionId = rolePermission.getPermissionId();
//                Operation permission = permissionService.find(permissionId);
//                if(null == permission) {
//                    continue;
//                }
//                info.addStringPermission(permission.getPermissionName());
//            }
        }
        return info;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        logger.info("################## 执行登录认证 ##################");
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        
        logger.info("验证当前Subject时获取到token为：" 
                + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE)); 
        //验证用户名
        User user = userService.findByUsername(token.getUsername());
        //用户不存在
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        SimpleAuthenticationInfo authenticationInfo = 
            new SimpleAuthenticationInfo(
            user, //登录识别串信息
            user.getPassword(), //密码
            ByteSource.Util.bytes(user.getSalt()),//盐值
            getName()  //realm name
        );     
        return authenticationInfo;
    }
    
    
}
