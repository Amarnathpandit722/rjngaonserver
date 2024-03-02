package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.OldBuildingRatePercentageBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OldBuildingRatePercentageDao extends JpaRepository<OldBuildingRatePercentageBean, Long> {
    @Query("select o from OldBuildingRatePercentageBean o where  ?1 between o.count_year_from and o.count_year_to")
    Optional<OldBuildingRatePercentageBean> getOldBuildingRatePercentage(Integer ageCount);
}
