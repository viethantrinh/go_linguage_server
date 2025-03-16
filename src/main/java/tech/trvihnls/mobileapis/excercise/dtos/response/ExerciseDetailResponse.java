package tech.trvihnls.mobileapis.excercise.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "instruction", "displayOrder", "data"})
public class ExerciseDetailResponse<T> {
    private long id;
    private String instruction;
    private int displayOrder;
    private T data;
}
