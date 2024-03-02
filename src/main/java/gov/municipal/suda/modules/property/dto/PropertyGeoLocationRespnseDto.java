package gov.municipal.suda.modules.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PropertyGeoLocationRespnseDto {
    private String property_latitude;
    private String property_longitude;
    private List<String> urls;
}
