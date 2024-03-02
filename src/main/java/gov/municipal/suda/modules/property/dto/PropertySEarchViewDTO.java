package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_property_view")
public class PropertySEarchViewDTO {
    @Id
    private Long id;
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