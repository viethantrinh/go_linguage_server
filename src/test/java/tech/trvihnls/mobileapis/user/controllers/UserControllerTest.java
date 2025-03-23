package tech.trvihnls.mobileapis.user.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tech.trvihnls.mobileapis.user.services.UserService;

// TODO: test
@ActiveProfiles({"test"})
@WebMvcTest({UserController.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


}
