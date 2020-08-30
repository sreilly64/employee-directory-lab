package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.models.Department;
import io.zipcoder.persistenceapp.services.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
    private DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService){
        this.departmentService = departmentService;
    }

    @PostMapping(value = "/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department d){
        Department department = null;
        try{
            department = departmentService.createDepartment(d);
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<Department>(department, HttpStatus.CREATED);
    }

    @PutMapping(value = "/departments/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department d){
        Department department = null;
        try{
            department = departmentService.updateDepartment(id, d);
        }catch (Exception exception){
            LOGGER.info(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<Department>(department, HttpStatus.OK);
    }

}
