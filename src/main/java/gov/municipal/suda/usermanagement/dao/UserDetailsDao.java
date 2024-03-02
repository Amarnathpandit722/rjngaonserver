package gov.municipal.suda.usermanagement.dao;


import gov.municipal.suda.usermanagement.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserDetailsDao extends JpaRepository<UserDetails, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE UserDetails e SET e.is_active = 'FALSE' WHERE e.user_id = ?1")
    void deactivateUser(Long user_id);
}
