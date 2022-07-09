package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.Guard;
import com.eazybuilder.ci.entity.ThresholdType;
import com.eazybuilder.ci.service.GuardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/guard")
public class GuardController extends CRUDRestController<GuardService, Guard>{


    @GetMapping(value="/getGuardMap")
    public Map<ThresholdType,List<Guard>> getGuardMap(){
        List<Guard> guardList =  (List<Guard>)service.findAll();
        return  guardList.stream().collect(Collectors.groupingBy(Guard::getGuardType));
    }
}
