package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerViewDTO {
    private Long consumer_master_id;
    private Long consumer_details_id;
    private String consumer_no;
    private String consumer_name;
    private Long ward_no;
    private String holding_no;
    private Long mobile_no;
    private String consumer_type;
}
