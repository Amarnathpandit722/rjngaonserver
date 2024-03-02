package gov.municipal.suda.usermanagement.service;

public interface LoginOtpValidatorService {
    boolean LoginOtpValidator(Long messageId, Long receivedOtp);
}
