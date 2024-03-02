package gov.municipal.suda.modules.property.model.master;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SAFAllDropDownBean {
    private List<ZoneBean> zone;
    private List<WardBean> ward;
    private List<FloorBean> floor;
    private List<BuildingTypeBean> building_type;
    private List<PropertyTypeBean> property_type;
    private List<OccupationTypeBean> occupation_type;
    private List<UsesTypeBean> uses_type;
    private List<RoadTypeBean> roadType;
    private List<EffectYearOfRateChangeBean> arvRateEffectiveYear;
    private List<EntryTypeBean> entry_type;
    private List<FinYearBean> financial_year;




}
