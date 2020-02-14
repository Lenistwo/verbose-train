package pl.lenistwo.restexample.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pl.lenistwo.restexample.RestExampleApplication;
import pl.lenistwo.restexample.entities.User;
import pl.lenistwo.restexample.exceptions.LimitCannotBeLessThanOneException;
import pl.lenistwo.restexample.exceptions.OffsetCannotBeLessThanZeroException;
import pl.lenistwo.restexample.repositories.UserRepository;
import pl.lenistwo.restexample.utills.OffsetPageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(JUnit4.class)
@ContextConfiguration(classes = RestExampleApplication.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserRepository repository;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static private List<User> users;

    @Autowired
    private MockMvc mvc;

    @BeforeAll
    static void fill_db() {
        users = new ArrayList<>(Arrays.asList(
                new User("Lenistwo", "Kacper", "Gmail"),
                new User("Tremek", "Matuesz", "onet"),
                new User("Kikrun", "Emil", "o2"),
                new User("Ania", "Ania", "wp")
        ));
    }

    @AfterAll
    static void clear_db() {
        users.clear();
    }

    @Test
    void should_return_all_users() throws Exception {
        given(repository.findAll()).willReturn(users);
        mvc.perform(get("/user-api/all-users")).andExpect(status().isOk()).andExpect(content().json(gson.toJson(users)));
    }

    @Test
    void getUserById() throws Exception {
        Optional<User> user = Optional.of(users.get(1));
        given(repository.findById(1L)).willReturn(user);
        mvc.perform(get("/user-api/user?id=" + 1)).andExpect(status().isOk()).andExpect(content().json(gson.toJson(users.get(1))));
    }

    @Test
    void shouldHandleUserIdOutOfBound() throws Exception {
        var id = 123;
        mvc.perform(get("/user-api/user?id=" + id)).andExpect(status().isOk()).andExpect(content().string("User with ".concat(String.valueOf(id)).concat(" doesnt exist")));
    }

    @Test
    void getAllWithLimit() throws Exception {
        int limit = 2;
        List<User> limitedList = users.subList(0, 2);
        given(repository.findAll(PageRequest.of(0, limit))).willReturn(limitedList);
        mvc.perform(get("/user-api/all-with-limit?limit=".concat(String.valueOf(limit)))).andExpect(status().isOk()).andExpect(content().json(gson.toJson(limitedList)));
    }

    @Test
    void getAllWithSkipAndLimit() throws Exception {
        int skip = 2;
        int limit = users.size();
        List<User> skippedAndLimitedList = users.subList(skip, limit);
        given(repository.findAll(new OffsetPageRequest(skip, limit))).willReturn(skippedAndLimitedList);
        mvc.perform(get("/user-api/all-with-skip?skip=".concat(String.valueOf(skip)).concat("&limit=" + limit)))
                .andExpect(status().isOk()).andExpect(content().json(gson.toJson(skippedAndLimitedList)));
    }

    @Test
    void shouldThrowOffsetCannotBeLessThanZeroException() {
        assertThrows(OffsetCannotBeLessThanZeroException.class, () ->
                new OffsetPageRequest(-1, 1)
        );
    }

    @Test
    void shouldThrowLimitCannotBeLessThanOneException() {
        assertThrows(LimitCannotBeLessThanOneException.class, () ->
                new OffsetPageRequest(0, 0)
        );
    }

    @Test
    void deleteUserWithId() throws Exception {
        mvc.perform(delete("/user-api/delete-user?id=".concat(String.valueOf(1)))).andExpect(status().isOk());
    }

    @Test
    void createUser() throws Exception {
        User user = new User("Adam", "Wie", "Jak");
        mvc.perform(post("/user-api/create-user").contentType(MediaType.APPLICATION_JSON_VALUE).content(gson.toJson(user))).andExpect(status().isCreated());
    }
}
