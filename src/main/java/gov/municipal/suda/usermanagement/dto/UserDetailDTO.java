package gov.municipal.suda.usermanagement.dto;

import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDTO {
    private BigInteger id;
    private BigInteger user_id;
    private String employee_name;
    private String user_name;
    private String designation;
    private String is_active;
}
