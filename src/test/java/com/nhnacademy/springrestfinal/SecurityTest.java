package com.nhnacademy.springrestfinal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "marco", roles = {"ADMIN"})
    public void user1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @WithMockUser(username = "marco", roles = {"ADMIN"})
    public void user2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/member"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    @WithMockUser(username = "marco", roles = {"ADMIN"})
    public void user3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/google"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "jsj", roles = {"MEMBER"})
    public void member1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    @WithMockUser(username = "jsj", roles = {"MEMBER"})
    public void member2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/member"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @WithMockUser(username = "jsj", roles = {"MEMBER"})
    public void member3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/google"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"GOOGLE"})
    public void admin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_GOOGLE"})
    public void admin2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/member"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    @WithMockUser(username = "user", roles = {"GOOGLE"})
    public void admin3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/google"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}