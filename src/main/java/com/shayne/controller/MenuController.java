package com.shayne.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.shayne.domain.Menu;
import com.shayne.domain.User;
import com.shayne.domain.vo.ApiResult;
import com.shayne.domain.vo.PageData;
import com.shayne.domain.vo.PageImpl;
import com.shayne.service.MenuService;
import com.shayne.service.RoleMenuService;

/**
 * 菜单管理控制器：菜单增删改查操作
 * @Author WY
 * @Date 2018年1月4日
 */
@RestController
@RequestMapping(value=ApiCons.API + ApiCons.SEPARATOR + ApiCons.MENU)
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;
    
    @Autowired
    private RoleMenuService roleMenuService;
    
    /**
     * 新增菜单
     * @param Menu menu
     * @return ApiResult<Menu>
     */
    @PostMapping
    public ApiResult<Menu> add(@RequestBody Menu menu) {
    	ApiResult<Menu> result = new ApiResult<Menu>();
    	String msg = "SUCCESS";
    	result.setMsg(msg);
        try {
			menu = menuService.save(menu);
			result.setResult(true);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setMsg(msg);
			result.setResult(true);
			logger.error("保存菜单发生系统异常：");
			e.printStackTrace();
		}
        result.setData(menu);
        return result;
    }
    
    /**
     * 批量删除
     * @param String ids
     * @return ApiResult<String>
     */
    @DeleteMapping
    public ApiResult<String> delete(@RequestParam("id") String ids) {
    	ApiResult<String> result = new ApiResult<String>();
    	String msg = "SUCCESS";
    	if(StringUtils.isBlank(ids)) {
    		msg = "ID为空，无删除数据!";
    		result.setMsg(msg);
    		result.setResult(false);
    		return result;
    	}
		String[] idsArr = ids.split(",");
		Set<Long> idsSet = new HashSet<>();
		boolean deleteAbleFlag = true;
		for (String id : idsArr) {
			if(StringUtils.isNotBlank(id)) {
				Long menuId = new Long(id); 
				Long cnt = roleMenuService.contByMenuId(menuId);
				if(cnt>0) {
					deleteAbleFlag = false;
					msg = "ID为"+id+"的菜单已授权给在用角色，请先处理授权再删除数据!";
					break;
				}
				idsSet.add(menuId);
			}
		}
		// 判断菜单是否已经授权，授权的不可删除
		if(!deleteAbleFlag) {
			result.setMsg(msg);
    		result.setResult(false);
			return result;
		}
		
		try {
			menuService.delete(idsSet);
			result.setResult(true);
		} catch (Exception e) {
			msg = e.getMessage();
			result.setMsg(msg);
    		result.setResult(false);
			logger.error("删除数据发生异常：");
			e.printStackTrace();
		}
		return result;
    }
    
    /**
     * 查询分页数据
     * @param Integer page
     * @param Integer limit
     * @param String name
     * @return PageData<Menu>
     */
    @GetMapping
    public PageData<Menu> page(
    		@RequestParam(value="page") Integer page,
            @RequestParam(value="limit") Integer limit,
            @RequestParam(value="queryKey", required=false) String name
            ) {
        PageImpl pageImpl = new PageImpl(page, limit);
		if(StringUtils.isNotBlank(name)) {
			name = "%" + name + "%";
		}
        Page<Menu> p = menuService.find(pageImpl, name);
        PageData<Menu> pageData = new PageData<>();
        pageData.setCode(0);
        pageData.setCount(p.getTotalElements());
        pageData.setData(p.getContent());
        pageData.setMsg("SUCCESS");
        return pageData;
    }
    
    /**
     * 查询用户的权限菜单
     * @return ApiResult<List<Menu>>
     */
    @GetMapping(value="/user")
    public ApiResult<List<Map<String, Object>>> user() {
    	ApiResult<List<Map<String, Object>>> result = new ApiResult<>();
    	String msg = "SUCCESS";
    	result.setResult(true);
    	result.setMsg(msg);
    	User user = null;
		try {
			user = getCurrentUser();
		} catch (Exception e) {
			msg = "用户未登录";
			result.setResult(false);
			result.setMsg(msg);
			logger.error("获取用户信息发生异常：");
			e.printStackTrace();
		}
    	// 查询当前用户的所有权限
        List<Menu> menuList = null;
        
		// 获取用户对应权限菜单
        try {
			menuList = menuService.findByUser(user);
		} catch (Exception e) {
			msg = "获取用户授权菜单异常";
			result.setMsg(msg);
			result.setResult(false);
			logger.error("用户角色授权异常:");
			e.printStackTrace();
		}
        //构造好的菜单数据
        List<Map<String, Object>> menus = null;
		try {
			menus = generatorMenu(menuList);
			if(null == menus || menus.size() ==0) {
				msg = "用户未授权";
				result.setResult(false);
				result.setMsg(msg);
			}
		} catch (Exception e) {
			msg = "获取用户授权菜单异常";
			result.setMsg(msg);
			result.setResult(false);
			logger.error("用户角色授权异常:");
			e.printStackTrace();
		}
        result.setData(menus);
        return result;
    }
    
    /**
     * 查询用户所有父级菜单
     * @return ApiResult<List<Menu>>
     */
    @GetMapping(value="/parent")
    public List<Menu> parent() {
        //所有菜单数据，后续会添加权限过滤
        return menuService.findByPid(0l);
    }
    
    /**
     * 通过ID查询单条数据
     * @param Long id
     * @return
     */
    @GetMapping(value="/one")
    public ApiResult<Menu> one(@RequestParam(value="id") Long id) {
    	ApiResult<Menu> result = new ApiResult<Menu>();
		try {
			Menu menu = menuService.findOne(id);
			result.setData(menu);
			result.setMsg("SUCCESS");
			result.setResult(true);
		} catch (Exception e) {
			result.setMsg(e.getMessage());
			result.setResult(false);
			logger.error("查询用户发生异常：");
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 转换菜单数据结构
     * @param List<Menu> menuList
     * @return List<Map<String, Object>>
     */
    private List<Map<String, Object>> generatorMenu(List<Menu> menuList) {
        if(null == menuList || menuList.isEmpty()) {
            return null;
        }
        //菜单结果
        List<Map<String, Object>> menus = new ArrayList<>();
        //查询所有父级菜单，父级id为0
        List<Menu> parentMenu = getChildMenu(menuList, 0l);
        for (Menu menu : parentMenu) {
            Map<String, Object> map = menuMap(menuList, menu);
            menus.add(map);
        }
        return menus;
    }
    
    /**
     * 菜单数据构造
     * @param List<Menu> list
     * @param Menu menu
     * @return Map<String,Object>
     */
    private Map<String, Object> menuMap(List<Menu> list, Menu menu) {
        Map<String, Object> map = new HashMap<>();
        Long id = menu.getId();
        map.put("id", id);
        map.put("name", menu.getName());
        map.put("url", menu.getUrl());
        map.put("icon", menu.getIcon());
        List<Menu> childMenu = getChildMenu(list, id);
        if(null == childMenu || childMenu.isEmpty()) {
            map.put("child", new ArrayList<>());
        }else {
            List<Map<String, Object>> child = getChildMenus(list, id);
            map.put("child", child);
        }
        return map;
    }
    
    /**
     * 迭代查询所有菜单
     * @param List<Menu> list
     * @param Long pid
     * @return List<Map<String,Object>>
     */
    private List<Map<String, Object>> getChildMenus(List<Menu> list, Long pid) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if(null == list) {
            return mapList;
        }
        //遍历来自pid的所有子菜单
        List<Menu> menuList = getChildMenu(list, pid);
        for (Menu menu : menuList) {
            Map<String, Object> map = new HashMap<>();
            Long id = menu.getId();
            map.put("id", id);
            map.put("name", menu.getName());
            map.put("url", menu.getUrl());
            map.put("icon", menu.getIcon());
            //迭代条件
            List<Menu> childMenu = getChildMenu(list, id);
            if(null == childMenu || childMenu.isEmpty()) {
                map.put("child", new ArrayList<>());
            }else {
                //查询所有id的子菜单
                List<Map<String, Object>> child = getChildMenus(list, id);
                map.put("child", child);
            }
            mapList.add(map);
        }
        return mapList;
    }
    
    /**
     * 获取子菜单数据
     * @param List<Menu> list
     * @param Long pid
     * @return List<Menu>
     */
    private List<Menu> getChildMenu(List<Menu> list, Long pid) {
        if(list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().filter( menu -> menu.getPid() == pid).collect(Collectors.toList());
    }
}
