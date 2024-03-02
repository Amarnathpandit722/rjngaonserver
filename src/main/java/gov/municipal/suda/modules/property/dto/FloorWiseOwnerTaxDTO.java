package gov.municipal.suda.modules.property.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FloorWiseOwnerTaxDTO {
    private Long prop_id;
    private String occupancy_type;
    private String construction_type;
    private Long uses_type_id;
    private String built_up_area;
    private String from_date;
    private String to_date;
    private Long user_id;
    private String isMobileTower; // Yes or No input value
    private String is_isdp;
    private String is_school;
    private String is_complex;
    private List<OwnerTaxDTO> floorWiseOwnerTax;

}
