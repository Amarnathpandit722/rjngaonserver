package gov.municipal.suda.usermanagement.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gov.municipal.suda.usermanagement.model.NewUserDetails;

@Repository
public interface NewUserDetailsDao extends CrudRepository<NewUserDetails, Long>{
	
	 @Modifying
	    @Transactional
	    @Query("UPDATE NewUserDetails e SET e.is_active = 'FALSE' WHERE e.user_id = ?1")
	    void deactivateUser(Long user_id);

}
