package com.shayne.dao;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shayne.domain.Dictionary;

/**
 * 字典DAO
 * @Author 王小张
 * @Date 2020年8月26日 下午2:12:19
 */
public interface DictionaryDao extends JpaRepository<Dictionary, Long>, JpaSpecificationExecutor<Dictionary> {

	/**
	 * 通过名称模糊查询
	 * @param dicName
	 * @param pageable 
	 * @return Page<Dictionary>
	 */
	Page<Dictionary> findByDicNameLike(String dicName, Pageable pageable);

	/**
	 * 通过字典编码查询字典信息
	 * @param dicCode
	 * @return
	 */
	Dictionary findByDicCode(String dicCode);

	/**
	 * 通过ID批量删除
	 * @param ids
	 */
	@Transactional
	void deleteByIdIn(Set<Long> ids);

	/**
	 * 通过字典类型查询字典列表
	 * @param String typeCode
	 * @return List<Dictionary>
	 */
	List<Dictionary> findByTypeCode(String typeCode);
}
