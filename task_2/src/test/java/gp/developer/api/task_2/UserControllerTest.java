package gp.developer.api.task_2;

import gp.developer.api.task_2.entity.UserEntity;
import gp.developer.api.task_2.exception.DeveloperNotFoundException;
import gp.developer.api.task_2.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.core.Is.is;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void resetDb() {
        userRepository.deleteAll();
    }


    @Test //new user added and ID assigned
    public void addUserOk() throws Exception {

        String userJson = "{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";

        mockMvc.perform(post("/user/add")
                .content(userJson)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("user_1")))
                .andExpect(jsonPath("email", is("email_1")))
                .andExpect(jsonPath("id").isNumber());

        UserEntity userEntity = userRepository.findByNameAndEmail("user_1","email_1");

        assertNotNull(userEntity);
        assertNotNull(userEntity.getId());
        assertEquals("user_1", userEntity.getName());
        assertEquals("email_1", userEntity.getEmail());
    }
    @Test  //you can't add different users with the same email
    public void addUserERROR_1() throws Exception {

        userRepository.save(new UserEntity("user_1", "email_1"));

        String user1Json = "{\n" +
                "    \"name\":\"user_2\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";
        mockMvc.perform(post("/user/add")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        UserEntity userEntity = userRepository.findByNameAndEmail("user_1","email_1");

        assertNotNull(userEntity);
        assertNotNull(userEntity.getId());
    }
    @Test  //you cannot add a user with a name less than 2 letters or more than 20
    public void addUserERROR_2() throws Exception {

        String user1Json = "{\n" +
                "    \"name\":\"u\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";

        mockMvc.perform(post("/user/add")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        String user2Json = "{\n" +
                "    \"name\":\"user1user1user1user11\",\n" +
                "    \"email\":\"email_2\"\n" +
                "}";

        mockMvc.perform(post("/user/add")
                .content(user2Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        UserEntity userEntity1 = userRepository.findByName("u");
        assertNull(userEntity1);

        UserEntity userEntity2 = userRepository.findByName("user1user1user1user11");
        assertNull(userEntity2);
    }
    @Test  //you can't add a user if the name starts with a number
    public void addUserERROR_3() throws Exception {

        String user1Json = "{\n" +
                "    \"name\":\"1_user\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";

        mockMvc.perform(post("/user/add")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        UserEntity userEntity = userRepository.findByName("1_user");
        assertNull(userEntity);
    }
    @Test  //you can get the user if he is added to the database
    public void getUserOK() throws Exception {

        userRepository.save(new UserEntity ("user_1", "email_1"));

        String user1Json = "{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";

        mockMvc.perform(get("/user/get")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("user_1")))
                .andExpect(jsonPath("email", is("email_1")))
                .andExpect(jsonPath("id").isNumber());
    }

    @Test  //you can't get a user if it's not in the database
    public void getUserERROR_1() throws Exception {

        String user1Json = "{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";

        mockMvc.perform(get("/user/get")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        UserEntity userEntity = userRepository.findByNameAndEmail("user_1","email_1");
        assertNull(userEntity);
    }

    @Test   //you can delete the user if it is in the database
    void deleteUserOK() throws Exception {

        userRepository.save(new UserEntity ("user_1", "email_1"));

        String user1Json = "{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";

        mockMvc.perform(delete("/user/delete")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        UserEntity userEntity = userRepository.findByNameAndEmail("user_1","email_1");
        assertNull(userEntity);
    }
    @Test   //you cannot delete a user if it is not in the database
    void deleteUserERROR_1() throws Exception {

        UserEntity userEntity = userRepository.findByNameAndEmail("user_1","email_1");

        assertNull(userEntity);

        String user1Json = "{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}";

        mockMvc.perform(delete("/user/delete")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test   //If user 1 is found in the database, user 2 is not found, then user 1's data is replaced with user 2
    void updateUserOK() throws Exception {

        userRepository.save(new UserEntity("user_1", "email_1"));

        String user1Json = "[{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "},\n {\n" +
                "    \"name\":\"user_2\",\n" +
                "    \"email\":\"email_2\"\n" +
                "}]";

        mockMvc.perform(put("/user/update")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("user_2")))
                .andExpect(jsonPath("email", is("email_2")))
                .andExpect(jsonPath("id").isNumber());

        UserEntity userEntity2 = userRepository.findByNameAndEmail("user_2","email_2");
        assertNotNull(userEntity2);

        UserEntity userEntity1 = userRepository.findByNameAndEmail("user_1","email_1");
        assertNull(userEntity1);
    }
    @Test   //if there is a user with user 2 mail in the database, user 1 will not be updated
    void updateUserERROR_1() throws Exception {

        userRepository.save(new UserEntity("user_1", "email_1"));
        userRepository.save(new UserEntity("user_3", "email_2"));

        String user1Json = "[{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "},\n {\n" +
                "    \"name\":\"user_2\",\n" +
                "    \"email\":\"email_2\"\n" +
                "}]";

        mockMvc.perform(put("/user/update")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        UserEntity userEntity1 = userRepository.findByNameAndEmail("user_1","email_1");
        assertNotNull(userEntity1);

        UserEntity userEntity2 = userRepository.findByNameAndEmail("user_2","email_2");
        assertNull(userEntity2);

        UserEntity userEntity3 = userRepository.findByNameAndEmail("user_3", "email_2");
        assertNotNull(userEntity3);


    }
    @Test   //if there are not 2 users in the request, user 1 will not be updated
    void updateUserERROR_2() throws Exception {

        userRepository.save(new UserEntity("user_1", "email_1"));

        String user1Json = "[{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}]";
        String user2Json = "[{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "},\n {\n" +
                "    \"name\":\"user_2\",\n" +
                "    \"email\":\"email_2\"\n" +
                "},\n {\n" +
                "    \"name\":\"user_3\",\n" +
                "    \"email\":\"email_3\"\n" +
                "}]";

        mockMvc.perform(put("/user/update")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/user/update")
                .content(user2Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        UserEntity userEntity1 = userRepository.findByNameAndEmail("user_1","email_1");
        assertNotNull(userEntity1);

        UserEntity userEntity2 = userRepository.findByNameAndEmail("user_2","email_2");
        assertNull(userEntity2);

        UserEntity userEntity3 = userRepository.findByNameAndEmail("user_3","email_3");
        assertNull(userEntity3);

    }
    @Test   //If user 1 settings == user 2, user 1 will not update
    void updateUserERROR_3() throws Exception {

        userRepository.save(new UserEntity("user_1", "email_1"));

        String user1Json = "[{\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "},\n {\n" +
                "    \"name\":\"user_1\",\n" +
                "    \"email\":\"email_1\"\n" +
                "}]";

        mockMvc.perform(put("/user/update")
                .content(user1Json)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}