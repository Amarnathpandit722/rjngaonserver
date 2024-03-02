package gov.municipal.suda.modules.property.dao.transaction;

import gov.municipal.suda.modules.property.model.transaction.ARVDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.SAFARVDetailsBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ARVDetailsDao extends JpaRepository<ARVDetailsBean, Long>  {

    @Query("select d from ARVDetailsBean d where d.prop_id=?1 and d.effect_year=?2 and d.status=1 order by d.stampdate desc")
    Optional<List<ARVDetailsBean>> getArvByPropAndYear(Long prop_id, String from_year);
}
