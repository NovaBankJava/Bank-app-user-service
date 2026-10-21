package org.example.bankappuserservice.userRegistration.infra.adapter.inbound;


import jakarta.validation.ConstraintViolationException;
import org.example.bankappuserservice.userRegistration.application.exception.CpfAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.application.exception.EmailAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserInput;
import org.example.bankappuserservice.userRegistration.domain.ports.in.CreateUserUseCase;
import org.example.bankappuserservice.userRegistration.infra.adapter.dtos.Request.UserRequestDTO;
import org.example.bankappuserservice.userRegistration.infra.adapter.dtos.mapper.UserRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private final CreateUserUseCase createUserUseCase =
            Mockito.mock(CreateUserUseCase.class);

    @Autowired
    private final UserRequestMapper userRequestMapper =
            new UserRequestMapper();

    private final UserController controller = new UserController(createUserUseCase, userRequestMapper);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    private CreateUserInput input;

    private UserRequestDTO dto;

    private String json;

    @BeforeEach
    void setup() {

        dto = new UserRequestDTO(
                "user",
                "233.543.771-27",
                "user@gmail.com",
                "75988887777",
                "12345678"
        );

        input = new CreateUserInput(
                "user",
                "233.543.771-27",
                "user@gmail.com",
                "75988887777",
                "12345678");

        json = objectMapper.writeValueAsString(dto);



        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

    }

    @Test
    void shouldReturn201CreatedAndExecute() throws Exception {

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

       UserRequestDTO invalidDto = new UserRequestDTO(
               " ",
               "233.543.771-27",
               "user@gmail.com",
               "75988887777",
               "12345678"
       );

       String invalidJson = objectMapper.writeValueAsString(invalidDto);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        ).andExpect(status().isBadRequest());

        Mockito.verify(createUserUseCase,never()).execute(input);

    }

}
