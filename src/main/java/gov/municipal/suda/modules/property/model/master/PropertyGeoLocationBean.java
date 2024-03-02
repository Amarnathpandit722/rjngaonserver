package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_prop_geolocation")
public class PropertyGeoLocationBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prop_geolocation_id;
    private Long property_id;
    private String property_latitude;
    private String property_longitude;
    private String property_pictures_location;
}
