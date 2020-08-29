package io.zipcoder.persistenceapp.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String title;
    private String phoneNumber;
    private String email;
    private LocalDate hireDate;
    //@ManyToOne
    private Employee manager;
    private Long departmentId;
    /*
    @OneToMany(targetEntity = Employee.class, mappedBy = "employee")
    private Set<Employee> managedEmployees;*/

    public Employee(){
        this(null,null,null,null,null,null,null,null,null);
    }

    public Employee(String firstName, String lastName, String title, String phoneNumber, String email, LocalDate hireDate, Employee manager, Long departmentId) {
        this(null,firstName,lastName,title,phoneNumber,email,hireDate,manager,departmentId);
    }

    public Employee(Long employeeId, String firstName, String lastName, String title, String phoneNumber, String email, LocalDate hireDate, Employee manager, Long departmentId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.hireDate = hireDate;
        this.manager = manager;
        this.departmentId = departmentId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
