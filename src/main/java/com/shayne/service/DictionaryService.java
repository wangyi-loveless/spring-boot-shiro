package com.shayne.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shayne.domain.Dictionary;

/**
 * 字典Service
 * @Author 王小张
 * @Date 2020年8月26日 下午2:33:20
 */
public interface DictionaryService {

    /**
     * 查询
     * @param String dicCode
     * @return
     * Dictionary
     */
	Dictionary findByDicCode(String dicCode);
    
    /**
     * 新增
     * @param Dictionary dictionary
     * @return
     * Dictionary
     */
    Dictionary save(Dictionary dictionary);
    
    /**
     * 删除
     * @param Long id
     */
    void delete(Long id);
    
    /**
     * 删除-批量
     * @param Set<Long> 
     */
    void delete(Set<Long> ids);
    
    
    /**
     * 查询
     * @param Long id
     * @return Dictionary
     */
    Dictionary find(Long id);
    
    /**
     * 查询
     * @return List<Dictionary> 
     */
    List<Dictionary> findAll();
    
    /**
     * 查询
     * @param Pageable pageable, String dicName
     * @return Page<Dictionary>
     */
    Page<Dictionary> find(Pageable pageable, String dicName);
    
    /**
     * 查询所有菜单
     * @param String typeCode
     * @return List<Dictionary>
     */
    List<Dictionary> findByTypeCode(String typeCode);
}
