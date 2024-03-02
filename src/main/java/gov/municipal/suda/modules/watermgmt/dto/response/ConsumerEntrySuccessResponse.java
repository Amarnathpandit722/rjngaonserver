package gov.municipal.suda.modules.watermgmt.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerEntrySuccessResponse {
    private Long consumerId;
    private String consumerNo;
}
