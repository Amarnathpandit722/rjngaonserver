package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.OnTimeTaxRebateBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OnTimeTaxRebateDao extends JpaRepository<OnTimeTaxRebateBean, Long> {
    @Query("select r from OnTimeTaxRebateBean r where TO_DATE(?1,'DD-MM-YYYY') between TO_DATE(r.from_date,'DD-MM-YYYY') and TO_DATE(r.to_date,'DD-MM-YYYY') and r.effective_year=?2")
    Optional<OnTimeTaxRebateBean> findOnTimeTaxRebate(String currentDate, String currentEffectiveYear);
}
