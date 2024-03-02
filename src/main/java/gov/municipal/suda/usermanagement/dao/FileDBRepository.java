package gov.municipal.suda.usermanagement.dao;

import gov.municipal.suda.usermanagement.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

}
