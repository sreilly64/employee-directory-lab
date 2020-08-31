package io.zipcoder.persistenceapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zipcoder.persistenceapp.models.Employee;
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

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void addEmployee() throws Exception {
        Employee employee = new Employee(1L, "Haryley", "Ozwald", "Silly Goober", "867-5309", "fluffy.cat@gmail.com", "3/20/2018", null, 7L);

        when(employeeService.addEmployee(employee)).thenReturn(employee);
        mockMvc.perform(
                post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(employee)))
                .andExpect(status().isCreated());

        verify(employeeService, times(1)).addEmployee(employee);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void getAllEmployees() throws Exception{
        List<Employee> employees = Arrays.asList(
                new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 2L, 100L),
                new Employee(2L,"Boss", "Guy", "Head of Acquisitions", "867-5306", "boss@business.com", "1/1/2020", null, 100L)
        );

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].employeeId", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Harley")))
                .andExpect(jsonPath("$[0].lastName", is("Ozwald")))
                .andExpect(jsonPath("$[0].title", is("Employee")))
                .andExpect(jsonPath("$[0].phoneNumber", is("867-5309")))
                .andExpect(jsonPath("$[0].email", is("harley@business.com")))
                .andExpect(jsonPath("$[0].hireDate", is("1/1/2020")))
                .andExpect(jsonPath("$[0].managerId", is(2)))
                .andExpect(jsonPath("$[0].departmentId", is(100)))
                .andExpect(jsonPath("$[1].employeeId", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Boss")))
                .andExpect(jsonPath("$[1].lastName", is("Guy")))
                .andExpect(jsonPath("$[1].title", is("Head of Acquisitions")))
                .andExpect(jsonPath("$[1].phoneNumber", is("867-5306")))
                .andExpect(jsonPath("$[1].email", is("boss@business.com")))
                .andExpect(jsonPath("$[1].hireDate", is("1/1/2020")))
                .andExpect(jsonPath("$[1].departmentId", is(100)));

        verify(employeeService, times(1)).getAllEmployees();
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void findEmployeeById() throws Exception {
        Employee employee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 2L, 100L);

        when(employeeService.findEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.employeeId", is(1)))
                .andExpect(jsonPath("$.firstName", is("Harley")))
                .andExpect(jsonPath("$.lastName", is("Ozwald")))
                .andExpect(jsonPath("$.title", is("Employee")))
                .andExpect(jsonPath("$.phoneNumber", is("867-5309")))
                .andExpect(jsonPath("$.email", is("harley@business.com")))
                .andExpect(jsonPath("$.hireDate", is("1/1/2020")))
                .andExpect(jsonPath("$.managerId", is(2)))
                .andExpect(jsonPath("$.departmentId", is(100)));

        verify(employeeService, times(1)).findEmployeeById(1L);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void findEmployeeByIdFail() throws Exception {
        when(employeeService.findEmployeeById(7L)).thenReturn(null);

        mockMvc.perform(get("/api/employees/{id}", 7L))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).findEmployeeById(7L);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void updateEmployeeManager() throws Exception {
        Employee employee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 7L, 100L);
        Employee manager = new Employee(2L,"Boss", "Guy", "Head of Acquisitions", "867-5306", "boss@business.com", "1/1/2020", null, 200L);
        Employee updatedEmployee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 2L, 200L);

        when(employeeService.updateEmployeeManager(employee.getEmployeeId(), manager.getEmployeeId())).thenReturn(updatedEmployee);

        mockMvc.perform(
                put("/api/employees/{employeeId}/manager/{managerId}", employee.getEmployeeId(), manager.getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.managerId", is(2)))
                .andExpect(jsonPath("$.departmentId", is(200)));

        //verify(employeeService, times(1)).findEmployeeById(employee.getEmployeeId());
        verify(employeeService, times(1)).updateEmployeeManager(employee.getEmployeeId(), manager.getEmployeeId());
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void updateEmployeeDetails() throws Exception {
        Employee employee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 7L, 100L);
        Employee updated = new Employee(1L,"Harley", "Ozwald", "Super Employee", "867-5309", "harley@business.com", "1/1/2020", 7L, 100L);

        when(employeeService.updateEmployeeDetails(employee.getEmployeeId(),updated)).thenReturn(updated);

        mockMvc.perform(
                put("/api/employees/{id}", employee.getEmployeeId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Super Employee")));

        verify(employeeService, times(1)).updateEmployeeDetails(employee.getEmployeeId(),updated);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void getEmployeesByManager() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 3L, 100L),
                new Employee(2L,"Boss", "Guy", "Head of Acquisitions", "867-5306", "boss@business.com", "1/1/2020", 3L, 100L)
        );

        when(employeeService.getDirectReportsByManager(3L)).thenReturn(employees);

        mockMvc.perform(get("/api/employees/3/direct_reports"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].employeeId", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Harley")))
                .andExpect(jsonPath("$[0].lastName", is("Ozwald")))
                .andExpect(jsonPath("$[0].title", is("Employee")))
                .andExpect(jsonPath("$[0].phoneNumber", is("867-5309")))
                .andExpect(jsonPath("$[0].email", is("harley@business.com")))
                .andExpect(jsonPath("$[0].hireDate", is("1/1/2020")))
                .andExpect(jsonPath("$[0].managerId", is(3)))
                .andExpect(jsonPath("$[0].departmentId", is(100)))
                .andExpect(jsonPath("$[1].employeeId", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Boss")))
                .andExpect(jsonPath("$[1].lastName", is("Guy")))
                .andExpect(jsonPath("$[1].title", is("Head of Acquisitions")))
                .andExpect(jsonPath("$[1].phoneNumber", is("867-5306")))
                .andExpect(jsonPath("$[1].email", is("boss@business.com")))
                .andExpect(jsonPath("$[1].hireDate", is("1/1/2020")))
                .andExpect(jsonPath("$[1].managerId", is(3)))
                .andExpect(jsonPath("$[1].departmentId", is(100)));

        verify(employeeService, times(1)).getDirectReportsByManager(3L);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void getReportingHierarchy() throws Exception {
        Employee employee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 2L, 100L);
        Employee manager = new Employee(2L,"Boss", "Guy", "Head of Acquisitions", "867-5306", "boss@business.com", "1/1/2020", null, 100L);
        List<Employee> higherUps = new ArrayList<>();
        higherUps.add(manager);

        when(employeeService.getReportingHierarchy(1L)).thenReturn(higherUps);

        mockMvc.perform(get("/api/employees/1/hierarchy"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employeeId", is(2)))
                .andExpect(jsonPath("$[0].firstName", is("Boss")))
                .andExpect(jsonPath("$[0].lastName", is("Guy")))
                .andExpect(jsonPath("$[0].title", is("Head of Acquisitions")))
                .andExpect(jsonPath("$[0].phoneNumber", is("867-5306")))
                .andExpect(jsonPath("$[0].email", is("boss@business.com")))
                .andExpect(jsonPath("$[0].hireDate", is("1/1/2020")))
                .andExpect(jsonPath("$[0].departmentId", is(100)));

        verify(employeeService, times(1)).getReportingHierarchy(1L);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void getEmployeesWithoutManagers() throws Exception {
        Employee employee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 2L, 100L);
        Employee manager = new Employee(2L,"Boss", "Guy", "Head of Acquisitions", "867-5306", "boss@business.com", "1/1/2020", null, 100L);
        List<Employee> empsWithoutManager = new ArrayList<>();
        empsWithoutManager.add(manager);

        when(employeeService.getEmployeesWithoutManagers()).thenReturn(empsWithoutManager);

        mockMvc.perform(get("/api/employees/no_manager"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employeeId", is(2)))
                .andExpect(jsonPath("$[0].firstName", is("Boss")))
                .andExpect(jsonPath("$[0].lastName", is("Guy")))
                .andExpect(jsonPath("$[0].title", is("Head of Acquisitions")))
                .andExpect(jsonPath("$[0].phoneNumber", is("867-5306")))
                .andExpect(jsonPath("$[0].email", is("boss@business.com")))
                .andExpect(jsonPath("$[0].hireDate", is("1/1/2020")))
                .andExpect(jsonPath("$[0].departmentId", is(100)));

        verify(employeeService, times(1)).getEmployeesWithoutManagers();
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void getReportsByManager() throws Exception {
        Employee employee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 2L, 100L);
        Employee manager = new Employee(2L,"Boss", "Guy", "Head of Acquisitions", "867-5306", "boss@business.com", "1/1/2020", 3L, 100L);
        List<Employee> allReports = new ArrayList<>();
        allReports.add(employee);
        allReports.add(manager);

        when(employeeService.getAllReportsByManager(3L)).thenReturn(allReports);

        mockMvc.perform(get("/api/employees/3/all_reports"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].employeeId", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Harley")))
                .andExpect(jsonPath("$[0].lastName", is("Ozwald")))
                .andExpect(jsonPath("$[0].title", is("Employee")))
                .andExpect(jsonPath("$[0].phoneNumber", is("867-5309")))
                .andExpect(jsonPath("$[0].email", is("harley@business.com")))
                .andExpect(jsonPath("$[0].hireDate", is("1/1/2020")))
                .andExpect(jsonPath("$[0].managerId", is(2)))
                .andExpect(jsonPath("$[0].departmentId", is(100)))
                .andExpect(jsonPath("$[1].employeeId", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Boss")))
                .andExpect(jsonPath("$[1].lastName", is("Guy")))
                .andExpect(jsonPath("$[1].title", is("Head of Acquisitions")))
                .andExpect(jsonPath("$[1].phoneNumber", is("867-5306")))
                .andExpect(jsonPath("$[1].email", is("boss@business.com")))
                .andExpect(jsonPath("$[1].hireDate", is("1/1/2020")))
                .andExpect(jsonPath("$[1].managerId", is(3)))
                .andExpect(jsonPath("$[1].departmentId", is(100)));

        verify(employeeService, times(1)).getAllReportsByManager(3L);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void deleteEmployeeById() throws Exception {
        Employee employee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 2L, 100L);

        when(employeeService.deleteEmployeeById(employee.getEmployeeId())).thenReturn(employee);

        mockMvc.perform(
                delete("/api/employees/{id}", employee.getEmployeeId()))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployeeById(employee.getEmployeeId());
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void deleteEmployeesUnderManager() throws Exception {
        Employee employee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 2L, 100L);
        Employee manager = new Employee(2L,"Boss", "Guy", "Head of Acquisitions", "867-5306", "boss@business.com", "1/1/2020", 3L, 100L);

        when(employeeService.deleteEmployeesUnderManager(3L)).thenReturn(2);

        mockMvc.perform(
                delete("/api/employees/{managerId}/all_reports", 3L))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployeesUnderManager(3L);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void deleteDirectReports() throws Exception {
        Employee employee = new Employee(1L,"Harley", "Ozwald", "Employee", "867-5309", "harley@business.com", "1/1/2020", 2L, 100L);
        Employee manager = new Employee(2L,"Boss", "Guy", "Head of Acquisitions", "867-5306", "boss@business.com", "1/1/2020", 3L, 100L);
        employeeService.addEmployee(employee);
        employeeService.addEmployee(manager);

        when(employeeService.deleteDirectReports(3L)).thenReturn(1);

        mockMvc.perform(
                delete("/api/employees/{managerId}/direct_reports", 3L))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteDirectReports(3L);
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