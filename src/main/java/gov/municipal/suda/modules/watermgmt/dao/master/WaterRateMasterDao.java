package gov.municipal.suda.modules.watermgmt.dao.master;

import gov.municipal.suda.modules.watermgmt.model.master.WaterRateMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WaterRateMasterDao extends JpaRepository<WaterRateMasterBean, Long> {
    @Query("SELECT r FROM WaterRateMasterBean r where r.prop_type_id=?1 and r.conn_type_id=?2 and r.range_mstr_id=?3 and status=1")
    Optional<WaterRateMasterBean> getRateWithRange(Long prop_type_id, Long conn_type_id, Long range_id);
    @Query("SELECT r FROM WaterRateMasterBean r where r.prop_type_id=?1 and r.conn_type_id=?2  and status=1")
    Optional<WaterRateMasterBean> getRateWithOutRange(Long prop_type_id, Long conn_type_id);

    @Query("SELECT r FROM WaterRateMasterBean r where r.prop_type_id=?1 and r.conn_type_id=?2 and r.range_mstr_id=?3 and r.date_of_effect=?4 and r.status=1")
   Optional<WaterRateMasterBean> getRateWithRangeAndEffectDate(Long prop_type_id, Long conn_type_id, Long range_id,String doe);
}
