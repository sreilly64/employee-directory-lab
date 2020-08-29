package io.zipcoder.persistenceapp.services;

import io.zipcoder.persistenceapp.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    private DepartmentRepository repository;

    @Autowired
    public DepartmentService(DepartmentRepository repository){
        this.repository = repository;
    }
}
