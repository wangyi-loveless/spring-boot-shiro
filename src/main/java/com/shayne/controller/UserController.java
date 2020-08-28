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
import com.shayne.domain.vo.ResetPasswordUserVo;
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
     * 新增/更新
     * @param User user
     * @return ApiResult<String>
     */
    @PostMapping
    public ApiResult<String> save(@RequestBody User user) {
    	ApiResult<String> result = new ApiResult<String>();
    	//用户信息加密
    	String msg = "SUCCESS";
    	result.setMsg(msg);
    	result.setResult(true);
    	
    	if(null == user) {
    		msg = "用户信息为空！";
    		result.setMsg(msg);
    		result.setResult(false);
    	}
    	// 写入创建时间
    	if(null == user.getCreateTime()) {
    		user.setCreateTime(new Date());
    	}
        try {
			user = super.encrypt(user);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setMsg(msg);
			result.setResult(false);
			logger.error("用户密码加密发生异常：");
			e.printStackTrace();
		}
        
        //用户信息保存
        try {
			user = userService.save(user);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setMsg(msg);
			result.setResult(false);
			logger.error("保存用户发生异常：");
			e.printStackTrace();
		}
        return result;
    }
    
    /**
     * 重置密码
     * @param User user
     * @return ApiResult<String>
     */
    @PostMapping(value = "reset")
    public ApiResult<String> reset(@RequestBody ResetPasswordUserVo resetPasswordUserVo) {
    	ApiResult<String> result = new ApiResult<String>();
    	//用户信息加密
    	String msg = "SUCCESS";
    	result.setMsg(msg);
    	result.setResult(true);
    	
    	// 入参为空
    	if(null == resetPasswordUserVo) {
    		msg = "密码不能为空！";
    		result.setMsg(msg);
    		result.setResult(false);
    		return result;
    	}
    	
    	// 判断用户是否存在
    	Long userId = resetPasswordUserVo.getId();
    	User user = userService.find(userId);
    	if(null == user) {
    		msg = "用户不存在！";
    		result.setMsg(msg);
    		result.setResult(false);
    		return result;
    	}
    	
    	// 判断旧密码是否正确
    	String salt = user.getSalt();
    	String oldUserPassWord = user.getPassword();
    	String oldPassWord = resetPasswordUserVo.getOldPassword();
    	String oldEncryptPassWord = md5Util.encrypt(oldPassWord, salt);
    	if(!StringUtils.equalsIgnoreCase(oldUserPassWord, oldEncryptPassWord)) {
    		msg = "旧密码不正确！";
    		result.setMsg(msg);
    		result.setResult(false);
    		return result;
    	}
    	
    	// 判断用户密码是否和历史相同
    	String newPassWord = resetPasswordUserVo.getNewPassword();
    	String encryptNewPassword = md5Util.encrypt(newPassWord, salt);
    	if(StringUtils.equalsIgnoreCase(encryptNewPassword, oldUserPassWord)) {
    		msg = "新密码不能和旧密码相同！";
    		result.setMsg(msg);
    		result.setResult(false);
    		return result;
    	}
    	
    	// 写入新密码
    	user.setPassword(encryptNewPassword);
        
        //用户信息保存
        try {
			user = userService.save(user);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setMsg(msg);
			result.setResult(false);
			logger.error("更新用户密码发生系统异常：");
			e.printStackTrace();
		}
        return result;
    }
    
    /**
     * 批量删除
     * @param String ids
     * @return ApiResult<String>
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
			boolean hasRole = userService.hasRole(idsSet);
			if(hasRole) {
				msg = "用户有授权角色，请先清除授权再删除";
				result.setMsg(msg);
				result.setResult(false);
			}else {
				userService.delete(idsSet);
			}
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
     * @param Long id
     * @return ApiResult<User>
     */
    @GetMapping
    public ApiResult<User> get(@RequestParam(value="id") Long id) {
    	ApiResult<User> result = new ApiResult<User>();
    	User user = null;
		try {
			user = userService.find(id);
			result.setData(user);
			result.setResult(true);
		} catch (Exception e) {
			String msg = e.getMessage();
			logger.error("查询用户发生异常：");
			e.printStackTrace();
			result.setResult(false);
			result.setMsg(msg);
		}
		return result;
    }
    
    /**
     * 查询分页数据
     * @param Integer page
     * @param Integer limit
     * @param String username
     * @return PageData<User>
     */
    @GetMapping(value="page")
    public PageData<User> page(
    		@RequestParam(value="page") Integer page,
            @RequestParam(value="limit") Integer limit,
            @RequestParam(value="queryKey", required=false) String username
            ) {
        PageImpl pageImpl = new PageImpl(page, limit);
		if(StringUtils.isNotBlank(username)) {
			username = "%" + username + "%";
		}
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
     * @param UserRoleVo userRoleVo
     * @return ApiResult<User>
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
