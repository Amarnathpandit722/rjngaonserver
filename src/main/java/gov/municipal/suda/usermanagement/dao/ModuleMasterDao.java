package gov.municipal.suda.usermanagement.dao;


import gov.municipal.suda.usermanagement.model.ModuleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModuleMasterDao extends JpaRepository<ModuleMaster, Long> {

    @Query("select m from ModuleMaster m where m.module_name=?1")
    ModuleMaster findByModuleName(String module_name);

}
