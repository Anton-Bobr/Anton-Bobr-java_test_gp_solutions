import gp.developer.api.task_2.controllers.UserController;
import gp.developer.api.task_2.entity.UserEntity;
import gp.developer.api.task_2.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.hamcrest.core.Is.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.given;





//@WebAppConfiguration
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest
@SpringBootTest(classes = UserController.class)
//@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void contextLoads(){
//    }

    @MockBean
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

//    @AfterEach
//    public void resetDb() {
//        userRepository.deleteAll();
//    }



    @Test
    public void addUserOk() throws Exception {

//        UserEntity userEntity = new UserEntity("user2", "email2");

//        Mockito.when(userRepository.save(Mockito.any())).thenReturn(userEntity);
//
//        mockMvc.perform(post("/user/add")
//                .content(asJson(userEntity))
//                .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("name", is("user1")))
//                .andExpect(jsonPath("email", is("email1")))
//                .andExpect(jsonPath("id", is("[\\d+]")));

//        String userJson = "{\n" +
//                "    \"name\":\"user2\",\n" +
//                "    \"email\":\"email2\"\n" +
//                "}";

//        Mockito.when(userRepository.save(Mockito.any())).thenReturn(userEntity);

//        mockMvc.perform(post("/user/add")
//                .content(userJson)
//                .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
////                .andExpect(jsonPath("name", is("user2")))
////                .andExpect(jsonPath("email", is("email2")))
////                .andExpect(jsonPath("id", is("[\\d+]")))
//                .andReturn();

//        UserEntity userEntity = new UserEntity("user2", "email2");

        userRepository.save(new UserEntity("user2", "email2"));
        UserEntity userEntity = userRepository.findByNameAndEmail("user2", "email2");
        assertNotNull(userEntity);
        assertEquals("user2", userEntity.getName());
        assertEquals("email2", userEntity.getEmail());
    }





//    @Test
//    public void getUser() throws Exception {
//
////        UserEntity userEntity = createTestUser("user2", "email2");
//
//        UserEntity userEntity = new UserEntity("user2", "email2");
//        userRepository.save(userEntity);
//
//        String userJson = "{\n" +
//                "    \"name\":\"user2\",\n" +
//                "    \"email\":\"email2\"\n" +
//                "}";
//
//        mockMvc.perform(get("/user/get")
//                .content(userJson)
//                .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("name").value("user2"))
//                .andExpect(jsonPath("email").value("email2"))
//                .andExpect(jsonPath("id").isNumber());
//
//    }

    //        mockMvc.perform(get("/user/get")
//                .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("name", is(employee.getName())));
//
//    @Test
//    void deleteUser() throws Exception {
//
//        createTestUser("user2", "email2");
//
//        String userJson = "{\n" +
//                "    \"name\":\"user2\",\n" +
//                "    \"email\":\"email2\"\n" +
//                "}";
//
//        mockMvc.perform(delete("/user/delete")
//                .content(userJson)
//                .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
////                .andExpect(jsonPath("name", is("user1")))
////                .andExpect(jsonPath("email", is("email1")))
////                .andExpect(jsonPath("id", is("[\\d+]")))
//                .andReturn();
//
//    }
//
//    @Test
//    void updateUser() {
//    }

    private static String asJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private UserEntity createTestUser(String name, String email) {
        UserEntity userEntity = new UserEntity(name, email);
        return userRepository.save(userEntity);
    }
}

//https://sysout.ru/testirovanie-kontrollerov-s-pomoshhyu-mockmvc/#:~:text=%D0%9A%D0%BB%D0%B0%D1%81%D1%81%20MockMvc%20%D0%BF%D1%80%D0%B5%D0%B4%D0%BD%D0%B0%D0%B7%D0%BD%D0%B0%D1%87%D0%B5%D0%BD%20%D0%B4%D0%BB%D1%8F%20%D1%82%D0%B5%D1%81%D1%82%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F,%2C%20%D1%82%D0%B0%D0%BA%20%D0%B8%20unit%2D%D1%82%D0%B5%D1%81%D1%82%D1%8B.