package com.scb.cos.hk.ms.userdata.departmentmanagerdefinition;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.cos.hk.ms.security.CORSFilter;

public class DepartmentControllerUnitTest {

    private MockMvc mockMvc;
    @Mock
    private DepartmentRepo departmentRepo;
    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    DepartmentController departmentController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(departmentController).setCustomArgumentResolvers(new SortHandlerMethodArgumentResolver()).addFilters(new CORSFilter()).build();
    }

    private Department getPreLoadedDepartment() {
        Department department = new Department();
        department.setCode("OPS System Admin Team");
        department.setDepartmentDesc("OPS System Admin Team");
        department.setStatus('Y');
        return department;
    }

    @Test
    public void givenListOfDepartment_whenGetListOfDepartment_thenReturnListOfDepartmentAsJsonArray() throws Exception {

        Department department_dt_001 = new Department();
        department_dt_001.setCode("OPS System Admin Team");
        department_dt_001.setDepartmentDesc("OPS System Admin Team");
        department_dt_001.setStatus('Y');

        Department department_dt_002 = new Department();
        department_dt_002.setCode("OPS System Admin Team");
        department_dt_002.setDepartmentDesc("OPS System Admin Team");
        department_dt_002.setStatus('Y');

        List<Department> departmentList = new ArrayList<>();
        departmentList.add(department_dt_002);
        departmentList.add(department_dt_001);

        when(departmentService.findAll(null,Sort.by(new Order[0]))).thenReturn(departmentList);

        this.mockMvc.perform(get("/cos/hk/v1/master-records/department").header("Origin", "*"))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].code", is("OPS System Admin Team")))
        .andExpect(jsonPath("$[0].departmentDesc", is("OPS System Admin Team")))
        .andExpect(jsonPath("$[0].status", is("Y")))
        .andExpect(jsonPath("$[1].code", is("OPS System Admin Team")))
        .andExpect(jsonPath("$[1].departmentDesc", is("OPS System Admin Team")))
        .andExpect(jsonPath("$[1].status", is("Y")));
    }

    @Test
    public void givenListOfDepartment_whenGetDocument_thenReturnDocumentAsJsonArray() throws Exception {

        Department dt_a001 = getPreLoadedDepartment();
        when(departmentService.findOne("OPS System Admin Team")).thenReturn(dt_a001);
        this.mockMvc.perform(get("/cos/hk/v1/master-records/department/OPS System Admin Team").header("Origin", "*"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.code", is("OPS System Admin Team")))
        .andExpect(jsonPath("$.departmentDesc", is("OPS System Admin Team")))
        .andExpect(jsonPath("$.status", is("Y")));
    }
    
    @Test
    public void givenDepartment_whenSaveWithDepartmentCodeAsNull_thenThrowException() throws Exception {
        Department dt_a001 = getPreLoadedDepartment();
        dt_a001.setCode(null);

        ObjectMapper mapper = new ObjectMapper();        
        this.mockMvc.perform(post("/cos/hk/v1/master-records/department")
                .content(mapper.writeValueAsString(dt_a001))
                .header("Origin", "*")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isBadRequest())
        .andReturn();
    }

    @Test
    public void givenDepartment_whenUpdateWithDepartmentCodeAsNull_thenThrowException() throws Exception {
        Department dt_a001 = getPreLoadedDepartment();
        dt_a001.setCode(null);

        ObjectMapper mapper = new ObjectMapper();        
        this.mockMvc.perform(put("/cos/hk/v1/master-records/department")
                .content(mapper.writeValueAsString(dt_a001))
                .header("Origin", "*")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isBadRequest())
        .andReturn();
    }

}
