package com.shayne.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shayne.dao.DictionaryDao;
import com.shayne.domain.Dictionary;
import com.shayne.service.DictionaryService;

@Service
public class DictionaryServiceImpl implements  DictionaryService {

    @Autowired
    private DictionaryDao dictionaryDao;

	@Override
	public Dictionary findByDicCode(String dicCode) {
		return dictionaryDao.findByDicCode(dicCode);
	}

	@Override
	public Dictionary save(Dictionary dictionary) {
		
		return dictionaryDao.save(dictionary);
	}

	@Override
	public void delete(Long id) {
		dictionaryDao.delete(id);
	}

	@Override
	public void delete(Set<Long> ids) {
		dictionaryDao.deleteByIdIn(ids);
	}

	@Override
	public Dictionary find(Long id) {
		return dictionaryDao.findOne(id);
	}

	@Override
	public List<Dictionary> findAll() {
		return dictionaryDao.findAll();
	}

	@Override
	public Page<Dictionary> find(Pageable pageable, String dicName) {
		if(StringUtils.isBlank(dicName)) {
			return dictionaryDao.findAll(pageable);
		}
		return dictionaryDao.findByDicNameLike(dicName, pageable);
	}

	@Override
	public List<Dictionary> findByTypeCode(String typeCode) {
		return dictionaryDao.findByTypeCode(typeCode);
	}
}
