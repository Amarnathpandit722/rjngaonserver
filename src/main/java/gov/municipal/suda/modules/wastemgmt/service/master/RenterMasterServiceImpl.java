package gov.municipal.suda.modules.wastemgmt.service.master;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerDetailsDao;
import gov.municipal.suda.modules.wastemgmt.dto.RenterEntryDTO;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerDetailsBean;
import gov.municipal.suda.modules.wastemgmt.service.transaction.RenterDemandService;
import gov.municipal.suda.modules.wastemgmt.service.transaction.RenterRateDetailService;
import gov.municipal.suda.util.Generate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@Slf4j
public class RenterMasterServiceImpl implements RenterMasterService{
    @Autowired
    ConsumerDetailsDao consumerDetailsDao;
    @Autowired
    RenterRateDetailService renterRateDetailService;
    @Autowired
    RenterDemandService renterDemandService;
    @Override
    public String addRenter(RenterEntryDTO renterEntryDTO) throws SQLException {
        String consumer_no = Generate.generateConsumerNo();
        ConsumerDetailsBean consumerDetailEntry=new ConsumerDetailsBean();
        consumerDetailEntry.setConsumer_mstr_id(renterEntryDTO.getConsumer_mstr_id());
        consumerDetailEntry.setConsumer_no(consumer_no);
        consumerDetailEntry.setConsumer_name(renterEntryDTO.getRenter_name());
        consumerDetailEntry.setMobile_no(renterEntryDTO.getRenter_mobile_no());
        consumerDetailEntry.setHouse_flat_no(renterEntryDTO.getHouse_flat_no());
        consumerDetailEntry.setGradian_name(renterEntryDTO.getRenter_gradian_name());
        consumerDetailEntry.setCreated_byid(renterEntryDTO.getCreated_byid());
        consumerDetailEntry.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
        consumerDetailEntry.setStatus(1);
        consumerDetailEntry.setRelation(renterEntryDTO.getRenter_relation());
        consumerDetailEntry.setConsumer_type("Renter");
        consumerDetailEntry.setConsumer_details_id(renterEntryDTO.getConsumer_dtl_id());
        try {
            ConsumerDetailsBean consumerDetView=consumerDetailsDao.save(consumerDetailEntry);
            renterRateDetailService.addRenterRate(consumerDetView.getId(),renterEntryDTO);
            renterDemandService.createRenterDemand(consumerDetView.getId(),renterEntryDTO);
        }
        catch (BadRequestException e) {

            return new BadRequestException("Data not save ").toString();
        }
        return "Success";
    }
}
