package io.zipcoder.persistenceapp.models;

import javax.persistence.*;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "department_id")
    private Long departmentId;
    @Column(name = "name")
    private String name;
    @Column(name = "manager_employee_id")
    private Long managerId;

    public Department(){
        this(null,null,null);
    }

    public Department(String name, Long managerId) {
        this(null,name, managerId);
    }

    public Department(Long departmentId, String name, Long managerId) {
        this.departmentId = departmentId;
        this.name = name;
        this.managerId = managerId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
