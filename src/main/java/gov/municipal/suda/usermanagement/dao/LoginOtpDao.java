package gov.municipal.suda.usermanagement.dao;

import gov.municipal.suda.usermanagement.model.OTPBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginOtpDao extends JpaRepository<OTPBean, Long> {

}
