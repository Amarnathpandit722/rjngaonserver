package gov.municipal.suda.modules.wastemgmt.dao.master;

import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerMasterBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ConsumerMasterDao extends JpaRepository<ConsumerMasterBean,Long> {
    @Query("select m from ConsumerMasterBean m where m.id in ?1 ")
    List<ConsumerMasterDao> fetchAllConsumerMasterRecordsById(Set<Long> Ids);
}
