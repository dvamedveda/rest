package ru.job4j.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.EmployeeService;
import ru.job4j.auth.service.PersonService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private PersonService personService;

    @MockBean
    private EmployeeService employeeService;

    private MockRestServiceServer server;

    @BeforeEach
    public void setUp() {
        RestGatewaySupport gateway = new RestGatewaySupport();
        gateway.setRestTemplate(this.restTemplate);
        this.server = MockRestServiceServer.createServer(gateway);
    }

    @Test
    public void whenGetEmployeesThenSuccess() throws Exception {
        List<Employee> mockEmployees = new ArrayList<>();
        Employee mockEmployee = Employee.of(1, "some", "for test", 1L, new Date(1L));
        mockEmployees.add(mockEmployee);
        Mockito.when(this.employeeService.findAll()).thenReturn(mockEmployees);
        List<Person> mockPersons = new ArrayList<>();
        Person mockPersonOne = Person.of(1, "first", "pass", 1);
        Person mockPersonTwo = Person.of(2, "second", "pass", 1);
        mockPersons.add(mockPersonOne);
        mockPersons.add(mockPersonTwo);
        Mockito.when(this.personService.findAllByEmployeeId(1)).thenReturn(mockPersons);
        this.server
                .expect(once(), requestTo("http://localhost:8080/person/employee/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("["
                                + "            {"
                                + "                \"id\": 1,"
                                + "                \"login\": \"first\","
                                + "                \"password\": \"pass\","
                                + "                \"employeeId\": 1"
                                + "            },"
                                + "            {"
                                + "                \"id\": 2,"
                                + "                \"login\": \"second\","
                                + "                \"password\": \"pass\","
                                + "                \"employeeId\": 1"
                                + "            }"
                                + "        ]",
                        MediaType.APPLICATION_JSON));
        this.mvc.perform(get("/employee/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("some")))
                .andExpect(jsonPath("$[0].lastName", is("for test")))
                .andExpect(jsonPath("$[0].inn", is(1)))
                .andExpect(jsonPath("$[0].hired", is("1970-01-01T00:00:00.001+00:00")));
        this.server.verify();
    }

    @Test
    public void whenCreatePersonForEmployeeThenCorrect() throws Exception {
        this.server
                .expect(once(), requestTo("http://localhost:8080/person"))
                .andExpect(MockRestRequestMatchers.content().json("{"
                        + "                \"id\": 0,"
                        + "                \"login\": \"rest\","
                        + "                \"password\": \"created\","
                        + "                \"employeeId\": 1"
                        + "            }"
                ))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{"
                                + "                \"id\": 1,"
                                + "                \"login\": \"rest\","
                                + "                \"password\": \"created\","
                                + "                \"employeeId\": 1"
                                + "            }",
                        MediaType.APPLICATION_JSON));
        this.mvc.perform(
                post("/employee/").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "                \"id\": 0,"
                                + "                \"login\": \"rest\","
                                + "                \"password\": \"created\","
                                + "                \"employeeId\": 1"
                                + "            }"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.login", is("rest")))
                .andExpect(jsonPath("$.password", is("created")))
                .andExpect(jsonPath("$.employeeId", is(1)));
        this.server.verify();
    }

    @Test
    public void whenUpdatePersonForEmployeeThenCorrect() throws Exception {
        this.server
                .expect(once(), requestTo("http://localhost:8080/person"))
                .andExpect(MockRestRequestMatchers.content().json("{"
                        + "                \"id\": 1,"
                        + "                \"login\": \"rest\","
                        + "                \"password\": \"updated\","
                        + "                \"employeeId\": 1"
                        + "            }"
                ))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK));
        this.mvc.perform(
                put("/employee/").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "                \"id\": 1,"
                                + "                \"login\": \"rest\","
                                + "                \"password\": \"updated\","
                                + "                \"employeeId\": 1"
                                + "            }"))
                .andExpect(status().isOk());
        this.server.verify();
    }

    @Test
    public void whenDeletePersonForEmployeeThenCorrect() throws Exception {
        this.server
                .expect(once(), requestTo("http://localhost:8080/person/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));
        this.mvc.perform(
                delete("/employee/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "                \"id\": 1,"
                                + "                \"login\": \"rest\","
                                + "                \"password\": \"deleted\","
                                + "                \"employeeId\": 1"
                                + "            }"))
                .andExpect(status().isOk());
        this.server.verify();
    }
}
