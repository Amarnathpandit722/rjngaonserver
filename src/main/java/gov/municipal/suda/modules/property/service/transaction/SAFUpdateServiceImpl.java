package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.exception.RecordNotFoundException;
import gov.municipal.suda.message.ResponseMessage;
import gov.municipal.suda.modules.property.dao.master.PropertyMasterDao;
import gov.municipal.suda.modules.property.dto.SAFARVDetailsDTO;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;
import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Service
@Slf4j

public class SAFUpdateServiceImpl implements SAFUpdateService {
    @Autowired
    PropertyMasterDao propertyMasterDao; // table tbl_property_mstr
    @Autowired
    MessageSource messageSource;
    @Autowired
    OwnerDetailsEntryService ownerDetailsEntryService;
    @Autowired
    OwnerDetailsUpdateService ownerDetailsUpdateService;

    @Autowired
    SAFARVUpdateService safarvUpdateService;

    @Autowired
    SAFARVDetailsService safarvDetailsService;

    @Override
    //@Transactional(rollbackOn= BadRequestException.class)
    public String SAFUpdate(SAFEntryRequestDto safEntryRequestDto, Long id) throws Exception {
        PropertyMasterBean property_entry = propertyMasterDao.findById(id).orElse(null);
        if (property_entry != null) {
            property_entry.setWard_id(safEntryRequestDto.getWard_id());

            if (property_entry.getEntry_type().equals("New Assessment")) {
                property_entry.setEntry_type("Re Assessment");
                safEntryRequestDto.setEntry_type_id(2L);
            }

            property_entry.setProperty_no(safEntryRequestDto.getProp_id()); // get manual prop id from SUDA
            property_entry.setProperty_type_id(safEntryRequestDto.getProperty_type_id());
            property_entry.setRoad_type_id(safEntryRequestDto.getRoad_type_id());
            property_entry.setArea_id(safEntryRequestDto.getArea_id());
            BigDecimal totalBuildUpArea = new BigDecimal(0.00);
           // BigDecimal previousTotalBuildUpArea=property_entry.getTotalbuilbup_area();

            for (SAFARVDetailsDTO floorDetails : safEntryRequestDto.getFloor_details()) {
                totalBuildUpArea = totalBuildUpArea.add(new BigDecimal(floorDetails.getBuilt_up_area()));
            }

            if (safEntryRequestDto.getProp_address().isEmpty()  // Property Address
                    || safEntryRequestDto.getProp_address().isBlank()) {
                throw new RecordNotFoundException(messageSource.getMessage("prop.mstr.prop.address.not.blank",
                        new Object[]{}, LocaleContextHolder.getLocale()));
            } else {
                if (!safEntryRequestDto.getProp_address().isEmpty()
                        || !safEntryRequestDto.getProp_address().isBlank()) {
                    property_entry.setProperty_address(safEntryRequestDto.getProp_address());
                }
            }

            property_entry.setCity(safEntryRequestDto.getCity());
            property_entry.setDistrict(safEntryRequestDto.getDistrict());

            if (safEntryRequestDto.getMohalla().equals("") // Mohalla entry
                    || safEntryRequestDto.getMohalla().isEmpty()
                    || safEntryRequestDto.getMohalla().isBlank()) {
                property_entry.setMohalla("N/A");
            } else if (!safEntryRequestDto.getMohalla().equals("")
                    || !safEntryRequestDto.getMohalla().isEmpty()
                    || !safEntryRequestDto.getMohalla().isBlank()) {
                property_entry.setMohalla(safEntryRequestDto.getMohalla());
            }

            if (safEntryRequestDto.getVsrno().equals("")
                    || safEntryRequestDto.getVsrno().isEmpty() ||
                    safEntryRequestDto.getVsrno().isBlank()) {              // check Vsr no field
                property_entry.setVsrno("N/A");
            } else if (!safEntryRequestDto.getVsrno().equals("")
                    || !safEntryRequestDto.getVsrno().isEmpty() ||
                    !safEntryRequestDto.getVsrno().isBlank()) {
                property_entry.setVsrno(safEntryRequestDto.getVsrno());
            }

            if (safEntryRequestDto.getPin().isBlank()
                    || safEntryRequestDto.getPin().isEmpty()) {
                property_entry.setPincode("N/A");
            } else if (safEntryRequestDto.getPin().length() != 0) {
                property_entry.setPincode(safEntryRequestDto.getPin());
            }


           // previousTotalBuildUpArea=previousTotalBuildUpArea.add(totalBuildUpArea);
                property_entry.setTotalbuilbup_area(totalBuildUpArea);
                property_entry.setPlot_area(safEntryRequestDto.getPlot_area()); // plotarea

            if (safEntryRequestDto.getPlot_no().isEmpty()
                    || safEntryRequestDto.getPlot_no().equals("")
                    || safEntryRequestDto.getPlot_no().isBlank()) {   // check Plot no field
                property_entry.setPlot_no("N/A");
            } else if (!safEntryRequestDto.getPlot_no().isEmpty()
                    || !safEntryRequestDto.getPlot_no().equals("")
                    || !safEntryRequestDto.getPlot_no().isBlank()) {
                property_entry.setPlot_no(safEntryRequestDto.getPlot_no());
            }

            if (safEntryRequestDto.getKhata_no().isEmpty()
                    || safEntryRequestDto.getKhata_no().equals("")
                    || safEntryRequestDto.getKhata_no().isBlank()) {   // check Khata no field
                property_entry.setKhata_no("N/A");
            } else if (!safEntryRequestDto.getKhata_no().isEmpty()
                    || !safEntryRequestDto.getKhata_no().equals("")
                    || !safEntryRequestDto.getKhata_no().isBlank()) {
                property_entry.setKhata_no(safEntryRequestDto.getKhata_no());
            }

            property_entry.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
            property_entry.setUser_id(safEntryRequestDto.getUser_id());
            property_entry.setStatus(1);
            if (safEntryRequestDto.getIsMobileTower().isEmpty()
                    || safEntryRequestDto.getIsMobileTower().isBlank()
                    || safEntryRequestDto.getIsMobileTower().equals("")) {
                property_entry.setIs_mobile_tower("No");
            } else if (!safEntryRequestDto.getIsMobileTower().isEmpty()
                    || !safEntryRequestDto.getIsMobileTower().isBlank()
                    || !safEntryRequestDto.getIsMobileTower().equals("")) {
                property_entry.setIs_mobile_tower(safEntryRequestDto.getIsMobileTower());
            }

            if (safEntryRequestDto.getProp_age_count() > 19) { // If Age of property > 20 years than Yes otherwise No
                property_entry.setOld_property("Yes");
            } else if (safEntryRequestDto.getProp_age_count() < 20) {
                property_entry.setOld_property("No");
            } else if (safEntryRequestDto.getProp_age_count() == null) {
                property_entry.setOld_property("N/A");
            }


            property_entry.setWidow_case(safEntryRequestDto.getIs_widow());

            if (safEntryRequestDto.getIs_handicapped().equals("No")) {
                property_entry.setPhys_disable(safEntryRequestDto.getIs_handicapped());
            } else if (safEntryRequestDto.getIs_handicapped().equals("Yes")) {
                property_entry.setPhys_disable(safEntryRequestDto.getIs_handicapped());
            }

            property_entry.setSchool_case(safEntryRequestDto.getIs_school()); // Is school
            property_entry.setComplex_case(safEntryRequestDto.getIs_complex()); // is complex
            property_entry.setBpl_category("No");
            property_entry.setIsdp_case(safEntryRequestDto.getIs_isdp());  // is ISDP
            property_entry.setBuilder_case(safEntryRequestDto.getIs_school()); // is Builder
            property_entry.setEntry_fy_id(safEntryRequestDto.getFy_id());
            //property_entry.setUsage_type_id(safEntryRequestDto.getUses_type_id());
            property_entry.setBuilding_name("N/A");
            property_entry.setApplication_no("N/A");
            property_entry.setApproval_status(0);
            property_entry.setEntry_date(Timestamp.valueOf(LocalDateTime.now()));

            if (safEntryRequestDto.getRain_harvest().isBlank()
                    || safEntryRequestDto.getRain_harvest().isEmpty()
                    || safEntryRequestDto.getRain_harvest().equals("")) { // Rain Harvest Flag
                property_entry.setRain_harvest("No");
            } else if (!safEntryRequestDto.getRain_harvest().isBlank()
                    || !safEntryRequestDto.getRain_harvest().isEmpty()
                    || !safEntryRequestDto.getRain_harvest().equals("")) {
                property_entry.setRain_harvest(safEntryRequestDto.getRain_harvest());
            }
            property_entry.setApproval_user(0L);
            property_entry.setConsumer_no("N/A");

            if (safEntryRequestDto.getEntry_type_id() == 4) {
                property_entry.setOld_property(safEntryRequestDto.getOld_ward_id().toString());
                property_entry.setUpdate_ward_user_id(safEntryRequestDto.getUser_id());
            } else if (safEntryRequestDto.getEntry_type_id() != 4) {
                property_entry.setOld_ward_id(0L);
                property_entry.setUpdate_ward_user_id(0L);
            }
            property_entry.setPenltydisc("N/A");
            propertyMasterDao.save(property_entry);
            log.info("SAFUpdateServiceImple -------- Line 190 ----- {}",property_entry);
            ownerDetailsUpdateService.updateOwnerDetails(id, safEntryRequestDto);
            log.info("OwnerDetials UpdateService ------ Line 194---- {}",ownerDetailsEntryService.toString());

            safarvUpdateService.updateSAFARV(id, safEntryRequestDto);
            //safarvDetailsService.createSAFARV(id, safEntryRequestDto);



        }
        else if(property_entry==null) {

            return "Failed, Data Not Found";
        }
        return "Update Successfully";
    }

}
