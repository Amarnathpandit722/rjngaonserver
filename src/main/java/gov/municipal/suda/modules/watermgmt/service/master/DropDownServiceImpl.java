package gov.municipal.suda.modules.watermgmt.service.master;

import gov.municipal.suda.modules.watermgmt.dao.master.*;
import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterAllDropDownDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DropDownServiceImpl implements DropDownService{
    @Autowired
    BPLAPLDetailsDao aplBplDao;
    @Autowired
    ConnectionTypeMasterDao connectionTypeDao;
    @Autowired
    ExtraRoomChargeDao extraRoomChargeDao;
    @Autowired
    PropertyTypeWaterDao propertyTypeDao;
    @Autowired
    WaterMeterRateChartDao waterMeterRateDao;
    @Autowired
    WaterModeOfPaymentDao modeOfPaymentDao;
    @Autowired
    WaterRangeMasterDao rangeDao;
    @Autowired
    WaterRateMasterDao rateDao;

    @Override
    public Optional<WaterAllDropDownDto> findAllDropDown() {
        WaterAllDropDownDto waterAllDropDown = new WaterAllDropDownDto();
        waterAllDropDown.setAplBpl(aplBplDao.findAll().stream()
                .filter(z -> z.getStatus()==1).collect(Collectors.toList()));
        waterAllDropDown.setConnectionType(connectionTypeDao.findAll().stream()
                .filter(z-> z.getStatus()==1).collect(Collectors.toList()));
        waterAllDropDown.setModeOfPayment(modeOfPaymentDao.findAll().stream()
                .filter(z->z.getStatus()==1).collect(Collectors.toList()));
        waterAllDropDown.setPropertyType(propertyTypeDao.findAll().stream().filter(z-> z.getStatus()==1).collect(Collectors.toList()));
        waterAllDropDown.setRange(rangeDao.findAll().stream()
                .filter(v->v.getStatus()==1).collect(Collectors.toList()));
        waterAllDropDown.setRate(rateDao.findAll().stream().filter(v-> v.getStatus()==1).collect(Collectors.toList()));
        return Optional.of(waterAllDropDown);
    }
}
