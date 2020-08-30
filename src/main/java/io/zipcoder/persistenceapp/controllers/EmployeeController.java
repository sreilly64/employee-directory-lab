package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.models.Employee;
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
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @PostMapping(value = "/employees")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee e){
        Employee employee = null;
        try{
            employee = employeeService.addEmployee(e);
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<Employee>(employee, HttpStatus.CREATED);
    }

    @GetMapping(value = "/employees")
    public @ResponseBody List<Employee> getAllEmployees(){
        List<Employee> list = null;
        try{
            list = employeeService.getAllEmployees();
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
        }
        return list;
    }

    @GetMapping(value = "/employees/{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable Long id){
        Employee employee = null;
        try{
            employee = employeeService.findEmployeeById(id);
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
        }

        if(employee == null){
            return ResponseEntity.notFound().build();
        }else{
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }

    }

    @PutMapping(value = "/employees/{employeeId}/manager/{managerId}")
    public ResponseEntity<Employee> updateEmployeeManager(@PathVariable("employeeId") Long employeeId, @PathVariable("managerId") Long managerId){
        Employee employee = null;
        try{
            employee = employeeService.updateEmployeeManager(employeeId, managerId);
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
    }

    @PutMapping(value = "/employees/{id}")
    public ResponseEntity<Employee> updateEmployeeDetails(@PathVariable("id") Long employeeId, @RequestBody Employee updatedDetails){
        Employee employee = null;
        try{
            employee = employeeService.updateEmployeeDetails(employeeId, updatedDetails);
        }catch (Exception exception){
            LOGGER.info(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
    }

    @GetMapping(value = "/employees/{managerId}/direct_reports")
    public @ResponseBody List<Employee> getEmployeesByManager(@PathVariable Long managerId){
        List<Employee> list = null;
        try{
            list = employeeService.getDirectReportsByManager(managerId);
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
        }
        return list;
    }

    @GetMapping(value = "/employees/{id}/hierarchy")
    public @ResponseBody List<Employee> getReportingHierarchy(@PathVariable Long id){
        List<Employee> list = null;
        try{
            list = employeeService.getReportingHierarchy(id);
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
        }
        return list;
    }

    @GetMapping(value = "/employees/no_manager")
    public @ResponseBody List<Employee> getEmployeesWithoutManagers(){
        List<Employee> list = null;
        try{
            list = employeeService.getEmployeesWithoutManagers();
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
        }
        return list;
    }

    @GetMapping(value = "/employees/{managerId}/all_reports")
    public @ResponseBody List<Employee> getReportsByManager(@PathVariable Long managerId){
        List<Employee> list = null;
        try{
            list = employeeService.getAllReportsByManager(managerId);
        }catch(Exception exception){
            LOGGER.info(exception.getMessage(), exception);
        }
        return list;
    }

    @DeleteMapping(value = "/employees/{id}")
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable Long id){
        Employee employee = null;
        try{
            employee = employeeService.deleteEmployeeById(id);
        }catch (Exception exception){
            LOGGER.info(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<Employee>(employee, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/employees/{managerId}/all_reports")
    public ResponseEntity<Boolean> deleteEmployeesUnderManager(@PathVariable Long managerId){
        return new ResponseEntity<Boolean>(employeeService.deleteEmployeesUnderManager(managerId), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/employees/{managerId}/direct_reports")
    public ResponseEntity<Boolean> deleteDirectReports(@PathVariable Long managerId){
        return new ResponseEntity<Boolean>(employeeService.deleteDirectReports(managerId), HttpStatus.NO_CONTENT);
    }


}
