package gov.municipal.suda.modules.watermgmt.dto.transaction;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchConsumerRequestDto {

    private Long wardId;
    private String propertyNo;
    private String consumerNo;
    private Long mobileNo;
    private String consumerName;
}
