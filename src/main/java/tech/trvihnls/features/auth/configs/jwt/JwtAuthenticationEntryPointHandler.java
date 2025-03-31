package tech.trvihnls.features.auth.configs.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.dtos.ApiResponse.ErrorResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(List.of(authException.getMessage()))
                .build();
        ResponseEntity<ApiResponse<Void>> result = ResponseUtils.error(ErrorCodeEnum.UNAUTHENTICATED, errorResponse);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String errorJson = mapper.writeValueAsString(result.getBody());
        response.getWriter().write(errorJson);
        response.flushBuffer();
    }

}
