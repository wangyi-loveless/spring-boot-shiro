package com.shayne.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shayne.domain.DictionaryType;

/**
 * 字典类型Service
 * @Author 王小张
 * @Date 2020年8月26日 下午2:33:20
 */
public interface DictionaryTypeService {

    /**
     * 查询
     * @param String typeCode
     * @return
     * DictionaryType
     */
	DictionaryType findByTypeCode(String typeCode);
    
    /**
     * 新增
     * @param DictionaryType dictionaryType
     * @return
     * DictionaryType
     */
    DictionaryType save(DictionaryType dictionaryType);
    
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
     * @return DictionaryType
     */
    DictionaryType find(Long id);
    
    /**
     * 查询
     * @return List<DictionaryType> 
     */
    List<DictionaryType> findAll();
    
    /**
     * 查询
     * @param Pageable pageable, String typeName
     * @return Page<DictionaryType>
     */
    Page<DictionaryType> find(Pageable pageable, String typeName);
    
}
