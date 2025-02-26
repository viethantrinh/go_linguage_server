package tech.trvihnls.configs.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tech.trvihnls.models.dtos.base.ApiResponse;
import tech.trvihnls.models.dtos.base.ApiResponse.ErrorResponse;
import tech.trvihnls.utils.ErrorCode;
import tech.trvihnls.utils.ResponseUtils;

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
        ResponseEntity<ApiResponse<Void>> result = ResponseUtils.error(ErrorCode.UNAUTHENTICATED, errorResponse);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String errorJson = mapper.writeValueAsString(result.getBody());
        response.getWriter().write(errorJson);
        response.flushBuffer();
    }

}
