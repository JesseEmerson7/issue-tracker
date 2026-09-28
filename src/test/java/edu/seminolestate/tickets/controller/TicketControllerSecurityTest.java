package edu.seminolestate.tickets.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void rejectsTicketWithoutAccessToken() throws Exception {
        mockMvc.perform(get("/tickets/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid request"));
    }

    @Test
    void rejectsTicketWithIncorrectAccessToken() throws Exception {
        mockMvc.perform(get("/tickets/1").param("token", "wrong-token"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Ticket access denied"));
    }

    @Test
    void rejectsStateChangeWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/tickets/1/status")
                        .param("token", "sample-token-1001")
                        .param("status", "CLOSED"))
                .andExpect(status().isForbidden());
    }

    @Test
    void rejectsInvalidTicketInput() throws Exception {
        mockMvc.perform(post("/tickets")
                        .with(csrf())
                        .param("requesterName", "")
                        .param("email", "not-an-email")
                        .param("category", "Software")
                        .param("description", "test"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid request"));
    }

    @Test
    void doesNotExposeExceptionDetails() throws Exception {
        mockMvc.perform(get("/tickets/1").param("token", "sample-token-1001"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Application error:"))))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Cause:"))));
    }
}
