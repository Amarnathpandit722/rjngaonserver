package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerEntryDTO {

    private String  holding_no;
    private Long  ward_id;
    private String consumer_name;
    private Long mobile_no;
    private String house_flat_no;
    private String gradian_name;
    private String relation;
    private String  police_station;
    private String land_mark;
    private String  address;
    private Long created_byid;
    private Long  ward_no;
    private List<AreaDetailsDto> area_details;

}
