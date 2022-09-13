package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.SecondParty;
import com.eazybuilder.ci.service.SecondPartyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secondParty")
public class SecondPartyController extends CRUDRestController<SecondPartyService,SecondParty>{

}
