package gov.municipal.suda.usermanagement.service;

import gov.municipal.suda.usermanagement.dao.LoginOtpDao;
import gov.municipal.suda.usermanagement.model.OTPBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Slf4j
public class LoginOtpValidatorServiceImpl implements LoginOtpValidatorService{
    @Autowired
    LoginOtpDao loginOtpDao;
    @Override
    public boolean LoginOtpValidator(Long messageId, Long receivedOtp) {
        boolean isValidated=false;
        Optional<OTPBean> otpCheck = loginOtpDao.findById(messageId);
        if(otpCheck.isPresent()) {
          if(receivedOtp.longValue()==otpCheck.get().getOtp().longValue()) {
              isValidated=true;
          }
        }
        return isValidated;
    }
}
