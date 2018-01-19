package com.shayne.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shayne.dao.MenuDao;
import com.shayne.domain.Menu;
import com.shayne.service.MenuService;

@Service
public class MenuServiceImpl implements  MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public Menu findByName(String name) {
        return menuDao.findByName(name);
    }

    @Override
    public Menu save(Menu menu) {
        return menuDao.save(menu);
    }

    @Override
    public void delete(Long id) {
        menuDao.delete(id);
    }

    @Override
    public List<Menu> find() {
        return menuDao.findByOrderBySequenceAsc();
    }

    @Override
    public List<Menu> findByPid(Long pid) {
        return menuDao.findByPidOrderBySequenceAsc(pid);
    }

    @Override
    public Page<Menu> find(Pageable pageable, String name) {
    	if(StringUtils.isBlank(name)) {
    		return menuDao.findAll(pageable);
    	}
    	return menuDao.findByNameLike(name, pageable);
    }

	@Override
	public Menu findOne(Long id) {
		return menuDao.findOne(id);
	}

	@Override
	public void delete(Set<Long> ids) {
		List<Menu> menus = menuDao.findByIdIn(ids);
		if(null != menus) {
			menuDao.deleteInBatch(menus);
		}
	}
}
