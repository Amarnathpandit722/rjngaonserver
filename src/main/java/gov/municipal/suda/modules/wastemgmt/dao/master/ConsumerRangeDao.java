package gov.municipal.suda.modules.wastemgmt.dao.master;

import gov.municipal.suda.modules.wastemgmt.dto.RangeAndRateDto;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRangeMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public interface ConsumerRangeDao extends JpaRepository<ConsumerRangeMasterBean,Long> {


}
