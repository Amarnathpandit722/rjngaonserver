package gov.municipal.suda.modules.watermgmt.dao.master;

import gov.municipal.suda.modules.wastemgmt.dto.WaterRangeDTO;
import gov.municipal.suda.modules.watermgmt.model.master.WaterRangeMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WaterRangeMasterDao extends JpaRepository<WaterRangeMasterBean, Long> {

    @Query("select m from WaterRangeMasterBean m where m.prop_type_id=?1 and ?2 between m.room_table_from and m.room_table_upto and status=1")
    Optional<WaterRangeMasterBean> getRange(Long propertyTypeId, Long noOfRoom);
    @Query("select new gov.municipal.suda.modules.wastemgmt.dto.WaterRangeDTO(r.id,r.prop_type_id,CONCAT(r.room_table_from,' TO ',r.room_table_upto) AS ws_range,r.status,r.date_of_effect) from WaterRangeMasterBean r")
    List<WaterRangeDTO> findAllRange();

}
