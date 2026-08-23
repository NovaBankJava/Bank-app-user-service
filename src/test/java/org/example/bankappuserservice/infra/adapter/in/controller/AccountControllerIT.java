package org.example.bankappuserservice.infra.adapter.in.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String userId = UUID.randomUUID().toString();

    private static final String VALID_CPF = "52998224725";

    @Test
    void createsAndListsAccount() throws Exception {
        mockMvc.perform(post("/api/v1/accounts/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(VALID_CPF, "123456", "CHECKING", true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(0)))
                .andExpect(jsonPath("$.data.primary", is(true)))
                .andExpect(jsonPath("$.data.bank", is("NovaBank")));

        mockMvc.perform(get("/api/v1/accounts/listAccounts/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(0)))
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void invalidCpfIsRejectedAtTheBoundary() throws Exception {
        // "99999999999" fails the @CPF check on the DTO, so @Valid rejects it
        // before the service runs -> MethodArgumentNotValidException -> INVALID_INPUT (1)
        mockMvc.perform(post("/api/v1/accounts/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("99999999999", "123456", "CHECKING", false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(1)));
    }

    @Test
    void duplicateAccountReturnsDuplicateStatus() throws Exception {
        createAccount("777777");

        mockMvc.perform(post("/api/v1/accounts/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(VALID_CPF, "777777", "CHECKING", false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(6)));
    }

    @Test
    void deleteNonExistentReturnsThirdPartyStatus() throws Exception {
        mockMvc.perform(delete("/api/v1/accounts/deleteAccount/{userId}/{accountId}",
                        userId, "does-not-exist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(2)));
    }

    @Test
    void setPrimarySwitchesAccount() throws Exception {
        String first = createAccount("111111");
        String second = createAccount("222222");

        mockMvc.perform(patch("/api/v1/accounts/setPrimaryAccount/{userId}/{accountId}",
                        userId, second))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(0)));

        MvcResult result = mockMvc.perform(
                        get("/api/v1/accounts/listAccounts/{userId}", userId))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode accounts = objectMapper
                .readTree(result.getResponse().getContentAsString())
                .get("data");

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

    private String createAccount(String number) throws Exception {
        MvcResult result = mockMvc.perform(
                        post("/api/v1/accounts/createAccount")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body(VALID_CPF, number, "CHECKING", false)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper
                .readTree(result.getResponse().getContentAsString())
                .get("data")
                .get("id")
                .asText();
    }

    private String body(String cpf, String number, String type, boolean primary) {
        return """
                {
                    "userId": "%s",
                    "cpf": "%s",
                    "bank": "NovaBank",
                    "branch": "0001",
                    "accountNumber": "%s",
                    "type": "%s",
                    "setAsPrimary": %s
                }
                """.formatted(userId, cpf, number, type, primary);
    }
}