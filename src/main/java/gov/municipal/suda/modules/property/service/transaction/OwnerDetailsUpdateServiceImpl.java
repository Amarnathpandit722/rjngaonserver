package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.OwnerDetailsDao;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;
import gov.municipal.suda.modules.property.model.master.OwnerDetailsBean;
import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class OwnerDetailsUpdateServiceImpl implements OwnerDetailsUpdateService {

    @Autowired
    OwnerDetailsDao ownerDetailsDao;
    @Override
   // @Transactional(rollbackOn=BadRequestException.class)
    public String updateOwnerDetails(Long prop_id, SAFEntryRequestDto safEntryRequestDto)  throws Exception{
        OwnerDetailsBean ownerDetailsEntry = ownerDetailsDao.findOwnerDetailsForUpdate(prop_id).orElse(null);
        if(prop_id==null) {
            throw new BadRequestException("Property Id shouldn't be blank");
        }
        if(safEntryRequestDto.getOwner_title()==null) {
            throw new BadRequestException("Property owner Honorific shouldn't be blank");
        }
        if(safEntryRequestDto.getOwner_gender()==null) {
            throw new BadRequestException("Property owner Gender shouldn't be blank");
        }
        if(safEntryRequestDto.getOwner_relation()==null) {
            throw new BadRequestException("Property owner Relation shouldn't be blank");
        }
        ownerDetailsEntry.setProp_id(prop_id);
        //ownerDetailsEntry.setOwner_name(safEntryRequestDto.getProp_owner_name());
       ownerDetailsEntry.setTitle(safEntryRequestDto.getOwner_title());
       ownerDetailsEntry.setGuardian_name(safEntryRequestDto.getFather_name());
        ownerDetailsEntry.setGender(safEntryRequestDto.getOwner_gender());
        //ownerDetailsEntry.setOwner_address(safEntryRequestDto.getProp_address());
        ownerDetailsEntry.setPurchase_date(safEntryRequestDto.getPurchase_date());
        ownerDetailsEntry.setRelation(safEntryRequestDto.getOwner_relation());
        ownerDetailsEntry.setMobile_no(safEntryRequestDto.getMobile_no());
        ownerDetailsEntry.setAadhar_no(safEntryRequestDto.getAadhar());
        if (safEntryRequestDto.getPanno().isBlank() || safEntryRequestDto.getPanno().isEmpty()
                || safEntryRequestDto.getPanno().equals("")) {
            ownerDetailsEntry.setPan_no("N/A");
        } else if (!safEntryRequestDto.getPanno().isBlank() || !safEntryRequestDto.getPanno().isEmpty()
                || !safEntryRequestDto.getPanno().equals("")) {
            ownerDetailsEntry.setPan_no(safEntryRequestDto.getPanno());
        }
        if (safEntryRequestDto.getEmail().isEmpty() || safEntryRequestDto.getEmail().isBlank()
                || safEntryRequestDto.getEmail().equals("")) {
            ownerDetailsEntry.setEmail_id("N/A");
        } else if (!safEntryRequestDto.getEmail().isEmpty() || !safEntryRequestDto.getEmail().isBlank()
                || !safEntryRequestDto.getEmail().equals("")) {
            ownerDetailsEntry.setEmail_id(safEntryRequestDto.getEmail());
        }
        ownerDetailsEntry.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
        ownerDetailsEntry.setUser_id(safEntryRequestDto.getUser_id());
        ownerDetailsEntry.setStatus(1);
        if (!safEntryRequestDto.getRain_harvest().isBlank()
                || !safEntryRequestDto.getRain_harvest().isEmpty()
                || !safEntryRequestDto.getRain_harvest().equals("")) {
            ownerDetailsEntry.setRain_wtr_doc(safEntryRequestDto.getRain_water_docs());
            ownerDetailsEntry.setRain_rmv_date(Timestamp.valueOf(LocalDateTime.now()));
            ownerDetailsEntry.setRain_rmv_user(safEntryRequestDto.getUser_id());
        } else if (safEntryRequestDto.getRain_harvest().isBlank()
                || safEntryRequestDto.getRain_harvest().isEmpty()
                || safEntryRequestDto.getRain_harvest().equals("")
                || safEntryRequestDto.getRain_harvest().equals("No")
                || safEntryRequestDto.getRain_harvest().equals("N")) {
            ownerDetailsEntry.setRain_wtr_doc("N/A");
            ownerDetailsEntry.setRain_rmv_date(null);
            ownerDetailsEntry.setRain_rmv_user(0L);
        }

        Optional<OwnerDetailsBean> ownerGet = ownerDetailsDao.findById(prop_id);
        if (ownerGet.isPresent()) {

            if (safEntryRequestDto.getPanno().isBlank() || safEntryRequestDto.getPanno().isEmpty()
                    || safEntryRequestDto.getPanno().equals("")) {
                ownerDetailsEntry.setPan_no("N/A");
            } else if (!safEntryRequestDto.getPanno().isBlank() || !safEntryRequestDto.getPanno().isEmpty()
                    || !safEntryRequestDto.getPanno().equals("")) {
                ownerDetailsEntry.setPan_no(safEntryRequestDto.getPanno());
            }
            if (safEntryRequestDto.getEmail().isEmpty() || safEntryRequestDto.getEmail().isBlank()
                    || safEntryRequestDto.getEmail().equals("")) {
                ownerDetailsEntry.setEmail_id("N/A");
            } else if (!safEntryRequestDto.getEmail().isEmpty() || !safEntryRequestDto.getEmail().isBlank()
                    || !safEntryRequestDto.getEmail().equals("")) {
                ownerDetailsEntry.setEmail_id(safEntryRequestDto.getEmail());
            }
            ownerDetailsEntry.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
            ownerDetailsEntry.setUser_id(safEntryRequestDto.getUser_id());
            ownerDetailsEntry.setStatus(1);
            if (!safEntryRequestDto.getRain_harvest().isBlank()
                    || !safEntryRequestDto.getRain_harvest().isEmpty()
                    || !safEntryRequestDto.getRain_harvest().equals("")) {
                ownerDetailsEntry.setRain_wtr_doc(safEntryRequestDto.getRain_water_docs());
                ownerDetailsEntry.setRain_rmv_date(Timestamp.valueOf(LocalDateTime.now()));
                ownerDetailsEntry.setRain_rmv_user(safEntryRequestDto.getUser_id());
            } else if (safEntryRequestDto.getRain_harvest().isBlank()
                    || safEntryRequestDto.getRain_harvest().isEmpty()
                    || safEntryRequestDto.getRain_harvest().equals("")
                    || safEntryRequestDto.getRain_harvest().equals("No")
                    || safEntryRequestDto.getRain_harvest().equals("N")) {
                ownerDetailsEntry.setRain_wtr_doc("N/A");
                ownerDetailsEntry.setRain_rmv_date(null);
                ownerDetailsEntry.setRain_rmv_user(0L);
            }


            if (!safEntryRequestDto.getOwner_pic().isBlank()
                    || !safEntryRequestDto.getOwner_pic().isEmpty()
                    || !safEntryRequestDto.getOwner_pic().equals("")) {
                ownerDetailsEntry.setOwner_pic(safEntryRequestDto.getOwner_pic());
            } else if (safEntryRequestDto.getOwner_pic().isBlank()
                    || safEntryRequestDto.getOwner_pic().isEmpty()
                    || safEntryRequestDto.getOwner_pic().equals("")) {
                ownerDetailsEntry.setOwner_pic("N/A");
            }
            if(!safEntryRequestDto.getB1().isBlank()
                    || !safEntryRequestDto.getB1().isEmpty()
                    || !safEntryRequestDto.getB1().equals("")) {
                ownerDetailsEntry.setB1(safEntryRequestDto.getB1());
            }
            else if(safEntryRequestDto.getB1().isBlank()
                    || safEntryRequestDto.getB1().isEmpty()
                    || safEntryRequestDto.getB1().equals("")) {
            	ownerDetailsEntry.setB1("N/A");
            }
            
            if(!safEntryRequestDto.getB2().isBlank()
                    || !safEntryRequestDto.getB2().isEmpty()
                    || !safEntryRequestDto.getB2().equals("")) {
                ownerDetailsEntry.setB2(safEntryRequestDto.getB2());
            }
            else if(safEntryRequestDto.getB2().isBlank()
                    || safEntryRequestDto.getB2().isEmpty()
                    || safEntryRequestDto.getB2().equals("")) {
            	ownerDetailsEntry.setB2("N/A");
            }
            
            if(!safEntryRequestDto.getDiversion().isBlank()
                    || !safEntryRequestDto.getDiversion().isEmpty()
                    || !safEntryRequestDto.getDiversion().equals("")) {
                ownerDetailsEntry.setDiversion(safEntryRequestDto.getDiversion());
            }
            else if(safEntryRequestDto.getDiversion().isBlank()
                    || safEntryRequestDto.getDiversion().isEmpty()
                    || safEntryRequestDto.getDiversion().equals("")) {
            	ownerDetailsEntry.setDiversion("N/A");
            }
            if(!safEntryRequestDto.getKharidi_Bikri().isBlank()
                    || !safEntryRequestDto.getKharidi_Bikri().isEmpty()
                    || !safEntryRequestDto.getKharidi_Bikri().equals("")) {
                ownerDetailsEntry.setKharidi_Bikri(safEntryRequestDto.getKharidi_Bikri());
            }
            else if(safEntryRequestDto.getKharidi_Bikri().isBlank()
                    || safEntryRequestDto.getKharidi_Bikri().isEmpty()
                    || safEntryRequestDto.getKharidi_Bikri().equals("")) {
            	ownerDetailsEntry.setKharidi_Bikri("N/A");
            }    
            
            
            
            
            

            ownerDetailsEntry.setType_of_change("N/A");


        ownerDetailsEntry.setType_of_change("Re Assessment");

            try {
                OwnerDetailsBean resultOfOwnerDetails = ownerDetailsDao.save(ownerDetailsEntry);
                log.info("tbl_prop_owner_details table saved record {}", resultOfOwnerDetails);


            } catch (BadRequestException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return "Success";
    }
    }
