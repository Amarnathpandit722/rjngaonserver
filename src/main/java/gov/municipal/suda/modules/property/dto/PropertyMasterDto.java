package gov.municipal.suda.modules.property.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyMasterDto {

    @NotNull(message ="{prop.mstr.ward.not.found}" )
    private  Long ward_id;
    @NotNull(message ="{prop.mstr.entry.type.not.found}" )
    private  String entry_type;
    @NotNull(message ="{prop.mstr.prop.type.id.not.found}" )
    private  Long property_type_id;
    @NotNull(message ="{prop.mstr.road.type.id.not.found}" )
    private  Long road_type_id;
    @NotNull(message ="{prop.mstr.area.id.found}" )
    private  Long area_id;
    private  String mohalla;
    private  String property_address;
    private  String city;
    private  String district;
    private  String vsrno;
    @NotNull(message ="{prop.mstr.pin.code.not.blank}" )
    private  String pincode;
    @NotNull(message ="{prop.mstr.build.up.area.not.blank}" )
    private  Integer totalbuilbup_area;
    private  String plot_no;
    private  String khata_no;  // need to rename (Khata_no) in DB (in DB Khata_no 'K' is in caps need to change in small 'k').
    @NotNull(message ="{prop.mstr.user.id.not.null}" )
    private  Long user_id;
    private  Integer status;
    private  String is_mobile_tower;
    private  String old_property;
    private  String widow_case;
    private  String phys_disable;
    private  String school_case;
    private  String complex_case;
    private  String bpl_category;
    private  String isdp_case;
    private  String builder_case;
    @NotNull(message ="{prop.mstr.entry.fin.year.not.blank}" )
    private  Long entry_fy_id;
    private  Long usage_type_id;
    private  String building_name;
    private  String application_no;
    private  String rain_harvest;
    private  String consumer_no;
    private  Long old_ward_id;
    private  Long update_ward_user_id;
    private  String penltydisc;
}
