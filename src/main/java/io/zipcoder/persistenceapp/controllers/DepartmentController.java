package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.models.Department;
import io.zipcoder.persistenceapp.models.Employee;
import io.zipcoder.persistenceapp.services.DepartmentService;
import io.zipcoder.persistenceapp.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
    private DepartmentService departmentService;
    @Autowired
    private EmployeeService employeeService;

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

    @GetMapping(value = "/departments/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id){
        Department department = null;
        try{
            department = departmentService.getDepartmentById(id);
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<Department>(department, HttpStatus.OK);
    }

    @GetMapping(value = "/departments/{departmentId}/employees")
    public @ResponseBody List<Employee> getEmployeesByDepartment(@PathVariable Long departmentId){
        List<Employee> list = null;
        try{
            list = employeeService.getEmployeesByDepartment(departmentService.getDepartmentById(departmentId));
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
        }
        return list;
    }
    //test this.. head manager survies? think this was fixed with getEbyD fix
    @DeleteMapping(value = "/departments/{departmentId}/employees")
    public ResponseEntity<Boolean> deleteEmployeesByDepartment(@PathVariable Long departmentId){
        return new ResponseEntity<Boolean>(employeeService.deleteEmployeesByDepartment(departmentService.getDepartmentById(departmentId)), HttpStatus.NO_CONTENT);
    }
    //test this... almost works, reassigned employees chain of managers breaks. maybe fixed?
    @PutMapping(value = "/departments/{id1}/merge/{id2}")
    public ResponseEntity<Boolean> mergeDepartments(@PathVariable Long id1, @PathVariable Long id2){
        Boolean response = employeeService.mergeDepartments(departmentService.getDepartmentById(id1), departmentService.getDepartmentById(id2));
        return new ResponseEntity<Boolean>(response, HttpStatus.OK);
    }

}
