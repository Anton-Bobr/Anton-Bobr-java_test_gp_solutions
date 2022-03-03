package gp.developer.api.task_2;

import gp.developer.api.task_2.controllers.DeveloperController;
import gp.developer.api.task_2.entity.DeveloperEntity;

import gp.developer.api.task_2.service.DeveloperService;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;


@RunWith(SpringRunner.class)
@WebMvcTest(DeveloperController.class)
class DeveloperControllerTest {

    @MockBean
    private DeveloperService developerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addUserOk() throws Exception {

        DeveloperEntity developerEntity = new DeveloperEntity("user_1","email_1", 1L);

        String developerJson = "{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";
        when(developerService.addDeveloper(any(DeveloperEntity.class))).thenReturn(developerEntity);

        String jsonAnswer =  mockMvc.perform(MockMvcRequestBuilders
                .post("/developer/add")
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
    void getReceiptForOrder() throws Exception {

        DeveloperEntity developerEntity = new DeveloperEntity("user_1","email_1", 1L);

                String developerJson = "{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";

        when(developerService.getDeveloper(any(DeveloperEntity.class))).thenReturn(developerEntity);

        String jsonAns =  mockMvc.perform(get("/developer/get")
                .contentType(MediaType.APPLICATION_JSON)
                .content(developerJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals("{id:1}", jsonAns, false);
        JSONAssert.assertEquals("{name:user_1}", jsonAns, false);
        JSONAssert.assertEquals("{email:email_1}", jsonAns, false);
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
                .delete("/developer/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(developerJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(messageDelOk, answer);
    }

    @Test
    void updateUserOK() throws Exception {

        DeveloperEntity developerEntity = new DeveloperEntity("user_2","email_2", 2L);

        String developerJson = "[{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "},\n {\n" +
                "    \"name\":\"user_2\",\n" +
                "    \"email\":\"email_2\"\n" +
                "}]";

        when(developerService.updateDeveloper(any(List.class))).thenReturn(developerEntity);

        String jsonAns =  mockMvc.perform(put("/developer/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(developerJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals("{id:2}", jsonAns, false);
        JSONAssert.assertEquals("{name:user_2}", jsonAns, false);
        JSONAssert.assertEquals("{email:email_2}", jsonAns, false);
    }
}