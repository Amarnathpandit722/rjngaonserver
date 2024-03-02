package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.model.master.EduAndCompositeRateBean;
import gov.municipal.suda.modules.property.model.master.VacantLandRateBean;
import gov.municipal.suda.modules.property.model.master.ArvRateBean;
import gov.municipal.suda.modules.property.model.master.EffectYearOfRateChangeBean;

import java.util.List;
import java.util.Optional;

public interface AllTypesOfRateService {
    Optional<List<VacantLandRateBean>> findVacantLandRate();
    Optional<List<EduAndCompositeRateBean>> getAllEduAndCompositeRate();
    Optional<ArvRateBean> findArvRateByUsesTypeAndFYYear(Long zone_id, Long road_id, String const_type, Long uses_type_id, String fy_year) ;
    public Optional<List<EffectYearOfRateChangeBean>> findAllEffectYearCharge();

    //    Optional<List<EffectYearOfRateChangeBean>> findAllEffectYearCharge();
//    List<ArvRateEffectiveDateDTO> findEffectiveDate();
}
