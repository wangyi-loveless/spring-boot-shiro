package com.shayne.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.shayne.domain.vo.ApiResult;
import com.shayne.domain.vo.PageData;
import com.shayne.domain.vo.PageImpl;
import com.shayne.service.MenuService;

/**
 * 菜单管理控制器：菜单增删改查操作
 * @Author WY
 * @Date 2018年1月4日
 */
@RestController
@RequestMapping(value=ApiCons.MENU_OPER)
public class MenuController extends BaseController {

    @Autowired
    private MenuService  menuService;
    
    /**
     * 新增菜单
     * @param menu
     * @return
     * String
     */
    @PostMapping
    public ApiResult<Menu> add(@RequestBody Menu menu) {
        menu = menuService.save(menu);
        return new ApiResult<>(menu);
    }
    
    /**
     * 删除菜单-批量
     * @param sysMenu
     * @return
     * String
     */
    @DeleteMapping
    public ApiResult<String> delete(@RequestParam("id") String ids) {
    	if(!ids.contains(",")) {
    		menuService.delete(new Long(ids));
    	}else {
    		String[] idsArr = ids.split(",");
    		Set<Long> idsSet = new HashSet<>();
    		for (String id : idsArr) {
    			idsSet.add(new Long(id));
    		}
    		menuService.delete(idsSet);
    	}
    	return ApiResult.success();
    }
    
    /**
     * 查询菜单
     * @return
     * String
     */
    @GetMapping
    public PageData<Menu> find(
    		@RequestParam(value="page") Integer page,
            @RequestParam(value="limit") Integer limit,
            @RequestParam(value="queryKey", required=false) String name
            ) {
        PageImpl pageImpl = new PageImpl(page, limit);
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
     * @return
     * ApiResult<List<Menu>>
     */
    @GetMapping(value="/user")
    public ApiResult<List<Map<String, Object>>> user() {
        //所有菜单数据，后续会添加权限过滤
        List<Menu> allMenu = menuService.find();
        //构造好的菜单数据
        List<Map<String, Object>> menus = generatorMenu(allMenu);
        return new ApiResult<>(menus);
    }
    
    /**
     * 查询用户所有父级菜单
     * @return
     * ApiResult<List<Menu>>
     */
    @GetMapping(value="/parent")
    public List<Menu> parent() {
        //所有菜单数据，后续会添加权限过滤
        return menuService.findByPid(0l);
    }
    
    /**
     * 查询通过id
     * @return
     * ApiResult<List<Menu>>
     */
    @GetMapping(value="/one")
    public ApiResult<Menu> one(@RequestParam(value="id") Long id) {
    	//所有菜单数据，后续会添加权限过滤
    	Menu menu = menuService.findOne(id);
    	return new ApiResult<>(menu);
    }
    
    /**
     * 构造菜单
     * @param menuList
     * @return
     * List<Map<String,Object>>
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
     * @param list
     * @param menu
     * @return
     * Map<String,Object>
     */
    private Map<String, Object> menuMap(List<Menu> list, Menu menu) {
        Map<String, Object> map = new HashMap<>();
        Long id = menu.getId();
        map.put("id", id);
        map.put("name", menu.getName());
        map.put("url", menu.getUrl());
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
     * @param list
     * @param pid
     * @return
     * List<Map<String,Object>>
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
     * @param list
     * @param pid
     * @return
     * List<Menu>
     */
    private List<Menu> getChildMenu(List<Menu> list, Long pid) {
        if(list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().filter( menu -> menu.getPid() == pid).collect(Collectors.toList());
    }
}
