package gov.municipal.suda.modules.watermgmt.dto.master;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerConnectionDetailsDto {
    private Long connectionType;
    private String dateOfConnection;

}
