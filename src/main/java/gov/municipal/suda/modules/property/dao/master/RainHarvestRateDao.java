package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.RainHarvestRateMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface RainHarvestRateDao extends JpaRepository<RainHarvestRateMasterBean, Long> {

    @Query("select w from RainHarvestRateMasterBean w where ?1 between w.buildup_area_from and w.buildup_area_upto and status=1")
    Optional<RainHarvestRateMasterBean> getRainHarvestCharge(BigDecimal buildupArea);



}
