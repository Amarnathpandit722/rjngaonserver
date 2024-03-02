package gov.municipal.suda.modules.property.dto;

import gov.municipal.suda.exception.BadRequestException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PropertyGeoLocationDTO {
    @NonNull
    private Long property_latitude;
    @NonNull
    private Long property_longitude;

//    @NonNull
//    private Long prop_id;
    @NonNull
    private String property_pictures_location;

    private PropertyGeoLocationDTO(final Long property_latitude, final Long property_longitude, final String property_pictures_location){

        if(property_latitude==null) {
            throw new BadRequestException("Latitude can't be blank");
        }
        if(property_longitude==null) {
            throw new BadRequestException("Longitude can't be blank");
        }
//        if(prop_id==null) {
//            throw new BadRequestException("Property Id can't be blank");
//        }
        if(StringUtils.isBlank(property_pictures_location)) {
            throw new BadRequestException("Property Pictures location can't be blank");
        }
        this.property_latitude=property_latitude;
        this.property_longitude=property_longitude;
        //this.prop_id=prop_id;
        this.property_pictures_location=property_pictures_location;
    }


}
