package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.DtpReport;
import com.eazybuilder.ci.service.DtpReportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dtpReport")
public class DtpReportController extends CRUDRestController<DtpReportService, DtpReport>{

}
