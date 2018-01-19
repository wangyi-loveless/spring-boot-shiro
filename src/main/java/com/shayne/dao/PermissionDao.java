package com.shayne.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shayne.domain.Operation;

/**
 * 系统权限DAO
 * @Author WY
 * @Date 2017年12月7日
 */
public interface PermissionDao extends JpaRepository<Operation, Long>, JpaSpecificationExecutor<Operation> {

}
