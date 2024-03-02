package gov.municipal.suda.usermanagement.dao;

import gov.municipal.suda.usermanagement.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DesignationDao extends JpaRepository<Designation, Long> {

      @Query("select d.short_name from Designation d where d.designation =?1")
      String findShortNameByDesignation(String designation);
      
      
      
      
}
