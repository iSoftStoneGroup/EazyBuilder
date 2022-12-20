package com.eazybuilder.ga.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dataMigration")
@CrossOrigin
public class DataMigrationController {

    private static Logger logger = LoggerFactory.getLogger(DataMigrationController.class);

}
