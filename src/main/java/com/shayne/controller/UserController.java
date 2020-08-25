package com.shayne.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shayne.constans.ApiCons;
import com.shayne.domain.User;
import com.shayne.domain.vo.ApiResult;
import com.shayne.domain.vo.PageData;
import com.shayne.domain.vo.PageImpl;
import com.shayne.domain.vo.UserRoleVo;
import com.shayne.service.UserService;

/**
 * 用户管理控制器：用户增删改查操作
 * @Author WY
 * @Date 2017年12月16日
 */
@RestController
@RequestMapping(value=ApiCons.API + ApiCons.SEPARATOR + ApiCons.USER)
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    
    /**
     * 新增用户
     * @param user
     * @return
     * String
     */
    @PostMapping
    public ApiResult<String> save(@RequestBody User user) {
    	//用户信息加密
    	String msg = "SUCCESS";
    	
    	if(null == user) {
    		msg = "用户信息为空！";
    		ApiResult.fail(msg);
    	}
    	user.setCreateTime(new Date());
        try {
			user = super.encrypt(user);
		} catch (Exception e) {
			logger.error("用户密码加密发生异常：");
			msg = e.getMessage();
			e.printStackTrace();
			return ApiResult.fail(msg);
		}
        
        //用户信息保存
        try {
			user = userService.save(user);
			return new ApiResult<>(msg);
		} catch (Exception e) {
			logger.error("保存用户发生异常：");
			msg = e.getMessage();
			e.printStackTrace();
		}
        return ApiResult.fail(msg);
    }
    
    /**
     * 删除用户
     * @param sysUser
     * @return
     * String
     */
    @DeleteMapping
    public ApiResult<String> delete(@RequestParam(value="id")  String ids) {
    	ApiResult<String> result = new ApiResult<String>();
    	String msg = "SUCCESS";
    	result.setMsg(msg);
    	result.setResult(true);
		String[] idsArr = ids.split(",");
		Set<Long> idsSet = new HashSet<>();
		for (String id : idsArr) {
			if(StringUtils.isNotBlank(id)) {
				idsSet.add(new Long(id));
			}
		}
		try {
			userService.delete(idsSet);
		} catch (Exception e) {
			result.setResult(false);
			msg = e.getMessage();
			logger.error("删除用户发生异常：");
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 查询用户
     * @param id
     * @return
     * User
     */
    @GetMapping
    public ApiResult<User> get(@RequestParam(value="id") Long id) {
    	ApiResult<User> result = new ApiResult<User>();
    	User user = null;
		try {
			user = userService.find(id);
			result.setData(user);
			result.setResult(true);
			return result;
		} catch (Exception e) {
			String msg = e.getMessage();
			logger.error("查询用户发生异常：");
			e.printStackTrace();
			result.setResult(false);
			result.setMsg(msg);
			return result;
		}
    }
    
    /**
     * 查询菜单
     * @return
     * String
     */
    @GetMapping(value="page")
    public PageData<User> page(
    		@RequestParam(value="page") Integer page,
            @RequestParam(value="limit") Integer limit,
            @RequestParam(value="queryKey", required=false) String username
            ) {
        PageImpl pageImpl = new PageImpl(page, limit);
        Page<User> p = userService.pageList(pageImpl, username);
        PageData<User> pageData = new PageData<>();
        pageData.setCode(0);
        pageData.setCount(p.getTotalElements());
        pageData.setData(p.getContent());
        pageData.setMsg("SUCCESS");
        return pageData;
    }
    
    /**
     * 用户授权
     * @param sysUser
     * @return
     * String
     */
    @PostMapping(value="grant")
    public ApiResult<User> grant(@RequestBody UserRoleVo userRoleVo) {
    	ApiResult<User> result = new ApiResult<User>();
    	result.setResult(true);
    	User user = null;
    	String msg = "SUCCESS";
    	result.setMsg(msg);
    	String roleIds = userRoleVo.getRoleIds();
        Set<Long> roleIdSet = new HashSet<Long>();
        String[] roleids = roleIds.split(",");
        for (String roleId : roleids) {
        	if(StringUtils.isNotBlank(roleId)){
        		roleIdSet.add(new Long(roleId));
        	}
        }
        Long userId = userRoleVo.getUserId();
        try {
			user = userService.grant(userId, roleIdSet);
			if(null == user) {
				msg = "授权失败";
				result.setResult(false);
				result.setMsg(msg);
			}
		} catch (Exception e) {
			msg = e.getMessage();
			result.setResult(false);
			result.setMsg(msg);
			logger.error("用户授权发生异常：");
			e.printStackTrace();
		}
        result.setData(user);
        return result;
    }
}
