package gov.municipal.suda.modules.watermgmt.dao.master;

import gov.municipal.suda.modules.watermgmt.model.master.ExtraRoomChargeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExtraRoomChargeDao extends JpaRepository<ExtraRoomChargeBean, Long> {

    @Query("SELECT e FROM ExtraRoomChargeBean e where e.prop_type_id=3")
    ExtraRoomChargeBean getExtraRoomChargeByFixedPropId();
}
