package com.shayne.dao;

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

    User findByUsername(String username);
}
