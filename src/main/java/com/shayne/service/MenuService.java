package com.shayne.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shayne.domain.Menu;

/**
 * 系统菜单service
 * @Author WY
 * @Date 2018年1月4日
 */
public interface MenuService {

    /**
     * 查询
     * @param name
     * @return
     * Menu
     */
    Menu findByName(String name);
    
    /**
     * 新增
     * @param menu
     * @return
     * Menu
     */
    Menu save(Menu menu);
    
    /**
     * 删除
     * @return
     * Long
     */
    void delete(Long id);
    
    /**
     * 删除-批量
     * @return
     * Long
     */
    void delete(Set<Long> ids);
    
    
    /**
     * 查询
     * @param id
     * @return
     */
    Menu findOne(Long id);
    
    /**
     * 查询
     * @param id
     * @return
     * Menu
     */
    List<Menu> find();
    
    /**
     * 查询
     * @param id
     * @return
     * Menu
     */
    Page<Menu> find(Pageable pageable, String name);
    
    /**
     * 查询所有用户
     * @return
     * Set<User>
     */
    List<Menu> findByPid(Long pid);
}
