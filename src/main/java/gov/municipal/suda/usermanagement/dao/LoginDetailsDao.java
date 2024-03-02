package gov.municipal.suda.usermanagement.dao;


import gov.municipal.suda.usermanagement.model.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface LoginDetailsDao extends JpaRepository<LoginDetails, Long> {
}
