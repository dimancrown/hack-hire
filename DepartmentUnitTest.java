package com.scb.cos.hk.ms.userdata.departmentmanagerdefinition;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.context.MessageSource;

import com.scb.cos.hk.ms.commons.utils.ConfigUtils;
import com.scb.cos.hk.ms.commons.utils.StringUtils;

public class DepartmentUnitTest {
    private Validator validator;

    @Mock
    MessageSource messageSource;

    @Before
    public void setUp() {
        messageSource = ConfigUtils.messageSource();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Department getPreLoadedDocumentType() {
        Department department = new Department();
        department.setCode("A0001");
        department.setDepartmentDesc("Department Desc");
        department.setStatus('Y');
        return department;
    }

    @Test
    public void givenDepartment_whenMandatoryFieldsAreNull_throwConstraintViolation() {
        Department department = getPreLoadedDocumentType();
        department.setCode(null);
        department.setDepartmentDesc(null);
        Set<String> errors = new HashSet<>();
        Set<ConstraintViolation<Department>> violations = validator.validate(department);
        violations.forEach(violation-> {
            String key = StringUtils.extractKeyFromProp(violation.getMessage());
            errors.add(messageSource.getMessage(key, null, Locale.getDefault()));
        });

        assertThat(errors, hasItems("Department Code is required.","Description is required."));
    }

    @Test
    public void givenReasonCode_whenInvalidProperties_throwConstraintViolation() {
        Department reasonCode = getPreLoadedDocumentType();
        reasonCode.setCode("A012345678912345678901234567890");
        reasonCode.setDepartmentDesc("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        reasonCode.setStatus('C');
        Set<String> errors = new HashSet<>();
        Set<ConstraintViolation<Department>> violations = validator.validate(reasonCode);
        violations.forEach(violation-> {
            String key = StringUtils.extractKeyFromProp(violation.getMessage());
            System.out.println(messageSource.getMessage(key, null, Locale.getDefault()));
            errors.add(messageSource.getMessage(key, null, Locale.getDefault()));
        });

        assertThat(errors, hasItems("Department Code cannot exceed 30 characters.", 
                "Description cannot exceed 50 characters.", 
                "Record Status, accepted value is either 'Y' or 'N'."));
    }

}
