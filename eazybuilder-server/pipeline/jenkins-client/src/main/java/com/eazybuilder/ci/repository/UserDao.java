package com.eazybuilder.ci.repository;

import org.springframework.stereotype.Repository;

import com.eazybuilder.ci.entity.User;

@Repository
public interface UserDao extends BaseDao<User, String>{

}
