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
import com.shayne.domain.Role;
import com.shayne.domain.vo.ApiResult;
import com.shayne.domain.vo.PageData;
import com.shayne.domain.vo.PageImpl;
import com.shayne.domain.vo.RoleMenuVo;
import com.shayne.service.RoleService;

/**
 * 角色管理控制器：角色增删改查操作
 * @Author 王小张
 * @Date 2020年8月19日 下午6:59:22
 */
@RestController
@RequestMapping(value=ApiCons.API + ApiCons.SEPARATOR + ApiCons.ROLE)
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    
    /**
     * 新增角色
     * @param role
     * @return
     * String
     */
    @PostMapping
    public ApiResult<String> save(@RequestBody Role role) {
    	//角色信息加密
    	String msg = "SUCCESS";
    	if(null == role) {
    		msg = "角色信息为空！";
    		ApiResult.fail(msg);
    	}
    	role.setCreateTime(new Date());
        
        //角色信息保存
        try {
			role = roleService.save(role);
			return new ApiResult<>(msg);
		} catch (Exception e) {
			logger.error("保存角色发生异常：");
			msg = e.getMessage();
			e.printStackTrace();
		}
        return ApiResult.fail(msg);
    }
    
    /**
     * 删除角色
     * @param String id
     * @return ApiResult<String>
     */
    @DeleteMapping
    public ApiResult<String> delete(@RequestParam(value="id")  String ids) {
    	ApiResult<String> result = new ApiResult<String>();
		String msg = "SUCCESS";
		try {
			String[] idsArr = ids.split(",");
			Set<Long> idsSet = new HashSet<>();
			for (String id : idsArr) {
				idsSet.add(new Long(id));
			}
			roleService.delete(idsSet);
			result.setMsg(msg);
			result.setResult(true);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setMsg(msg);
			result.setResult(true);
			logger.error("删除角色发生系统异常：");
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 查询角色
     * @param id
     * @return
     * Role
     */
    @GetMapping
    public ApiResult<Role> get(@RequestParam(value="id") Long id) {
    	ApiResult<Role> result = new ApiResult<Role>();
    	Role role = null;
		try {
			role = roleService.find(id);
			result.setData(role);
			result.setResult(true);
		} catch (Exception e) {
			String msg = e.getMessage();
			logger.error("查询角色发生异常：");
			e.printStackTrace();
			result.setResult(false);
			result.setMsg(msg);
		}
		return result;
    }
    
    /**
     * 查询菜单
     * @return
     * String
     */
    @GetMapping(value="page")
    public PageData<Role> page(
    		@RequestParam(value="page") Integer page,
            @RequestParam(value="limit") Integer limit,
            @RequestParam(value="queryKey", required=false) String rolename
            ) {
    	PageData<Role> pageData = new PageData<>();
        try {
			PageImpl pageImpl = new PageImpl(page, limit);
			Page<Role> p = roleService.pageList(pageImpl, rolename);
			pageData.setCode(0);
			pageData.setCount(p.getTotalElements());
			pageData.setData(p.getContent());
			pageData.setMsg("SUCCESS");
		} catch (Exception e) {
			logger.error("查询角色列表发生系统异常：");
			e.printStackTrace();
		}
        return pageData;
    }
    
    /**
     * 角色授权
     * @param sysRole
     * @return
     * String
     */
    @PostMapping(value="grant")
    public ApiResult<Role> grant(@RequestBody RoleMenuVo roleMenu) {
    	ApiResult<Role> result = new ApiResult<Role>();
        Role role = null;
		String msg = "SUCCESS";
		try {
			Set<Long> menuIdSet = new HashSet<Long>();
			String menuIds = roleMenu.getMenuIds();
			String roleIdStr = roleMenu.getRoleId();
			Long roleId = new Long(roleIdStr);
			String[] menuArray = menuIds .split(",");
			for (String menuId : menuArray) {
				if(StringUtils.isNotBlank(menuId)) {
					menuIdSet.add(new Long(menuId));
				}
			}
			role = roleService.grant(roleId, menuIdSet);
			result.setData(role);
			result.setMsg(msg );
			result.setResult(true);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setData(role);
			result.setMsg(msg);
			result.setResult(false);
			logger.error("角色授权发生异常：");
			e.printStackTrace();
		}
        return result;
    }
}
