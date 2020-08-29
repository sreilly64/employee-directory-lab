package io.zipcoder.persistenceapp.models;

import javax.persistence.*;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long departmentId;
    private String name;
    @OneToOne
    private Employee manager;

    public Department(){
        this(null,null,null);
    }

    public Department(String name, Employee manager) {
        this(null,name,manager);
    }

    public Department(Long departmentId, String name, Employee manager) {
        this.departmentId = departmentId;
        this.name = name;
        this.manager = manager;
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

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
}
