package com.shayne.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shayne.domain.Menu;

/**
 * 系统用户DAO
 * @Author WY
 * @Date 2017年12月7日
 */
public interface MenuDao extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

    List<Menu> findByPidOrderBySequenceAsc(Long pid);
    
    List<Menu> findByOrderBySequenceAsc();
    
    Menu findByName(String name);
    
    List<Menu> findByNameLike(String name);
    
    List<Menu> findByIdIn(Set<Long> ids);
    
    Page<Menu> findByNameLike(String name, Pageable pageble);
}
