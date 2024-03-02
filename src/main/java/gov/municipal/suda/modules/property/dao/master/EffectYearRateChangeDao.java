package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.EffectYearOfRateChangeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EffectYearRateChangeDao extends JpaRepository<EffectYearOfRateChangeBean, Long> {
    @Query("select e from EffectYearOfRateChangeBean e where e.effect_year=?1")
    EffectYearOfRateChangeBean findEffectDate(String financial_Year);

  @Query("select e from EffectYearOfRateChangeBean e where e.effect_year between ?1 and ?2 order by e.effect_year asc")
    Optional<List<EffectYearOfRateChangeBean>> findEffectYearBetween(String fromDate, String toDate);
}
