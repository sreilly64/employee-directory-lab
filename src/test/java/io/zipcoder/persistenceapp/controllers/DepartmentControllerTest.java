package io.zipcoder.persistenceapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zipcoder.persistenceapp.models.Department;
import io.zipcoder.persistenceapp.models.Employee;
import io.zipcoder.persistenceapp.services.DepartmentService;
import io.zipcoder.persistenceapp.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private DepartmentController departmentController;


//
//    @InjectMocks
//    private EmployeeController employeeController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
    }

    @Test
    public void createDepartment() throws Exception {
        Department department = new Department(100L, "Finance", 1L);

        when(departmentService.createDepartment(department)).thenReturn(department);
        mockMvc.perform(
                post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(department)))
                .andExpect(status().isCreated());

        verify(departmentService, times(1)).createDepartment(department);
        verifyNoMoreInteractions(departmentService);

    }

    @Test
    public void updateDepartment() throws Exception {
        Department department = new Department(100L, "Finance", 1L);

        when(departmentService.updateDepartment(100L, department)).thenReturn(department);

        mockMvc.perform(
                put("/api/departments/{id}", department.getDepartmentId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(department)))
                .andExpect(status().isOk());

        verify(departmentService, times(1)).updateDepartment(100L, department);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    public void getDepartmentById() throws Exception {
        Department department = new Department(100L, "Finance", 1L);

        when(departmentService.getDepartmentById(100L)).thenReturn(department);

        mockMvc.perform(get("/api/departments/{id}", 100L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.departmentId", is(100)))
                .andExpect(jsonPath("$.name", is("Finance")))
                .andExpect(jsonPath("$.managerId", is(1)));

        verify(departmentService, times(1)).getDepartmentById(100L);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    public void getEmployeesByDepartment() throws Exception {
        /*
        Department department = new Department(100L, "Finance", 2L);
        Employee employee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 2L, 100L);
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);

        departmentService.createDepartment(department);

        when(employeeService.getEmployeesByDepartment(department)).thenReturn(employeeList);

        mockMvc.perform(get("/api/departments/{departmentId}/employees", 100L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].employeeId", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Harley")))
                .andExpect(jsonPath("$[0].lastName", is("Ozwald")))
                .andExpect(jsonPath("$[0].title", is("Employee")))
                .andExpect(jsonPath("$[0].phoneNumber", is("867-5309")))
                .andExpect(jsonPath("$[0].email", is("harley@business.com")))
                .andExpect(jsonPath("$[0].hireDate", is("1/1/2020")))
                .andExpect(jsonPath("$[0].managerId", is(2)))
                .andExpect(jsonPath("$[0].departmentId", is(100)));
        verify(employeeService, times(1)).getEmployeesByDepartment(department);
        verifyNoMoreInteractions(employeeService);
         */
    }

    @Test
    public void deleteEmployeesByDepartment() {
    }

    @Test
    public void mergeDepartments() {
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}