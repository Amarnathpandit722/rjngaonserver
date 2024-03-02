package gov.municipal.suda.usermanagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PwdChangeDTO {
    private Long id;
    private String userPassword;
}
