package gov.municipal.suda.usermanagement.dao;

import gov.municipal.suda.usermanagement.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserDao extends CrudRepository<User, Long>{
    @Query("select u from User u where u.userName =?1")
    Optional<User> findByUserName(String user_name);
    @Query("select u from User u where u.user_id =?1")
    Optional<User> findByUserId(@Param("user_id") Long user_id);
    @Query("select max(u.user_id) from User u")
    Long maxUserId();
    @Query("select max(u.user_id)+1 from User u")
    Long newUserID();
    @Modifying
    @Query("update User u set u.userPassword=?1 where u.id=?2")
    Integer changePassword(String password, Long id);
    @Query("select u from User u where u.email =?1 ")
    User findByEmail(String email);
    
}
