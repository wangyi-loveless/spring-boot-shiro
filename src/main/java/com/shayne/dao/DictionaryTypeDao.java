package com.shayne.dao;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shayne.domain.DictionaryType;

/**
 * 字典类型DAO
 * @Author 王小张
 * @Date 2020年8月26日 下午2:12:19
 */
public interface DictionaryTypeDao extends JpaRepository<DictionaryType, Long>, JpaSpecificationExecutor<DictionaryType> {

	/**
	 * 通过名称模糊查询
	 * @param typeName
	 * @param pageable 
	 * @return Page<DictionaryType>
	 */
	Page<DictionaryType> findByTypeNameLike(String typeName, Pageable pageable);

	/**
	 * 通过类型code查询类型
	 * @param String typeCode
	 * @return DictionaryType
	 */
	DictionaryType findByTypeCode(String typeCode);

	/**
	 * 通过ID集合删除数据
	 * @param ids
	 */
	@Transactional
	void deleteByIdIn(Set<Long> ids);
}
