package gov.municipal.suda.modules.property.dao.master;
import gov.municipal.suda.modules.property.model.master.ArvRateBean;
import gov.municipal.suda.modules.property.model.master.VacantLandRateBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VacantLandRateDao  extends JpaRepository<VacantLandRateBean, Long> {

    @Query("select m from VacantLandRateBean m where m.zone_id=?1 and m.road_id=?2  and m.uses_type_id=?3 and m.doe=?4 and status=1 order by m.doe asc")
    Optional<List<VacantLandRateBean>> findVacantRateByTypes(Long zone_id, Long road_id, Long uses_type_id, String doe);

}
