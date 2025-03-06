package tech.trvihnls.models.dtos.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "code", "message", "timestamp", "errorResponse", "data" })
public class ApiResponse<T> {

    private int code;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private ErrorResponse errorResponse;

    private T result;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonPropertyOrder({ "apiPath", "errors" })
    public static class ErrorResponse {

        private String apiPath;

        @Builder.Default
        private List<String> errors = new ArrayList<>();
    }
}
