package com.eazybuilder.ci.repository;

import com.eazybuilder.ci.entity.devops.Event;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDao extends BaseDao<Event, String>{

}
