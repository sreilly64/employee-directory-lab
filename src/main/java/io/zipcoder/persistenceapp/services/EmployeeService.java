package io.zipcoder.persistenceapp.services;

import io.zipcoder.persistenceapp.models.Department;
import io.zipcoder.persistenceapp.models.Employee;
import io.zipcoder.persistenceapp.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public Employee addEmployee(Employee e) {
        return employeeRepository.save(e);
    }

    public Employee updateEmployeeManager(Long employeeId, Long newManageId) {
        Employee employee = employeeRepository.findOne(employeeId);
        employee.setManagerId(newManageId);
        employee.setDepartmentId(employeeRepository.findOne(newManageId).getDepartmentId());
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee findEmployeeById(Long id) {
        return employeeRepository.findOne(id);
    }

    public Employee updateEmployeeDetails(Long employeeId, Employee updatedDetails) {
        Employee employee = employeeRepository.findOne(employeeId);
        employee.setFirstName(updatedDetails.getFirstName());
        employee.setLastName(updatedDetails.getLastName());
        employee.setTitle(updatedDetails.getTitle());
        employee.setPhoneNumber(updatedDetails.getPhoneNumber());
        employee.setEmail(updatedDetails.getEmail());
        return employeeRepository.save(employee);
    }

    public List<Employee> getDirectReportsByManager(Long employeeId) {
        return employeeRepository.findAll().stream().filter(e -> e.getManagerId() == employeeId).collect(Collectors.toList());
    }

    public List<Employee> getReportingHierarchy(Long id) {
        Employee employee = employeeRepository.findOne(id);
        boolean hasManager = employee.getManagerId() != null;
        List<Employee> managerList = new LinkedList<>();

        while(hasManager){
            employee = employeeRepository.findOne(employee.getManagerId());
            managerList.add(employee);
            hasManager = employee.getManagerId() != null;
        }
        return managerList;
    }

    public List<Employee> getEmployeesWithoutManagers() {
        return employeeRepository.findAll().stream().filter(e -> e.getManagerId() == null).collect(Collectors.toList());
    }

    public List<Employee> getAllReportsByManager(Long id) {
        return employeeRepository.findAll()
                .stream()
                .filter(e -> getReportingHierarchy(e.getEmployeeId()).contains(employeeRepository.findOne(id)))
                .collect(Collectors.toList());
    }

    public Employee deleteEmployeeById(Long id) {
        Employee employee = employeeRepository.findOne(id);
        employeeRepository.delete(id);
        return employee;
    }

    public Integer deleteEmployeesUnderManager(Long managerId) {
        List<Employee> employeesToRemove = getAllReportsByManager(managerId);
        employeesToRemove.forEach(e -> deleteEmployeeById(e.getEmployeeId()));
        return employeesToRemove.size();
    }

    public Integer deleteDirectReports(Long managerId) {
        List<Employee> employeesToRemove = getDirectReportsByManager(managerId);
        employeesToRemove.forEach(e ->{
            List<Employee> subordinates = getDirectReportsByManager(e.getEmployeeId());
            subordinates.forEach(s -> updateEmployeeManager(s.getEmployeeId(), managerId));
            deleteEmployeeById(e.getEmployeeId());
        });
        return employeesToRemove.size();
    }

    public List<Employee> getEmployeesByDepartment(Department d) {
        List<Employee> response = getAllReportsByManager(d.getManagerId());
        response.add(findEmployeeById(d.getManagerId()));
        return response;
        /*return employeeRepository.findAll()
                .stream()
                .filter(e -> e.getDepartmentId() == departmentId)
                .collect(Collectors.toList());*/
    }

    public Boolean deleteEmployeesByDepartment(Department d) {
        List<Employee> employeesToDelete = getEmployeesByDepartment(d);
        employeesToDelete.forEach(e -> deleteEmployeeById(e.getEmployeeId()));
        return true;
    }

    public Boolean mergeDepartments(Department departmentOne, Department departmentTwo) {
        Long managerOfTwoId = departmentTwo.getManagerId();
        List<Employee> listOfReportsForManagerTwo = getAllReportsByManager(managerOfTwoId);
        updateEmployeeManager(managerOfTwoId, departmentOne.getManagerId());
        listOfReportsForManagerTwo.forEach(e -> {
            e.setDepartmentId(departmentOne.getDepartmentId());
            employeeRepository.save(e);
        });
        return true;
    }
}
