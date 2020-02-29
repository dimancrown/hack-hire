package com.scb.cos.hk.ms.userdata.departmentmanagerdefinition;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.scb.cos.hk.ms.commons.constants.MasterConstants;
import com.scb.cos.hk.ms.commons.exception.CustomException;

public class DepartmentServiceUnitTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private DepartmentRepo departmentRepo;

    @InjectMocks
    private DepartmentService departmentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);        
    }

    private DepartmentEntity getPreLoadedDepartmentEntity(){
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setCode("OPS System Admin Team");
        departmentEntity.setDepartmentDesc("OPS System Admin Team");
        departmentEntity.setManagerId("1169759");
        departmentEntity.setStatus('Y');
        return departmentEntity;
    }

    private Department getPreLoadedDepartment(){
        Department department = new Department();
        department.setCode("OPS System Admin Team");
        department.setDepartmentDesc("OPS System Admin Team");
        department.setManagerId("1169759");
        department.setStatus('Y');
        return department;
    }

    @Test
    public void given_whenfindAll_thenReturnAllDepartmentEntity(){
        DepartmentEntity department_001 = new DepartmentEntity();
        department_001.setCode("OPS System Admin Team");
        department_001.setDepartmentDesc("OPS System Admin Team");
        department_001.setManagerId("1169759");
        department_001.setStatus('Y');

        DepartmentEntity department_002 = new DepartmentEntity();
        department_002.setCode("OPS System Admin Team2");
        department_002.setDepartmentDesc("OPS System Admin Team");
        department_002.setManagerId("1169759");
        department_002.setStatus('Y');

        List<DepartmentEntity> documentTypeEntityList = new ArrayList<>();
        documentTypeEntityList.add(department_001);
        documentTypeEntityList.add(department_002);

        when(departmentRepo.findAll()).thenReturn(documentTypeEntityList);

        List<Department> documentTypeList = departmentService.findAll(null,null);

        assertThat(documentTypeList, hasSize(2));
        assertThat(documentTypeList.get(0).getCode(), is("OPS System Admin Team"));
        assertThat(documentTypeList.get(0).getDepartmentDesc(), is("OPS System Admin Team"));
        assertThat(documentTypeList.get(0).getManagerId(), is("1169759"));
        assertThat(documentTypeList.get(0).getStatus(), is('Y'));
        assertThat(documentTypeList.get(1).getCode(), is("OPS System Admin Team2"));
        assertThat(documentTypeList.get(1).getDepartmentDesc(), is("OPS System Admin Team"));
        assertThat(documentTypeList.get(1).getManagerId(), is("1169759"));
        assertThat(documentTypeList.get(1).getStatus(), is('Y'));

    }

    @Test
    public void givenDepartmentCode_whenfindOneWithValidCode_thenReturnDepartmentEntity(){
        when(departmentRepo.findById("OPS System Admin Team")).thenReturn(Optional.of(getPreLoadedDepartmentEntity()));
        Department department = departmentService.findOne("OPS System Admin Team");
        assertThat(department.getCode(), is("OPS System Admin Team"));
        assertThat(department.getDepartmentDesc(), is("OPS System Admin Team"));
        assertThat(department.getManagerId(), is("1169759"));
        assertThat(department.getStatus(), is('Y'));
    }

    @Test
    public void givenDepartmentCode_whenfindOneWithInValidCode_thenThrowException(){
        expectedException.expect(CustomException.class);
        expectedException.expectMessage(MasterConstants.RESOURCE_STATUS_NOT_FOUND);
        when(departmentRepo.findById("OPS System Admin Team")).thenReturn(Optional.of(getPreLoadedDepartmentEntity()));
        departmentService.findOne("OPS System Admin Team1");
    }

    @Test
    public void givenNewDepartment_whenSave_thenReturnDepartmentEntity(){
        DepartmentEntity departmentEntity = getPreLoadedDepartmentEntity();

        when(departmentRepo.findById("OPS System Admin Team")).thenReturn(Optional.empty());
        when(departmentRepo.save(any())).thenReturn(departmentEntity);

        Department department = departmentService.save(getPreLoadedDepartment());
        assertThat(department.getCode(), is("OPS System Admin Team"));
        assertThat(department.getDepartmentDesc(), is("OPS System Admin Team"));
        assertThat(department.getManagerId(), is("1169759"));
        assertThat(department.getStatus(), is('Y'));
    }

    @Test
    public void givenExistingDepartment_whenSave_thenThrowException(){
        expectedException.expect(CustomException.class);
        expectedException.expectMessage(MasterConstants.RESOURCE_STATUS_NOT_ACCEPTED);
        DepartmentEntity documentTypeEntity = getPreLoadedDepartmentEntity();
        when(departmentRepo.findById("OPS System Admin Team")).thenReturn(Optional.of(getPreLoadedDepartmentEntity()));
        when(departmentRepo.save(any())).thenReturn(documentTypeEntity);
        departmentService.save(getPreLoadedDepartment());
    }

    @Test
    public void givenExistingDepartment_whenUpdate_thenReturnDepartmentEntity(){
        DepartmentEntity documentTypeEntity = getPreLoadedDepartmentEntity();
        when(departmentRepo.findById("OPS System Admin Team")).thenReturn(Optional.of(getPreLoadedDepartmentEntity()));
        when(departmentRepo.save(any())).thenReturn(documentTypeEntity);
        Department department = departmentService.update(getPreLoadedDepartment());
        assertThat(department.getCode(), is("OPS System Admin Team"));
        assertThat(department.getDepartmentDesc(), is("OPS System Admin Team"));
        assertThat(department.getManagerId(), is("1169759"));
        assertThat(department.getStatus(), is('Y'));
    }

    @Test
    public void givenNewDepartment_whenUpdate_thenThrowException(){
        expectedException.expect(CustomException.class);
        expectedException.expectMessage(MasterConstants.RESOURCE_STATUS_NOT_FOUND);
        DepartmentEntity documentTypeEntity = getPreLoadedDepartmentEntity();
        when(departmentRepo.findById("A002")).thenReturn(Optional.empty());
        when(departmentRepo.save(any())).thenReturn(documentTypeEntity);
        departmentService.update(getPreLoadedDepartment());
    }



}
