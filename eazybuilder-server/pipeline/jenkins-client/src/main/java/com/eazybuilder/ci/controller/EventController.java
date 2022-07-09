package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.base.PageResult;
import com.eazybuilder.ci.entity.QTeam;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.devops.Event;
import com.eazybuilder.ci.entity.devops.EventType;
import com.eazybuilder.ci.entity.devops.QEvent;
import com.eazybuilder.ci.repository.EventDao;
import com.eazybuilder.ci.repository.TeamDao;
import com.eazybuilder.ci.service.EventService;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.querydsl.core.types.Predicate;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event")
public class EventController extends CRUDRestController<EventService, Event>{

    @Autowired
    TeamServiceImpl teamServiceImpl;

    @Autowired
    TeamDao teamDao;

    @Autowired
    EventDao eventDao;

    @Override
    @RequestMapping(value="/page",method= RequestMethod.GET)
    @ApiOperation("按页查询")
    public PageResult<Event> page(
            @RequestParam(value="limit",defaultValue="10")int limit,
            @RequestParam(value="offset")int offset,
            @RequestParam(value="search",required=false)String searchText, HttpServletRequest request){


        Pageable pageable=PageRequest.of(Math.floorDiv(offset, limit), limit, Sort.Direction.DESC,"id");
        Page<Event> page=null;
        if(StringUtils.isNotBlank(searchText)){
            List<Team> teamList= (List)teamServiceImpl.findAll(QTeam.team.name.contains(searchText));
            Set<String> ids= teamList.stream().map(e->e.getId()).collect(Collectors.toSet());
            List<EventType> eventTypes= Arrays.stream(EventType.values()).filter(eventType -> eventType.getName().contains(searchText)).collect(Collectors.toList());
            Predicate predicate= QEvent.event.teamId.in(ids).or(QEvent.event.eventType.in(eventTypes));
            page=eventDao.findAll(predicate, pageable);
        }else {
            page=service.pageSearch(pageable, searchText);
        }
        PageResult<Event> result=PageResult.create(page.getTotalElements(), page.getContent());
        return result;
    }

}
