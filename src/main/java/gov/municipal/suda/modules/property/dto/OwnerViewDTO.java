package gov.municipal.suda.modules.property.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OwnerViewDTO {
    private String owner_name;
    private String relation;
    private String guardian_name;

}
