package com.shayne.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shayne.constans.ApiCons;
import com.shayne.service.RoleService;
import com.shayne.service.UserRoleService;

/**
 * 登录控制器
 * @Author WY
 * @Date 2017年12月13日
 */
@Controller
public class ViewController extends BaseController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	/**
     * 登录页
     * @return ModelAndView
     */
    @GetMapping(value=ApiCons.LOGIN)
    public ModelAndView login() {
        return baseModelAndView(ApiCons.LOGIN);
    }
    
    /**
     * 登出
     * @return ModelAndView
     */
    @GetMapping(value=ApiCons.LOGOUT)
    public ModelAndView logout() {
        //获取当前的Subject  
        Subject subject = SecurityUtils.getSubject();
        if(null == subject) {
            return baseModelAndView(ApiCons.LOGIN);
        }
        Session session = subject.getSession();
        if(null == session) {
            return baseModelAndView(ApiCons.LOGIN);
        }
        subject.logout();
        return baseModelAndView(ApiCons.LOGIN);
    }
	
    /**
     * 首页
     * @return ModelAndView
     */
    @GetMapping(value=ApiCons.INDEX)
    public ModelAndView index() {
        return baseUserModelAndView(ApiCons.INDEX);
    }
    
    /**
     * 菜单管理
     * @return ModelAndView
     */
    @GetMapping(value=ApiCons.MENU)
    public ModelAndView menu() {
        return baseModelAndView(ApiCons.MENU);
    }
    
    /**
     * 菜单管理
     * @param Long roleId
     * @return ModelAndView
     */
    @GetMapping(value=ApiCons.ROLEMENU)
    public ModelAndView rolemenu(
    		@RequestParam(value = "roleId", required = false) Long roleId) {
    	ModelAndView mv = baseModelAndView(ApiCons.ROLEMENU);
    	String menuIds = "";
		if(null != roleId) {
			menuIds = roleService.getGrantMenus(roleId);
		}
    	mv.addObject("menuIds", menuIds );
    	return mv;
    }
    
    /**
     * 用户管理
     * @return ModelAndView
     */
    @GetMapping(value=ApiCons.USER)
    public ModelAndView user() {
    	return baseModelAndView(ApiCons.USER);
    }
    
    /**
     * 角色管理
     * @return ModelAndView
     */
    @GetMapping(value=ApiCons.ROLE)
    public ModelAndView role() {
    	return baseModelAndView(ApiCons.ROLE);
    }
    
    /**
     * 用户角色管理
     * @param Long userId
     * @return ModelAndView
     */
    @GetMapping(value=ApiCons.USERROLE)
    public ModelAndView userrole(
    		@RequestParam(value = "userId", required = false) Long userId) {
    	ModelAndView mv = baseModelAndView(ApiCons.USERROLE);
    	String roleIds = "";
		if(null != userId) {
			roleIds = userRoleService.getGrantRoles(userId);
		}
    	mv.addObject("roleIds", roleIds );
    	return mv;
    }
    
    /**
     * 字典类型管理
     * @return ModelAndView
     */
    @GetMapping(value=ApiCons.DICTYPE)
    public ModelAndView dictype() {
    	return baseModelAndView(ApiCons.DICTYPE);
    }
    
    /**
     * 字典管理
     * @return ModelAndView
     */
    @GetMapping(value=ApiCons.DICTIONARY)
    public ModelAndView dictionary() {
    	return baseModelAndView(ApiCons.DICTIONARY);
    }
}
