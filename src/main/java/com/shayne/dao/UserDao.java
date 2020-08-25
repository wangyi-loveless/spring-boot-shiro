package com.shayne.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shayne.domain.User;

/**
 * 系统用户DAO
 * @Author WY
 * @Date 2017年12月7日
 */
public interface UserDao extends JpaRepository<User, Long>, 
    JpaSpecificationExecutor<User> {

	/**
	 * 查询用户
	 * @param username
	 * @return
	 */
    User findByUsername(String username);

    /**
     * 通过账号查询分页数据
     * @param username
     * @param pageable
     * @return
     */
	Page<User> findByUsernameLike(String username, Pageable pageable);

	/**
	 * 通过ID查询
	 * @param ids
	 * @return
	 */
	List<User> findByIdIn(Set<Long> ids);
	
	/**
	 * 通过ID批量删除
	 * @param ids
	 * @return
	 */
	Long deleteByIdIn(Set<Long> ids);
}
