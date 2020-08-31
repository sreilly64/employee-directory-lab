package io.zipcoder.persistenceapp.services;

import io.zipcoder.persistenceapp.models.Department;
import io.zipcoder.persistenceapp.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    private DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository){
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(Department d) {
        return departmentRepository.save(d);
    }

    public Department updateDepartment(Long id, Department d) {
        Department department = departmentRepository.findOne(id);
        department.setName(d.getName());
        boolean newManager = department.getManagerId() != d.getDepartmentId();
        department.setManagerId(d.getManagerId());
        return departmentRepository.save(department);
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findOne(id);
    }
}
