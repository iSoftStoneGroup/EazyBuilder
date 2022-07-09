package com.eazybuilder.ci.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.AuditProfile;
import com.eazybuilder.ci.service.AuditProfileService;

@RestController
@RequestMapping("/api/audit-profile")
public class AuditProfileController extends CRUDRestController<AuditProfileService, AuditProfile>{

}
