package gov.municipal.suda.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO {
    private String status;
    private String message;


}
