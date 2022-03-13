package gp.developer.api.task_3;

import gp.developer.api.task_3.controllers.DeveloperController;
import gp.developer.api.task_3.entity.DeveloperEntity;

import gp.developer.api.task_3.exception.DeveloperMailNotUniqueException;
import gp.developer.api.task_3.exception.DeveloperNotFoundException;
import gp.developer.api.task_3.exception.DeveloperUpdateBadRequestException;
import gp.developer.api.task_3.exception.NameValidateException;
import gp.developer.api.task_3.service.DeveloperService;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(DeveloperController.class)
class DeveloperControllerTest {

    @MockBean
    private DeveloperService developerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addDeveloperOk() throws Exception {

        DeveloperEntity developerEntity = new DeveloperEntity("user_1","email_1", 1L);

        String developerJson = "{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";
        when(developerService.addDeveloper(any(DeveloperEntity.class))).thenReturn(developerEntity);

        String jsonAnswer =  mockMvc.perform(MockMvcRequestBuilders
                .post("/developer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(developerJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals("{id:1}", jsonAnswer, false);
        JSONAssert.assertEquals("{name:user_1}", jsonAnswer, false);
        JSONAssert.assertEquals("{email:email_1}", jsonAnswer, false);
    }
    @Test
    public void addUserException() throws Exception {

        when(developerService.addDeveloper(any(DeveloperEntity.class)))
                .thenThrow(new DeveloperMailNotUniqueException());

        String answer = mockMvc.perform(MockMvcRequestBuilders
                .post("/developer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("ERROR Developer with this email exists", answer);

        when(developerService.addDeveloper(any(DeveloperEntity.class)))
                .thenThrow(new NameValidateException(any(String.class)));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/developer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

    }
    @Test
    void getDeveloperOK() throws Exception {

        long id = 1L;

        DeveloperEntity developerEntity = new DeveloperEntity("user_1","email_1", id);

        when(developerService.getDeveloper(id)).thenReturn(developerEntity);

        String jsonAnswer =  mockMvc.perform(get("/developer/")
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals("{id:1}", jsonAnswer, false);
        JSONAssert.assertEquals("{name:user_1}", jsonAnswer, false);
        JSONAssert.assertEquals("{email:email_1}", jsonAnswer, false);
    }

    @Test
    void getDeveloperException() throws Exception {

        when(developerService.getDeveloper(any(long.class))).thenThrow(new DeveloperNotFoundException());

        String answer =  mockMvc.perform(get("/developer/")
                .param("id", String.valueOf(any(long.class))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("ERROR The developer with this requested parameters was not found", answer);
    }

    @Test
    void deleteUserOK() throws Exception {

        String developerJson = "{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";

        String messageDelOk ="OK Developer has been successfully deleted";

        when(developerService.deleteDeveloper(any(DeveloperEntity.class))).thenReturn(messageDelOk);

        String answer =  mockMvc.perform(MockMvcRequestBuilders
                .delete("/developer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(developerJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(messageDelOk, answer);
    }

    @Test
    void updateUserOK() throws Exception {

        long id = 1L;
        DeveloperEntity developerEntityNew = new DeveloperEntity("user_2","email_2");
        DeveloperEntity developerEntityBD = new DeveloperEntity("user_2","email_2", id);

        String developerJson = "{\n" +
                "    \"name\":\"user_2\",\n" +
                "    \"email\":\"email_2\"\n" +
                "}";

        when(developerService.updateDeveloper(developerEntityNew, id)).thenReturn(developerEntityBD);

        String jsonAns =  mockMvc.perform(put("/developer/")
                .param("id", String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(developerJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals("{id:1}", jsonAns, false);
        JSONAssert.assertEquals("{name:user_2}", jsonAns, false);
        JSONAssert.assertEquals("{email:email_2}", jsonAns, false);
    }
    @Test
    void updateUserException() throws Exception {

        when(developerService.updateDeveloper(any(DeveloperEntity.class), any(long.class)))
                                        .thenThrow(new DeveloperUpdateBadRequestException());

        String answer =  mockMvc.perform(put("/developer/")
                .param("id", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("ERROR Developer already has these options", answer);
    }
}