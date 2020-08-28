package com.shayne.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shayne.dao.DictionaryTypeDao;
import com.shayne.domain.DictionaryType;
import com.shayne.service.DictionaryTypeService;

@Service
public class DictionaryTypeServiceImpl implements  DictionaryTypeService {

    @Autowired
    private DictionaryTypeDao dictionaryTypeDao;

	@Override
	public DictionaryType findByTypeCode(String typeCode) {
		return dictionaryTypeDao.findByTypeCode(typeCode);
	}

	@Override
	public DictionaryType save(DictionaryType dictionaryType) {
		
		return dictionaryTypeDao.save(dictionaryType);
	}

	@Override
	public void delete(Long id) {
		dictionaryTypeDao.delete(id);
	}

	@Override
	public void delete(Set<Long> ids) {
		dictionaryTypeDao.deleteByIdIn(ids);
	}

	@Override
	public DictionaryType find(Long id) {
		return dictionaryTypeDao.findOne(id);
	}

	@Override
	public List<DictionaryType> findAll() {
		return dictionaryTypeDao.findAll();
	}

	@Override
	public Page<DictionaryType> find(Pageable pageable, String typeName) {
		if(StringUtils.isBlank(typeName)) {
			return dictionaryTypeDao.findAll(pageable);
		}
		return dictionaryTypeDao.findByTypeNameLike(typeName, pageable);
	}
}
