package gov.municipal.suda.usermanagement.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gov.municipal.suda.usermanagement.model.NewUser;



@Repository
public interface NewUserDao extends CrudRepository<NewUser, Long>{
	
	@Query("select u from NewUser u where u.userName =?1")
    Optional<NewUser> findByUserName(String user_name);
   
	@Query("select u from NewUser u where u.user_id =?1")
    Optional<NewUser> findByUserId(@Param("user_id") Long user_id);
    
	@Query("select max(u.user_id) from NewUser u")
    Long maxUserId();
   
	@Query("select max(u.user_id)+1 from NewUser u")
    Long newUserID();
    
	@Modifying
    @Query("update NewUser u set u.userPassword=?1 where u.id=?2")
    Integer changePassword(String password, Long id);
	

}
