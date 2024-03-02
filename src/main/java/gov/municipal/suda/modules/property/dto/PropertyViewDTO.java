package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.Entity;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PropertyViewDTO {
    private BigInteger id;
    private BigInteger ward_id;
    private String ward_name;
    private  String property_no;
    private  String application_no;
    private  String entry_type;
    private String owner_name;
    private  String owner_address;
    private BigInteger entry_fy_id;
    private String fy_name;

}