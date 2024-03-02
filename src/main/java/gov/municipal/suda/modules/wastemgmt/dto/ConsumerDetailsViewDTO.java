package gov.municipal.suda.modules.wastemgmt.dto;

import gov.municipal.suda.util.common.LastPaymentUpdateMonthDropDownDto;
import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerDetailsViewDTO {
    private Long consumer_mstr_id;
    private String consumer_no;
    private String consumer_name;
    private String gradian_name;
    private Long ward_no;
    private String holding_no;
    private Long mobile_no;
    private String consumer_type;
    private String relation;
    private String  house_flat_no;
    private String police_station;
    private String land_mark;
    private String  address;
    private String  ward_id;
    private List<LastPaymentUpdateMonthDropDownDto> dropDown;

}
