package com.eazybuilder.ci.repository;

import com.eazybuilder.ci.entity.Guard;
import com.eazybuilder.ci.entity.WarnRule;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardDao extends BaseDao<Guard, String>{

}
