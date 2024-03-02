package gov.municipal.suda.modules.watermgmt.dto.master;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerBasicDetailsDto {
    private String name;
    private Long mobileNo;
    private String relation;
    private String guardianName;
}
