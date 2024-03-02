package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RenterEntryDTO {
    private Long  consumer_mstr_id;
    private Long  consumer_dtl_id;
    private Long  ward_id;
    private String renter_name;
    private Long renter_mobile_no;
    private String house_flat_no;
    private String renter_gradian_name;
    private String renter_relation;
    private Long created_byid;
    private List<AreaDetailsDto> area_details;

}
