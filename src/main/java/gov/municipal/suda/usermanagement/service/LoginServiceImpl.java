package gov.municipal.suda.usermanagement.service;

import gov.municipal.suda.exception.RecordNotFoundException;
import gov.municipal.suda.usermanagement.dao.LoginDetailsDao;
import gov.municipal.suda.usermanagement.dao.ModuleMasterDao;
import gov.municipal.suda.usermanagement.dao.NewUserDao;
import gov.municipal.suda.usermanagement.dao.UserDao;
import gov.municipal.suda.usermanagement.model.LoginDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService{

    @Autowired
    private LoginDetailsDao loginDetailsDao;
    @Autowired
    private ModuleMasterDao moduleMasterDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageSource messageSource;
    @Override
    public void createLoginData(LoginDetails loginDetails) {

        Long moduleId= moduleMasterDao.findById(loginDetails.getModule_id()).get().getId();
        log.info("Module Id {} ",moduleId);
        Long userId= userDao.findByUserId(loginDetails.getUser_id()).get().getUser_id();
        log.info("User Id {} ",userId);
        if(null==userId || null==moduleId) {

            throw new RecordNotFoundException(messageSource.getMessage("invalid.request.body",
                    new Object[] {}, LocaleContextHolder.getLocale()));
        }

        loginDetails.setUser_id(userId);
        loginDetails.setModule_id(moduleId);
        loginDetails.setLogin_time(LocalDateTime.now());

        loginDetailsDao.save(loginDetails);
    }
}
