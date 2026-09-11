package org.example.bankappuserservice.userRegistration.infra.adapter.inbound;


import jakarta.validation.ConstraintViolationException;
import org.example.bankappuserservice.userRegistration.application.exception.CpfAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.application.exception.EmailAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserInput;
import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private final CreateUserUseCase createUserUseCase =
            Mockito.mock(CreateUserUseCase.class);

    private final UserController controller = new UserController(createUserUseCase);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    private CreateUserInput input;

    private String json;

    @BeforeEach
    void setup() {

        input = new CreateUserInput(
                "user",
                "12345678910",
                "user@gmail.com",
                "75988887777",
                "12345678");

        json = objectMapper.writeValueAsString(input);



        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

    }

    @Test
    void shouldReturn201CreatedAndExecute() throws Exception {

        String json = objectMapper.writeValueAsString(input);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)

        ).andExpect(status().isCreated());

        Mockito.verify(createUserUseCase).execute(input);

    }

    @Test
    void shouldReturn409WhenCpfAlreadyExists() throws Exception {

        Mockito.when(createUserUseCase.execute(input))
                .thenThrow(CpfAlreadyExistsException.class);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isConflict());

        Mockito.verify(createUserUseCase).execute(input);
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists () throws Exception{

        Mockito.when(createUserUseCase.execute(input))
                .thenThrow(EmailAlreadyExistsException.class);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isConflict());

        Mockito.verify(createUserUseCase).execute(input);

    }

    @Test
    void shouldReturn400WhenConstraintViolationOccurs() throws Exception{

        Mockito.when(createUserUseCase.execute(input))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest());

        Mockito.verify(createUserUseCase).execute(input);

    }

}
