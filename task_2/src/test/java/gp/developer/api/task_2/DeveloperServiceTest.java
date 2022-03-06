package gp.developer.api.task_2;

import gp.developer.api.task_2.entity.DeveloperEntity;
import gp.developer.api.task_2.exception.DeveloperMailNotUniqueException;
import gp.developer.api.task_2.exception.DeveloperNotFoundException;
import gp.developer.api.task_2.exception.DeveloperUpdateBadRequestException;
import gp.developer.api.task_2.exception.NameValidateException;
import gp.developer.api.task_2.repository.DeveloperRepository;
import gp.developer.api.task_2.service.DeveloperService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


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
    public void addDeveloperOK () throws DeveloperMailNotUniqueException, NameValidateException {

        DeveloperEntity developerEntity = new DeveloperEntity("user_1","email_1");

        when(developerRepository.save(developerEntity)).thenReturn(developerEntity);

        DeveloperEntity developerEntitySave = developerService.addDeveloper(developerEntity);

        assertEquals(developerEntitySave, developerEntity);
    }
    @Test
    public void getDeveloperOK () throws DeveloperNotFoundException {

        String name = "user_1";
        String email = "email_1";
        long id = 1L;
        DeveloperEntity developerEntity = new DeveloperEntity(name, email, id);

        when(developerRepository.findById(id)).thenReturn(developerEntity);

        DeveloperEntity developerEntityGet = developerService.getDeveloper(id);

        assertEquals(developerEntityGet, developerEntity);
    }
    @Test
    public void deleteDeveloperOK () throws DeveloperNotFoundException {

        DeveloperEntity developerEntity = new DeveloperEntity("user_1", "email_1");

        when(developerRepository.findByNameAndEmail(any(String.class), any(String.class))).thenReturn(developerEntity);

        String result = developerService.deleteDeveloper(developerEntity);
        verify(developerRepository).delete(developerEntity);
        assertEquals(result, "OK Developer has been successfully deleted");
    }

    @Test
    public void updateDeveloperOK () throws DeveloperNotFoundException, DeveloperMailNotUniqueException,
                                            NameValidateException, DeveloperUpdateBadRequestException {
        long id = 1L;
        DeveloperEntity developerEntityDB = new DeveloperEntity("user_1", "email_1", id);
        DeveloperEntity developerEntityNew = new DeveloperEntity("user_2", "email_2");

        when(developerRepository.findById(id)).thenReturn(developerEntityDB);
//        when(developerRepository.findById(id)).thenReturn(developerEntity);

        developerService.updateDeveloper(developerEntityNew, id);

        verify(developerRepository).save(developerEntityDB);
    }

    @Test
    public void checkMailUniquenessTest () throws DeveloperMailNotUniqueException {
        String email = "email_1";

        DeveloperEntity developerEntity = new DeveloperEntity("user_1", email, 1L);


        when(developerRepository.findByEmail(any(String.class))).thenReturn(null);

        boolean result = developerService.checkMailUniqueness(email);
        assertTrue(result);

        when(developerRepository.findByEmail(email)).thenReturn(developerEntity);

        assertThrows(DeveloperMailNotUniqueException.class, () ->
                developerService.checkMailUniqueness(email));

    }

    @Test
    public void checkNameValidateTest () throws NameValidateException {

        assertTrue(developerService.checkNameValidate("user_1"));
        assertThrows(NameValidateException.class, () ->
                developerService.checkNameValidate("1_user_1"));
    }
    @Test
    public void checkNameLengthValidateTest () throws NameValidateException {
        assertThrows(NameValidateException.class, () ->
                developerService.checkNameLengthValidate("u"));

        assertTrue(developerService.checkNameLengthValidate("user_1"));

        assertThrows(NameValidateException.class, () ->
                developerService.checkNameLengthValidate("u123456789_123456789_123456789_123456789_0123456789"));
    }
    @Test
    public void checkDeveloperInDBbyIdTest () throws DeveloperNotFoundException {
        String name = "user_1";
        String email = "email_1";
        long id = 1L;
        DeveloperEntity developerEntity = new DeveloperEntity(name, email, id);

        when(developerRepository.findById(id)).thenReturn(developerEntity);
        DeveloperEntity developerEntityDB = developerService.checkDeveloperInDBbyId(id);

        assertEquals(developerEntityDB, developerEntity);

        when(developerRepository.findById(id)).thenReturn(null);

        assertThrows(DeveloperNotFoundException.class, () ->
                developerService.checkDeveloperInDBbyId(id));
    }
    @Test
    public void checkDeveloperInDBbyNameAndEmailTest () throws DeveloperNotFoundException {
        String name = "user_1";
        String email = "email_1";
        long id = 1L;
        DeveloperEntity developerEntity = new DeveloperEntity(name, email, id);

        when(developerRepository.findByNameAndEmail(name, email)).thenReturn(developerEntity);
        DeveloperEntity developerEntityDB = developerService.checkDeveloperInDBbyNameAndEmail(name, email);

        assertEquals(developerEntityDB, developerEntity);

        when(developerRepository.findByNameAndEmail(any(String.class), any(String.class))).thenReturn(null);

        assertThrows(DeveloperNotFoundException.class, () ->
                developerService.checkDeveloperInDBbyNameAndEmail(name, email));
    }
    @Test
    public void requestsUpdateValidateTest () throws DeveloperUpdateBadRequestException {
        DeveloperEntity developerEntityDB = new DeveloperEntity("user_1", "email_1");
        DeveloperEntity developerEntityNew_1 = new DeveloperEntity("user_1", "email_1");
        DeveloperEntity developerEntityNew_2 = new DeveloperEntity("user_2", "email_2");

        assertTrue(developerService.requestsUpdateValidate(developerEntityDB, developerEntityNew_2));

        assertThrows(DeveloperUpdateBadRequestException.class, () ->
                developerService.requestsUpdateValidate(developerEntityDB, developerEntityNew_1));
    }
}
