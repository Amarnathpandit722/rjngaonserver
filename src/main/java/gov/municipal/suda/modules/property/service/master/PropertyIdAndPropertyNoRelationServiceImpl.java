package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.exception.AlreadyExistException;
import gov.municipal.suda.modules.property.dao.master.EntryTypeDao;
import gov.municipal.suda.modules.property.dao.master.PropertyIdAndPropertyNoRelationDao;
import gov.municipal.suda.modules.property.model.master.PropertyIdAndPropertyNoRelationBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;

@Service
@Slf4j
public class PropertyIdAndPropertyNoRelationServiceImpl implements PropertyIdAndPropertyNoRelationService{
    @Autowired
    private EntryTypeDao entryTypeDao;
    @Autowired
    PropertyIdAndPropertyNoRelationDao propertyIdAndPropertyNoRelationDao;
    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional
    public PropertyIdAndPropertyNoRelationBean create(PropertyIdAndPropertyNoRelationBean propertyIdAndPropertyNoRelationBean) {
        PropertyIdAndPropertyNoRelationBean entryBean=new PropertyIdAndPropertyNoRelationBean();
        Long count = propertyIdAndPropertyNoRelationDao.findRecordCountByPropertyId(propertyIdAndPropertyNoRelationBean.getProp_id());

        if(count > 0) {
                    throw new AlreadyExistException(messageSource.getMessage("prop.mstr.prop.id.found",
                            new Object[] {propertyIdAndPropertyNoRelationBean.getProp_id()}, LocaleContextHolder.getLocale()));
        }
        //entryBean.setHolding_no();  //pending for create new holding no from the programme, need to ask Subhamay
        entryBean.setWard_id(propertyIdAndPropertyNoRelationBean.getWard_id());
        entryBean.setProp_id(propertyIdAndPropertyNoRelationBean.getProp_id());
        entryBean.setModule(propertyIdAndPropertyNoRelationBean.getModule());
        entryBean.setUser_id(propertyIdAndPropertyNoRelationBean.getUser_id());
        entryBean.setStampdate((Timestamp) new Date());
        entryBean.setRemarks(propertyIdAndPropertyNoRelationBean.getRemarks());
        entryBean.setStatus(1);

        return propertyIdAndPropertyNoRelationDao.save(entryBean);
    }
}
