package gov.municipal.suda.modules.property.dao.master;

import gov.municipal.suda.modules.property.model.master.FloorBean;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//New Changes
@Repository
public interface FloorDao extends JpaRepository<FloorBean, Long> {
	
	
	@Query("SELECT fb FROM FloorBean fb WHERE fb.id = :floor_id")
    FloorBean findById(@Param("floor_id") long floorId);
	
	
	
}
