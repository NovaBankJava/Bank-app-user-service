package org.example.bankappuserservice.account.adapter.in.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String userId = UUID.randomUUID().toString();

    @Test
    void createsAndListsAccount() throws Exception {
        mockMvc.perform(post(base())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("0001", "123456", "CHECKING", true)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.primary", is(true)))
                .andExpect(jsonPath("$.bank", is("NovaBank")));

        mockMvc.perform(get(base()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deleteNonExistentReturns404() throws Exception {
        mockMvc.perform(delete(base() + "/{accountId}", "does-not-exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    void setPrimarySwitchesAccount() throws Exception {
        String first = createAccount("0001", "111111");
        String second = createAccount("0002", "222222");

        mockMvc.perform(patch(base() + "/{accountId}/primary", second))
                .andExpect(status().isNoContent());

        MvcResult result = mockMvc.perform(get(base()))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode accounts = objectMapper.readTree(result.getResponse().getContentAsString());
        for (JsonNode account : accounts) {
            String id = account.get("id").asText();
            boolean primary = account.get("primary").asBoolean();
            if (id.equals(second)) {
                org.assertj.core.api.Assertions.assertThat(primary).isTrue();
            } else if (id.equals(first)) {
                org.assertj.core.api.Assertions.assertThat(primary).isFalse();
            }
        }
    }

    private String createAccount(String branch, String number) throws Exception {
        MvcResult result = mockMvc.perform(post(base())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(branch, number, "CHECKING", false)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asText();
    }

    private String base() {
        return "/api/v1/users/" + userId + "/accounts";
    }

    private String body(String branch, String number, String type, boolean primary) {
        return """
                {"bank":"NovaBank","branch":"%s","accountNumber":"%s","type":"%s","setAsPrimary":%s}
                """.formatted(branch, number, type, primary);
    }
}