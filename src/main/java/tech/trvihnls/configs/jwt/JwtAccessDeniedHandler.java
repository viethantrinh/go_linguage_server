package tech.trvihnls.configs.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tech.trvihnls.models.dtos.base.ApiResponse;
import tech.trvihnls.models.dtos.base.ApiResponse.ErrorResponse;
import tech.trvihnls.utils.ResponseUtils;
import tech.trvihnls.utils.enums.ErrorCode;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(request.getServletPath())
                .errors(List.of(accessDeniedException.getMessage()))
                .build();
        ResponseEntity<ApiResponse<Void>> result = ResponseUtils.error(ErrorCode.UNAUTHORIZED, errorResponse);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String errorJson = mapper.writeValueAsString(result.getBody());
        response.getWriter().write(errorJson);
        response.flushBuffer();
    }

}
