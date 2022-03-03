package gp.developer.api.task_2;

import gp.developer.api.task_2.entity.DeveloperEntity;
import gp.developer.api.task_2.exception.DeveloperNotFoundException;
import gp.developer.api.task_2.repository.DeveloperRepository;
import gp.developer.api.task_2.service.DeveloperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class DeveloperServiceTest {

    @MockBean
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperService developerService;

    @Test
    public void addDeveloperOK () throws DeveloperNotFoundException {

        String email = "email_1";
        DeveloperEntity developerEntity = new DeveloperEntity("user_1",email);

        when(developerRepository.save(any(DeveloperEntity.class))).thenReturn(developerEntity);

        DeveloperEntity developerEntitySave = developerService.addDeveloper(developerEntity);

        assertEquals(developerEntitySave, developerEntity);
    }
    @Test
    public void getDeveloperOK () throws DeveloperNotFoundException {

        String name = "user_1";
        String email = "email_1";
        DeveloperEntity developerEntity = new DeveloperEntity(name, email);

        when(developerRepository.findByNameAndEmail(any(String.class), any(String.class))).thenReturn(developerEntity);
        DeveloperEntity developerEntityGet = developerService.getDeveloper(developerEntity);

        assertEquals(developerEntityGet, developerEntity);
    }
    @Test
    public void deleteDeveloperOK () throws DeveloperNotFoundException {

        DeveloperEntity developerEntity = new DeveloperEntity("user_1", "email_1");

        when(developerRepository.findByNameAndEmail(any(String.class), any(String.class))).thenReturn(developerEntity);
//        when(developerService.checkDeveloperInBD(any(DeveloperEntity.class))).thenReturn(developerEntity); // need to figure out why it doesn't work

        String result = developerService.deleteDeveloper(developerEntity);
        verify(developerRepository).delete(developerEntity);
        assertEquals(result, "OK Developer has been successfully deleted");
    }

    @Test
    public void updateDeveloperOK () throws DeveloperNotFoundException {

        DeveloperEntity developerEntityOld = new DeveloperEntity("user_1", "email_1");
        DeveloperEntity developerEntityNew = new DeveloperEntity("user_2", "email_2");
        List<DeveloperEntity> developerEntityList = new ArrayList<>();
        developerEntityList.add(developerEntityOld);
        developerEntityList.add(developerEntityNew);

        when(developerRepository.findByNameAndEmail(any(String.class), any(String.class))).thenReturn(developerEntityList.get(0));
//        when(developerRepository.save(any(DeveloperEntity.class))).thenReturn(developerEntityNew);

        developerService.updateDeveloper(developerEntityList);

        verify(developerRepository).save(developerEntityList.get(1));
    }

    @Test
    public void checkMailUniquenessTest () throws DeveloperNotFoundException {
        String email = "email_1";

        when(developerRepository.findByEmail(any(String.class))).thenReturn(null);
        boolean result = developerService.checkMailUniqueness(email);

        assertTrue(result);
    }

    @Test
    public void checkNameValidateTest () throws DeveloperNotFoundException {

        assertTrue(developerService.checkNameValidate("user_1"));
        assertThrows(DeveloperNotFoundException.class, () ->
                developerService.checkNameValidate("1_user_1"));
    }
    @Test
    public void checkNameLengthValidateTest () throws DeveloperNotFoundException {
        assertThrows(DeveloperNotFoundException.class, () ->
                developerService.checkNameLengthValidate("u"));

        assertTrue(developerService.checkNameLengthValidate("user_1"));

        assertThrows(DeveloperNotFoundException.class, () ->
                developerService.checkNameLengthValidate("u123456789_123456789_123456789_123456789_0123456789"));
    }
    @Test
    public void checkDeveloperInBDTest () throws DeveloperNotFoundException {
        String name = "user_1";
        String email = "email_1";
        DeveloperEntity developerEntity = new DeveloperEntity(name, email);

        when(developerRepository.findByNameAndEmail(any(String.class),any(String.class))).thenReturn(developerEntity);
        DeveloperEntity developerEntityDB = developerService.checkDeveloperInBD(developerEntity);

        assertEquals(developerEntityDB, developerEntity);

        when(developerRepository.findByNameAndEmail(any(String.class),any(String.class))).thenReturn(null);

        assertThrows(DeveloperNotFoundException.class, () ->
                developerService.checkDeveloperInBD(developerEntity));
    }
    @Test
    public void requestsUpdateValidateTest () throws DeveloperNotFoundException {
        DeveloperEntity developerEntity_1 = new DeveloperEntity("user_1", "email_1");
        DeveloperEntity developerEntity_2 = new DeveloperEntity("user_2", "email_2");
        DeveloperEntity developerEntity_3 = new DeveloperEntity("user_2", "email_2");

        List<DeveloperEntity> developerEntityList_1 = new ArrayList<>();
        developerEntityList_1.add(developerEntity_1);
        developerEntityList_1.add(developerEntity_2);

        List<DeveloperEntity> developerEntityList_2 = new ArrayList<>();
        developerEntityList_2.add(developerEntity_1);
        developerEntityList_2.add(developerEntity_2);
        developerEntityList_2.add(developerEntity_3);

        List<DeveloperEntity> developerEntityList_3 = new ArrayList<>();
        developerEntityList_3.add(developerEntity_2);
        developerEntityList_3.add(developerEntity_3);

        assertTrue(developerService.requestsUpdateValidate(developerEntityList_1));

        assertThrows(DeveloperNotFoundException.class, () ->
                developerService.requestsUpdateValidate(developerEntityList_2));

        assertThrows(DeveloperNotFoundException.class, () ->
                developerService.requestsUpdateValidate(developerEntityList_3));
    }
}
