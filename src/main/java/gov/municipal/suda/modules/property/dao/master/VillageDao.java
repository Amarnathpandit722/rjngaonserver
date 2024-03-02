package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.VillageBean;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VillageDao extends JpaRepository<VillageBean, Long> {

}
