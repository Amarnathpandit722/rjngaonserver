package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.TenantedRateBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TenantedRateDao extends JpaRepository<TenantedRateBean, Long> {
    @Query("select r from TenantedRateBean r where  ?1 between r.range_from and r.range_to and r.effective_year=?2 and r.status=1")
    Optional<TenantedRateBean> getTenantedRate(Long totalEarningPerYear, String effective_year);
}
