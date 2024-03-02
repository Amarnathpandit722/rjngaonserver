package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dao.transaction.SAFDao;
import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class SAFServiceImpl implements SAFService{

    @Autowired
    private SAFDao safDao;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<PropertyMasterBean> findAllByPage(Pageable page) {


        //List<PropertyMasterBean> getResult=safDao.findAllByPropertyNo(page);
       // log.info("data is ",getResult);
        //Page<PropertyMasterBean> response = safDao.findAll(page);
        //response= safDao.findAll(page);
        //getResult= (List<PropertyMasterBean>) safDao.findAll(page);
        //if(!response.isEmpty()) {
           // getResult = response.getContent();
        //}
        //else {
//        List<Object> errors=null;
//            errors.add(new RecordNotFoundException(messageSource.getMessage("table.empty",
//                new Object[] {"tbl_property_mstr"}, LocaleContextHolder.getLocale())));
//            return Collections.singletonList((PropertyMasterBean) errors);

          //log.info("else part");
            //return null;
       // }
        return safDao.findAllByPropertyNo(page);
    }
}
