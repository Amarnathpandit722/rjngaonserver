package gov.municipal.suda.modules.property.service.transaction;
import gov.municipal.suda.exception.AlreadyExistException;
import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.exception.RecordNotFoundException;
import gov.municipal.suda.modules.property.dao.master.*;
import gov.municipal.suda.modules.property.dto.SAFARVDetailsDTO;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;
import gov.municipal.suda.modules.property.model.master.PropertyDocDtlBean;

import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import gov.municipal.suda.usermanagement.model.FileDB;
import gov.municipal.suda.util.enumtype.BuildingType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.beans.factory.annotation.Value;

import javax.transaction.Transactional;

import java.io.File;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SAFEntryServiceImpl implements SAFEntryService {
    @Autowired
    PropertyMasterDao propertyMasterDao; // table tbl_property_mstr
    @Autowired
     MessageSource messageSource;
    @Autowired
   OwnerDetailsEntryServiceImpl ownerDetailsEntryService;

    @Autowired
    SAFARVDetailsServiceImpl safarvDetailsService;


    @Autowired
    PropertyDocDtlDao propertyDocDtlDao;
        @Override
        //@Transactional(rollbackOn=BadRequestException.class)
      public String create(SAFEntryRequestDto safEntryRequestDto) throws BadRequestException {
            //String newPropId="";
            log.info("All Request Json {} ", safEntryRequestDto.getFloor_details());
            if (safEntryRequestDto != null) {
                BigDecimal totalBuiltupArea = new BigDecimal(0);
                for (SAFARVDetailsDTO countBuiltUpArea : safEntryRequestDto.getFloor_details()) {
                    totalBuiltupArea = totalBuiltupArea.add(new BigDecimal(countBuiltUpArea.getBuilt_up_area()));
                }

                if (safEntryRequestDto.getEntry_type_id() == 1) { // New Assessment

                    Optional<String> property_no = propertyMasterDao.findPropertyNo(safEntryRequestDto.getProp_id());
                    if (property_no.isPresent()) {
                        throw new AlreadyExistException(messageSource.getMessage("prop.mstr.prop.no.found",
                                new Object[]{safEntryRequestDto.getProp_id()}, LocaleContextHolder.getLocale()));
                    }

                    PropertyMasterBean property_entry = new PropertyMasterBean();
                    property_entry.setWard_id(safEntryRequestDto.getWard_id());
                    property_entry.setEntry_type(safEntryRequestDto.getEntry_type_name());
                    property_entry.setProperty_no(safEntryRequestDto.getProp_id()); // get manual prop id from SUDA
                    property_entry.setProperty_type_id(safEntryRequestDto.getProperty_type_id());
                    property_entry.setRoad_type_id(safEntryRequestDto.getRoad_type_id());
                    property_entry.setArea_id(safEntryRequestDto.getArea_id());

                    if (safEntryRequestDto.getProp_address().isEmpty()  // Property Address
                            || safEntryRequestDto.getProp_address().isBlank()) {
                        throw new RecordNotFoundException(messageSource.getMessage("prop.mstr.prop.address.not.blank",
                                new Object[]{}, LocaleContextHolder.getLocale()));
                    } else if (!safEntryRequestDto.getProp_address().isEmpty()
                            || !safEntryRequestDto.getProp_address().isBlank()) {

                        property_entry.setProperty_address(safEntryRequestDto.getProp_address());
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
                    } else if (!safEntryRequestDto.getPin().isEmpty()
                            || !safEntryRequestDto.getPin().isBlank()) {
                        property_entry.setPincode(safEntryRequestDto.getPin());
                    }

                    property_entry.setTotalbuilbup_area(totalBuiltupArea); // total buildup area
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

                    if (safEntryRequestDto.getProp_age_count() > 25) { // If Age of property > 25 years than Yes otherwise No
                        property_entry.setOld_property("Yes");
                        												//Entry BuildingAge
                    } else if (safEntryRequestDto.getProp_age_count() < 25) {
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
                    if (safEntryRequestDto.getFloor_details().size() > 0) { //uses type id
                        property_entry.setUsage_type_id(safEntryRequestDto.getFloor_details().get(0).getUsage_type_id()); // I think this field need to be present under the floor details field
                    } else if (safEntryRequestDto.getFloor_details().size() == 0) {
                        property_entry.setUsage_type_id(0L);
                    }
                    property_entry.setSchool_case(safEntryRequestDto.getIs_school()); // Is school
                    property_entry.setComplex_case(safEntryRequestDto.getIs_complex()); // is complex
                    property_entry.setBpl_category("No"); // This field not present into the Saf Entry DTO because no confirmation from the client, but this field is present tbl_property_master
                    property_entry.setIsdp_case(safEntryRequestDto.getIs_isdp());  // is ISDP
                    property_entry.setBuilder_case(safEntryRequestDto.getIs_school()); // is Builder
                    property_entry.setEntry_fy_id(safEntryRequestDto.getFy_id());
                    property_entry.setBuilding_name("N/A"); // need to ask
                    property_entry.setApplication_no("N/A"); // need to ask
                    property_entry.setApproval_status(1); //default status is true means value is 1
                    property_entry.setEntry_date(Timestamp.valueOf(LocalDateTime.now()));
                    if(safEntryRequestDto.getBuildingType().equals("YES")) {
                    	property_entry.setBuildingtype(BuildingType.IS_GOVERMENT_TYPE);
                    }else {
                    	property_entry.setBuildingtype(BuildingType.IS_PRIVATE_TYPE);
                    }
                    
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
                  //  property_entry.setGovTap_Conn(safEntryRequestDto.getGovTap_Conn());

                    if (safEntryRequestDto.getEntry_type_id() == 4) {
                        property_entry.setOld_property(safEntryRequestDto.getOld_ward_id().toString());
                        property_entry.setUpdate_ward_user_id(safEntryRequestDto.getUser_id());

                    } else if (safEntryRequestDto.getEntry_type_id() != 4) {
                        property_entry.setOld_ward_id(0L);
                        property_entry.setUpdate_ward_user_id(0L);
                    } else if (safEntryRequestDto.getEntry_type_id() != 4) {
                        property_entry.setOld_ward_id(0L);
                        property_entry.setUpdate_ward_user_id(0L);
                    }

                    property_entry.setPenltydisc("N/A"); // need to ask
                    try {
                        PropertyMasterBean resultOfPropertyMaster = propertyMasterDao.save(property_entry);
                        log.info("tbl_property_mstr table saved record {}", resultOfPropertyMaster);
                        // newPropId=resultOfPropertyMaster.getId().toString();
                        log.info("New prop_id {}", resultOfPropertyMaster.getId());
                        ownerDetailsEntryService.createOwnerDetailsEntry(resultOfPropertyMaster.getId(), safEntryRequestDto);
                     //  log.info("ownerDetailsService {} ",ownerDetailsEntryService.createOwnerDetailsEntry(resultOfPropertyMaster.getId(), safEntryRequestDto));
                        String  arv= safarvDetailsService.createSAFARV(resultOfPropertyMaster.getId(), safEntryRequestDto);
                        return arv;

                    } catch (BadRequestException e) {
                    		log.info("Bad Request Exception {} ",e.getLocalizedMessage());
                        return new BadRequestException("Data not save ").toString();
                    }
                    //return "Successful";
                }
                else if(safEntryRequestDto.getEntry_type_id() != 1) {
                    throw new BadRequestException("Invalid Entry type for the new Assessment");
                }
}
            return "Successful";

        }

		
		  @Value("${aws.accessKeyId}")
		  private String accessKey;
		  
		  @Value("${aws.secretKey}") 
		  private String secretKey;
		  
		  @Value("${aws.region}")
		  private String region;
		  
		  @Value("${aws.bucketName}") 
		  private String bucketName;
		 
    @Override
    public PropertyDocDtlBean SAFDocumentUpload(MultipartFile file, Long prop_id, Long doc_mstr_id, Long user_id, Long fy_id) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        LocalDateTime localDateTime = LocalDateTime.now();
        File convertedFile = convertMultiPartFileToFile(file);

       String objectKey = prop_id+"-"+file.getOriginalFilename().toString();
		
		  BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,secretKey);
		  AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
		  .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
		  .withRegion(region).build();
		  // replace with your preferred region .build();
		 
        PutObjectRequest putRequest = new PutObjectRequest(bucketName, objectKey, convertedFile);
        s3Client.putObject(putRequest);
        PropertyDocDtlBean propertyDoc=new PropertyDocDtlBean();
        propertyDoc.setProp_id(prop_id);
        propertyDoc.setDoc_mstr_id(doc_mstr_id);
        propertyDoc.setUploaded_doc(objectKey);
        propertyDoc.setUser_id(user_id);
        propertyDoc.setStampdate(localDateTime);
        propertyDoc.setFy_id(fy_id);
        propertyDoc.setStatus(0L);
        propertyDoc.setApproval_status(0L);
        return propertyDocDtlDao.save(propertyDoc);
    }
    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileUtils.writeByteArrayToFile(convertedFile, file.getBytes());
        return convertedFile;
    }
}
