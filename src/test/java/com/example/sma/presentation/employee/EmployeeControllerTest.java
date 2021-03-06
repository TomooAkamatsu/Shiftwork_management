package com.example.sma.presentation.employee;

import com.example.sma.application.employee.EmployeeApplicationService;
import com.example.sma.domain.models.employee.Employee;
import com.example.sma.domain.models.employee.Security;
import com.example.sma.domain.models.employee.WorkingForm;
import com.example.sma.exception.EmptyValueException;
import com.example.sma.exception.InvalidNumberException;
import com.example.sma.exception.NotFoundEmployeeException;
import com.example.sma.presentation.exceptionHandler.GlobalExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    //    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeApplicationService employeeApplicationService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    Map<String, String> convertToJson(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(data, new TypeReference<>() {
        });
    }

    @Test
    public void ?????????????????????????????????????????????200????????????????????????() throws Exception {
        when(employeeApplicationService.findAllWorkingForm()).thenReturn(Arrays.asList(
                new WorkingForm(1, "?????????"),
                new WorkingForm(2, "?????????(??????)"),
                new WorkingForm(3, "?????????")
        ));

        mockMvc.perform(get("/api/employees/working-form")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"workingFormId\":1,\"workingFormName\":\"?????????\"},{\"workingFormId\":2,\"workingFormName\":\"?????????(??????)\"},{\"workingFormId\":3,\"workingFormName\":\"?????????\"}]"));
    }

    @Test
    void ????????????????????????????????????????????????200????????????????????????() throws Exception {
        when(employeeApplicationService.findActiveEmployee()).thenReturn(Arrays.asList(
                new Employee(1, "??????", "??????", "Kishida", "Fumio", "1957-07-29", 64, "???", "090-1111-1111", "kishida@hoge.com", "2020-01-01", null, new WorkingForm(1, "?????????")),
                new Employee(2, "???", "??????", "Suga", "Yoshihide", "1948-12-06", 73, "???", "090-2222-2222", "suga@hoge.com", "2020-02-01", null, new WorkingForm(2, "?????????(??????)")),
                new Employee(3, "??????", "??????", "Abe", "Shinzo", "1954-09-21", 67, "???", "090-3333-3333", "abe@hoge.com", "2020-03-01", null, new WorkingForm(1, "?????????"))
        ));

        mockMvc.perform(get("/api/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"employeeId\":1,\"lastName\":\"??????\",\"firstName\":\"??????\",\"romanLastName\":\"Kishida\",\"romanFirstName\":\"Fumio\",\"birthday\":\"1957-07-29\",\"age\":64,\"gender\":\"???\",\"phoneNumber\":\"090-1111-1111\",\"email\":\"kishida@hoge.com\",\"employmentDate\":\"2020-01-01\",\"workingFormName\":\"?????????\"}," +
                        "{\"employeeId\":3,\"lastName\":\"??????\",\"firstName\":\"??????\",\"romanLastName\":\"Abe\",\"romanFirstName\":\"Shinzo\",\"birthday\":\"1954-09-21\",\"age\":67,\"gender\":\"???\",\"phoneNumber\":\"090-3333-3333\",\"email\":\"abe@hoge.com\",\"employmentDate\":\"2020-03-01\",\"workingFormName\":\"?????????\"}," +
                        "{\"employeeId\":2,\"lastName\":\"???\",\"firstName\":\"??????\",\"romanLastName\":\"Suga\",\"romanFirstName\":\"Yoshihide\",\"birthday\":\"1948-12-06\",\"age\":73,\"gender\":\"???\",\"phoneNumber\":\"090-2222-2222\",\"email\":\"suga@hoge.com\",\"employmentDate\":\"2020-02-01\",\"workingFormName\":\"?????????(??????)\"}]"));
    }

    @Test
    void ??????????????????????????????????????????201???????????????????????????????????????() throws Exception {
        EmployeeForm employeeForm = new EmployeeForm(new Employee(1, "??????", "??????", "Kishida", "Fumio", "1957-07-29", 64, "???", "090-1111-1111", "kishida@hoge.com", "2020-01-01", null, new WorkingForm(1, "?????????")));
        when(employeeApplicationService.insertEmployee(employeeForm.convertToEntity()))
                .thenReturn(new EmployeeOperationResult(true, 1));

        mockMvc.perform(post("/api/employees")
                        .content("{\"lastName\":\"??????\",\"firstName\":\"??????\",\"romanLastName\":\"Kishida\",\"romanFirstName\":\"Fumio\",\"birthday\":\"1957-07-29\",\"age\":64,\"gender\":\"???\",\"phoneNumber\":\"090-1111-1111\",\"email\":\"kishida@hoge.com\",\"employmentDate\":\"2020-01-01\",\"workingFormName\":\"?????????\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(header().string("Location","http://localhost:8080/api/employees/1"))
                .andExpect(content().json("{\"completed\":true,\"targetEmployeeId\":1}"));
    }

    @Test
    void ??????????????????????????????????????????????????????????????????400???MethodArgumentNotValidException???????????????() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .content("{\"employeeId\":1,\"lastName\":\"\",\"firstName\":\"\",\"romanLastName\":\"Kishida\",\"romanFirstName\":\"Fumio\",\"birthday\":\"1957-07-29\",\"age\":64,\"gender\":\"???\",\"phoneNumber\":\"090-1111-1111\",\"email\":\"kishida@hoge.com\",\"employmentDate\":\"2020-01-01\",\"workingFormName\":\"?????????\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(content().json("{\"exception\":\"org.springframework.web.bind.MethodArgumentNotValidException\",\"message\":\"???????????????????????????\",\"status\":\"BAD_REQUEST\"}"));
    }

    @Test
    void ??????????????????????????????????????????200???????????????????????????????????????() throws Exception {
        String patchData = "{\"lastName\":\"??????\",\"age\":\"60\"}";

        when(employeeApplicationService.updateEmployee(convertToJson(patchData), 1)).thenReturn(new EmployeeOperationResult(true,1));

        mockMvc.perform(patch("/api/employees/1")
                        .content(patchData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"completed\":true,\"targetEmployeeId\":1}"));
    }

    @Test
    void InvalidNumberException??????????????????400???????????????????????????????????????() throws Exception {
        String patchData = "{\"lastName\":\"??????\",\"age\":\"126\"}";

        when(employeeApplicationService.updateEmployee(convertToJson(patchData), 1)).thenThrow(new InvalidNumberException("hoge"));

        mockMvc.perform(patch("/api/employees/1")
                        .content(patchData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(content().string("{\"exception\":\"com.example.sma.exception.InvalidNumberException\",\"message\":\"hoge\",\"status\":\"BAD_REQUEST\"}"));
    }

    @Test
    void NotFoundEmployeeException??????????????????404???????????????????????????????????????() throws Exception {
        String patchData = "{\"lastName\":\"??????\",\"age\":\"26\"}";

        when(employeeApplicationService.updateEmployee(convertToJson(patchData), 10)).thenThrow(new NotFoundEmployeeException("hoge"));

        mockMvc.perform(patch("/api/employees/10")
                        .content(patchData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string("{\"exception\":\"com.example.sma.exception.NotFoundEmployeeException\",\"message\":\"hoge\",\"status\":\"NOT_FOUND\"}"));
    }

    @Test
    void EmptyValueException??????????????????400???????????????????????????????????????() throws Exception {
        String patchData = "{\"lastName\":\"\",\"\":\"26\"}";

        when(employeeApplicationService.updateEmployee(convertToJson(patchData), 1)).thenThrow(new EmptyValueException("hoge"));

        mockMvc.perform(patch("/api/employees/1")
                        .content(patchData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(content().string("{\"exception\":\"com.example.sma.exception.EmptyValueException\",\"message\":\"hoge\",\"status\":\"BAD_REQUEST\"}"));
    }

    @Test
    void ????????????????????????????????????200???????????????????????????????????????() throws Exception {
        when(employeeApplicationService.patchRetirement(1)).thenReturn(new EmployeeOperationResult(true, 1));

        mockMvc.perform(delete("/api/employees/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"completed\":true,\"targetEmployeeId\":1}"));
    }

    @Test
    void ?????????ID????????????????????????????????????????????????????????????????????????200????????????????????????() throws Exception {
        when(employeeApplicationService.getLoginInfo(1))
                .thenReturn(new Security(1,"password","admin"));

        mockMvc.perform(get("/api/employees/login/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"employeeId\":1,\"password\":\"password\",\"authority\":\"admin\"}"));
    }

}
