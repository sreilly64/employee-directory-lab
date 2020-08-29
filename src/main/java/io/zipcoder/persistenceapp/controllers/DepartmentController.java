package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.services.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
    private DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService){
        this.departmentService = departmentService;
    }
}
