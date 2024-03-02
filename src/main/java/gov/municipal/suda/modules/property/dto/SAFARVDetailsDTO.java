package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SAFARVDetailsDTO {
    private Long id;
    @NotNull(message="usage_type_id shouldn't be null")
    private Long usage_type_id;
    @NotNull(message="uses_type_name shouldn't be null")
    private String uses_type_name;
    @NotNull(message="occup_type_id shouldn't be null")
    private Long occup_type_id;
    @NotNull(message="occupancy_type shouldn't be null")
    private String occupancy_type;
    @NotNull(message="construction_type shouldn't be null")
    private String construction_type;
    @NotNull(message="floor_id shouldn't be null")
    private Long floor_id;
    @NotNull(message="floor_name shouldn't be null")
    private String floor_name;
    @NotNull(message="built_up_area shouldn't be null")
    private String built_up_area;
    
    @NotNull(message="From Date shouldn't be null")
    private String from_date; // It is use under the floor Details for calculate ARV (Annual Rental Value)
    private String to_date;  // if this is null than it's take current financial year by default and it is handled from the UI, It is use under the floor Details for calculate ARV
    //change on 21-02-2024 by suraj as per client
        @NotNull(message="zone_id shouldn't be null")
  private Long zone_id;  // get from tbl_zone_mstr and insert
   
    
    
    
}
