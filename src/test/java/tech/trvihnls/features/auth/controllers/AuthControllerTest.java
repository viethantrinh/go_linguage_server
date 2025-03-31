package tech.trvihnls.features.auth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.auth.dtos.request.SignInRequest;
import tech.trvihnls.features.auth.dtos.request.SignUpRequest;
import tech.trvihnls.features.auth.dtos.response.SignInResponse;
import tech.trvihnls.features.auth.services.AuthService;
import tech.trvihnls.features.auth.services.JwtService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO: test
@ActiveProfiles("test")
@WebMvcTest({AuthController.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private String signInApiPath;
    private String signUpApiPath;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;

    @BeforeEach
    void setUp() {
        signInApiPath = "/auth/sign-in";
        signUpApiPath = "/auth/sign-up";
        signInRequest = SignInRequest.builder()
                .email("hntrnn12@gmail.com")
                .password("Sohappy212@")
                .build();
        signUpRequest = SignUpRequest.builder()
                .email("hntrnn13@gmail.com")
                .password("Sohappy212@")
                .name("han")
                .build();
    }

@Test
@DisplayName(value = "1 - sign in success")
@Tag(value = "sign-in")
@Order(1)
void testSignInSuccess() throws Exception {
    // arrange
    String requestBody = objectMapper.writeValueAsString(signInRequest);

    // Use Mockito's any() to be more flexible with argument matching
    when(authService.signIn(any(SignInRequest.class)))
            .thenReturn(SignInResponse.builder().token("JWT_TOKEN").build());

    // act
    ResultActions resultActions = mockMvc.perform(post(signInApiPath)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody));

    // assert
    resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.code", is(1000)))
            .andExpect(jsonPath("$.message", is("sign in succeeded")))
            .andDo(print());
}

    @Test
    @DisplayName(value = "2 - sign in failed since email is missing")
    @Tag(value = "sign-in")
    @Order(2)
    void testSignInFailedBecauseMissingEmail() throws Exception {
        // arrange
        signInRequest.setEmail(null);
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        when(authService.signIn(signInRequest))
                .thenReturn(SignInResponse.builder().token("JWT_TOKEN").build());

        // act
        ResultActions resultActions = mockMvc.perform(post(signInApiPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // assert
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.BAD_REQUEST.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.BAD_REQUEST.getMessage())))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "3 - sign in failed since password is missing")
    @Tag(value = "sign-in")
    @Order(3)
    void testSignInFailedBecauseMissingPassword() throws Exception {
        // arrange
        signInRequest.setPassword(null);
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        when(authService.signIn(signInRequest))
                .thenReturn(SignInResponse.builder().token("JWT_TOKEN").build());

        // act
        ResultActions resultActions = mockMvc.perform(post(signInApiPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // assert
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.BAD_REQUEST.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.BAD_REQUEST.getMessage())))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "4 - sign in failed since email less than 4 characters")
    @Tag(value = "sign-in")
    @Order(4)
    void testSignInFailedBecauseEmailLessThan4Characters() throws Exception {
        // arrange
        signInRequest.setEmail("h@.");
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        when(authService.signIn(signInRequest))
                .thenReturn(SignInResponse.builder().token("JWT_TOKEN").build());

        // act
        ResultActions resultActions = mockMvc.perform(post(signInApiPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // assert
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.BAD_REQUEST.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.BAD_REQUEST.getMessage())))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "5 - sign in failed since email more than 64 characters")
    @Tag(value = "sign-in")
    @Order(5)
    void testSignInFailedBecauseEmailMoreThan64Characters() throws Exception {
        // arrange
        signInRequest.setEmail("dfad".repeat(100));
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        when(authService.signIn(signInRequest))
                .thenReturn(SignInResponse.builder().token("JWT_TOKEN").build());

        // act
        ResultActions resultActions = mockMvc.perform(post(signInApiPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // assert
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.BAD_REQUEST.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.BAD_REQUEST.getMessage())))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "6 - sign in failed since email is invalid")
    @Tag(value = "sign-in")
    @Order(6)
    void testSignInFailedBecauseEmailIsInvalid() throws Exception {
        // arrange
        signInRequest.setEmail("hntrnn12gmail,com");
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        when(authService.signIn(signInRequest))
                .thenReturn(SignInResponse.builder().token("JWT_TOKEN").build());

        // act
        ResultActions resultActions = mockMvc.perform(post(signInApiPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // assert
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.BAD_REQUEST.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.BAD_REQUEST.getMessage())))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "7 - sign in failed since password less than 8 characters")
    @Tag(value = "sign-in")
    @Order(7)
    void testSignInFailedBecausePasswordLessThan8Characters() throws Exception {
        // arrange
        signInRequest.setPassword("h@.");
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        when(authService.signIn(signInRequest))
                .thenReturn(SignInResponse.builder().token("JWT_TOKEN").build());

        // act
        ResultActions resultActions = mockMvc.perform(post(signInApiPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // assert
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.BAD_REQUEST.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.BAD_REQUEST.getMessage())))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "8 - sign in failed since password more than 64 characters")
    @Tag(value = "sign-in")
    @Order(8)
    void testSignInFailedBecausePasswordMoreThan64Characters() throws Exception {
        // arrange
        signInRequest.setPassword("dfad".repeat(100));
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        when(authService.signIn(signInRequest))
                .thenReturn(SignInResponse.builder().token("JWT_TOKEN").build());

        // act
        ResultActions resultActions = mockMvc.perform(post(signInApiPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // assert
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.BAD_REQUEST.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.BAD_REQUEST.getMessage())))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "9 - sign in failed since email is not existed")
    @Tag(value = "sign-in")
    @Order(9)
    void testSignInFailedBecauseEmailIsNotExisted() throws Exception {
        // arrange
        System.out.println(authService);
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        when(authService.signIn(any(SignInRequest.class)))
                .thenThrow(new AppException(ErrorCodeEnum.USER_NOT_EXISTED));

        // act
        ResultActions resultActions = mockMvc.perform(post(signInApiPath)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // assert
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.USER_NOT_EXISTED.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.USER_NOT_EXISTED.getMessage())))
                .andDo(print());
    }
}
