package ec.com.leodev.fundamentals.controller;

import ec.com.leodev.fundamentals.entity.Posts;
import ec.com.leodev.fundamentals.entity.User;
import ec.com.leodev.fundamentals.repository.IPostRepository;
import ec.com.leodev.fundamentals.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// La aplicación carga 12 usuarios de ejemplo al arrancar (ver FundamentalsSpringBootApplication)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IPostRepository postRepository;

    @Test
    void shouldListTheUsersLoadedAtStartup() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(12)));
    }

    @Test
    void shouldPaginateTheUsers() throws Exception {
        mockMvc.perform(get("/users/all/0/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.totalElements").value(12));
    }

    @Test
    void shouldFindAUserByEmail() throws Exception {
        mockMvc.perform(get("/users/byEmail/john@domain.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void shouldReturn404WhenTheUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/users/byEmail/nadie@domain.com")).andExpect(status().isNotFound());
        mockMvc.perform(get("/users/find/99999")).andExpect(status().isNotFound());
        mockMvc.perform(delete("/users/delete/99999")).andExpect(status().isNotFound());
    }

    @Test
    void shouldSerializeAUserWithPostsWithoutInfiniteRecursion() throws Exception {
        User user = userRepository.findMyUserByEmail("john@domain.com").orElseThrow(IllegalStateException::new);
        Posts post = postRepository.save(new Posts("Mi primer post", user));
        user.getPosts().add(post);

        mockMvc.perform(get("/users/find/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts", hasSize(1)))
                .andExpect(jsonPath("$.posts[0].description").value("Mi primer post"))
                .andExpect(jsonPath("$.posts[0].user").doesNotExist());
    }

    @Test
    void shouldCreateAUser() throws Exception {
        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Nueva\", \"email\": \"nueva@domain.com\", \"birthDate\": \"2000-01-15\"}"))
                .andExpect(status().isCreated());

        assertEquals(LocalDate.of(2000, 1, 15),
                userRepository.findMyUserByEmail("nueva@domain.com").orElseThrow(IllegalStateException::new).getBirthDate());
    }

    @Test
    void shouldUpdateTheUserOfTheUrlInsteadOfCreatingAnotherOne() throws Exception {
        User user = userRepository.findMyUserByEmail("julie@domain.com").orElseThrow(IllegalStateException::new);
        long before = userRepository.count();

        mockMvc.perform(put("/users/update/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Julia\", \"email\": \"julie@domain.com\"}"))
                .andExpect(status().isOk());

        assertEquals(before, userRepository.count());
        assertEquals("Julia", userRepository.findById(user.getId()).orElseThrow(IllegalStateException::new).getName());
    }

    @Test
    void shouldDeleteAUser() throws Exception {
        User user = userRepository.findMyUserByEmail("oscar@domain.com").orElseThrow(IllegalStateException::new);

        mockMvc.perform(delete("/users/delete/" + user.getId())).andExpect(status().isAccepted());

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }
}
