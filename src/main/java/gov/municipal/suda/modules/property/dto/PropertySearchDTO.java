package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.sql.Timestamp;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PropertySearchDTO {

    private  String ward_id;
    private  String entry_type;
    @Column(unique=true)
    private  String property_no;
    private  String fy_name;
    private  String property_type_name;
    private  String uses_type_name;
    private  String mohalla;
    private  String property_address;
    private  String city;
    private  String district;
    private  String pincode;
    private BigDecimal totalbuilbup_area;
    private  String plot_no;
    private  String khata_no;
    private  String building_name;
    private Timestamp stampdate;
}
