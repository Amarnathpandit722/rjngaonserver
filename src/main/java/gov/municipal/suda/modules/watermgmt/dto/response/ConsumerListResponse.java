package gov.municipal.suda.modules.watermgmt.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerListResponse {
    private String wardNo;
    private String propertyNo;
    private String consumerNo;
    private String consumerName;
    private Long mobileNo;
    private String address;

}
