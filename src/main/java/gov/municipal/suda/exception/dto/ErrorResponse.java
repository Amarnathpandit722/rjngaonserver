package gov.municipal.suda.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String status ="Failure";
    private List<ErrorDTO> errors;

    public ErrorResponse(List<ErrorDTO> errors)
    {
        this.errors=errors;
    }
}
