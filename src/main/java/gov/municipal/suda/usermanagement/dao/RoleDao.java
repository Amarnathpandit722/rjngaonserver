package gov.municipal.suda.usermanagement.dao;

import gov.municipal.suda.usermanagement.model.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDao extends CrudRepository<Role, String> {
    @Modifying
    @Query(value = "update new_user_role w set w.role_id =?2 where w.user_id=?1 ",nativeQuery = true)
    void updateUserRole(@Param("user_id")String userId, @Param("role_id")String roleId);

    @Query("select r from Role r where r.roleName =?1")
    Optional<Role> findRoleName(@Param("roleName") String roleName);
}
