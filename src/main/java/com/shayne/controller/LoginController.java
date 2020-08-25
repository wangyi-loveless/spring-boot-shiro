package com.shayne.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shayne.constans.ApiCons;
import com.shayne.domain.vo.ApiResult;
import com.shayne.domain.vo.UserVo;
import com.shayne.util.IpUtil;

/**
 * 登录控制器
 * @Author WY
 * @Date 2017年12月13日
 */
@Controller
public class LoginController extends BaseController {
    
    /**
     * 登录认证
     * @param user
     * @param bindingResult
     * @param redirectAttributes
     * @return
     * String
     */
    @ResponseBody
    @PostMapping(value = ApiCons.LOGIN)
    public ApiResult<String> login(@Valid @RequestBody UserVo user, HttpServletRequest request) {
        //用户帐号
        String username = user.getUsername();
        //用户密码
        String password = user.getPassword();
        //是否记住
        Boolean rememberMe = Boolean.parseBoolean(user.getRememberMe());
        String host = IpUtil.getHost(request);
        // 认证对象
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe, host);
        //获取当前的Subject
        Subject subject = SecurityUtils.getSubject();
        try {
            /** 在调用了login方法后,SecurityManager会收到AuthenticationToken,
             *  并将其发送给已配置的Realm执行必须的认证检查  
             *  每个Realm都能在必要时对提交的AuthenticationTokens作出反应  
             *  所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,
             *  具体验证方式详见此方法
             */
            logger.info("对用户[" + username + "]进行登录验证..验证开始");
            subject.login(token);
            logger.info("对用户[" + username + "]进行登录验证..验证通过");
        }catch(UnknownAccountException uae){
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
            return ApiResult.fail("未知账户");
        }catch(IncorrectCredentialsException ice){
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
            return ApiResult.fail("密码不正确");
        }catch(LockedAccountException lae){
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
            return ApiResult.fail("账户已锁定");
        }catch(ExcessiveAttemptsException eae){
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
            return ApiResult.fail("用户名或密码错误次数过多");
        }catch(AuthenticationException ae){
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景 
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
            return ApiResult.fail("用户名或密码不正确");
        }
        //验证是否登录成功
        if(subject.isAuthenticated()){
            logger.info("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            return ApiResult.success();
        }else{
            token.clear();
            return ApiResult.fail("登录失败");
        } 
    }
}
