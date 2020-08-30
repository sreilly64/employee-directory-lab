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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}