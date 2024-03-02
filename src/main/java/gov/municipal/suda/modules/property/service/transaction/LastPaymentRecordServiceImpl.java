package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.*;
import gov.municipal.suda.modules.property.dao.transaction.DemandDao;
import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.master.*;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;
import gov.municipal.suda.util.DeleteFilesAndFolder;
import gov.municipal.suda.util.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LastPaymentRecordServiceImpl implements LastPaymentRecordService{
    @Autowired
    DemandDao demandDao;
    @Autowired
    LastPaymentRecordDao lastPaymentRecordDao;
    @Autowired
    WardDao wardDao;
    @Autowired
    PropertyMasterDao propertyMasterDao;
    @Autowired
    OwnerTaxMasterDao ownerTaxMasterDao;
    @Autowired
    OwnerDetailsDao ownerDetailsDao;

	
	
	  @Value("${aws.accessKeyId}") private String accessKey;
	  
	  @Value("${aws.secretKey}") private String secretKey;
	  
	  @Value("${aws.region}") private String region;
	  
	  @Value("${aws.property.lastPayment.bucketName}") private String bucketName;
	 
	     @Override
    public Long create(Long prop_id, String from_year, String to_year, Long user_id) throws Exception {
        LastPaymentRecordBean lastPaymentRecordBean=new LastPaymentRecordBean();

        if(prop_id !=null) {
            try {
                List<DemandDetailsBean> demandDetailsByPropId = demandDao.getDemandDetailsByPropId(prop_id);
                if (demandDetailsByPropId.size() > 0) {
                    BigDecimal totalAmount=new BigDecimal(0);
                    BigDecimal penality= new BigDecimal(0);
                    BigDecimal penalCharge=new BigDecimal(0);
                    for(DemandDetailsBean demandInsideLoop : demandDetailsByPropId) {

                            totalAmount=totalAmount.add(demandInsideLoop.getTotal_amount());
                            penality=penality.add(demandInsideLoop.getPenalty());
                            penalCharge=penalCharge.add(demandInsideLoop.getPenal_charge());

                    }


                    //BigDecimal penalCharge = demandDetailsByPropId.stream().filter(v -> v.getEffect_year().equals(from_year)).filter(p -> p.getEffect_year().equals(to_year)).map(m -> m.getPenal_charge()).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal totalFine =penality.add(penality);
                    lastPaymentRecordBean.setProp_id(prop_id);
                    lastPaymentRecordBean.setTime(LocalDateTime.now().toLocalTime().toString());
                    lastPaymentRecordBean.setFrm_year(from_year);
                    lastPaymentRecordBean.setUpto_year(to_year);
                    lastPaymentRecordBean.setReceipt_date(LocalDateTime.now().toLocalDate().toString());
                    lastPaymentRecordBean.setEntry_date(LocalDateTime.now().toLocalDate().toString());
                    lastPaymentRecordBean.setFine_amount(totalFine);
                    lastPaymentRecordBean.setTot_amount(totalAmount);
                    lastPaymentRecordBean.setBook_no(String.valueOf(1111)); //need to ask and rectify
                    lastPaymentRecordBean.setReceipt_no(String.valueOf(33));
                    lastPaymentRecordBean.setRecpt_filename("N/A"); // need to ask and decide how to generate this receipt file
                    lastPaymentRecordBean.setUser_id(user_id);
                    lastPaymentRecordDao.save(lastPaymentRecordBean);

                }
            }
            catch (Exception e){
                throw new BadRequestException(e.getMessage());
            }
        }
        return lastPaymentRecordBean.getId();
    }

    // Main search for LastPayment Update, need to complete in the next release
    @Override
    public List<LastPaymentUpdateSearchResponseDTO> lastPaymentUpdateSearch(Long wardId, String propertyNo, String ownerName, Long mobileNo) {
        List<LastPaymentUpdateSearchResponseDTO> response = new ArrayList<>();
        String queryStringOne=null;
        String queryStringTwo=null;
        if(wardId !=null || propertyNo !=null) {
            if(wardId!=null && propertyNo==null) {
                queryStringOne="select p from PropertyMasterBean p where p.ward_id="+wardId;
            }
            else if(wardId==null && propertyNo!=null) {
                queryStringOne="select p from PropertyMasterBean p where p.ward_id="+propertyNo;
            }
            else if(wardId!=null && propertyNo!=null){
                queryStringOne="select p from PropertyMasterBean p where p.ward_id="+propertyNo+ "and p.ward_id="+wardId;
            }
            if(queryStringOne !=null) {

            }
        }
        else if(ownerName !=null || mobileNo.intValue() > 0) {
            if(ownerName !=null && mobileNo.intValue()==0) {
                queryStringTwo="select o from OwnerDetailsBean o where o.owner_name like" +"%"+ownerName+"%";
            } else if (ownerName==null && mobileNo.intValue() > 0) {
                queryStringTwo="select o from OwnerDetailsBean o where o.mobile_no="+mobileNo;

            }
            else if(ownerName !=null && mobileNo.intValue() > 0) {
                queryStringTwo="select o from OwnerDetailsBean o where o.mobile_no="+mobileNo+ "and o.owner_name like" +"%"+ownerName+"%";
            }

            if(queryStringTwo!=null) {

            }
        }
        return response;
    }

    @Override
    public LastPaymentResponseDTO lastUpdatePaymentView(String propertyNo) {
        LastPaymentResponseDTO dto = new LastPaymentResponseDTO();
        PropertyMasterBean property=propertyMasterDao.findPropertyByPropNo(propertyNo);
        if(property !=null) {
            Optional<WardBean> wardBean=wardDao.findById(property.getWard_id());
            if(wardBean.isPresent()) {
                dto.setWardNo(Integer.parseInt(wardBean.get().getWard_name()));
            }
            dto.setPropertyNo(property.getProperty_no());
            dto.setPropertyAddress(property.getProperty_address());
            dto.setMohalla(property.getMohalla());
            dto.setEntryType(property.getEntry_type());
            dto.setTotalArea(property.getTotalbuilbup_area());
            dto.setKhataNo(property.getKhata_no());
            dto.setMemoNo("NA");
            dto.setOrderDate("NA");
           Optional<OwnerDetailsBean> ownerDetails = ownerDetailsDao.findOwnerDetailsByPropId(property.getId());
           if(ownerDetails.isPresent()) {
               OwnerViewDTO ownerDTO = new OwnerViewDTO();
               ownerDTO.setOwner_name(ownerDetails.get().getOwner_name());
               ownerDTO.setRelation(ownerDetails.get().getRelation());
               ownerDTO.setGuardian_name(ownerDetails.get().getGuardian_name());
               dto.setOwnerDetails(ownerDTO);
           }
            List<OwnerTaxMasterBean> ownerTaxMasterList = ownerTaxMasterDao.getPropertyOwnerTaxByPropId(property.getId());
            if(!ownerTaxMasterList.isEmpty()) {
                List<OwnerTaxViewDTO> ownerTaxList = new ArrayList<>();
                for(OwnerTaxMasterBean ownerTaxBean:ownerTaxMasterList) {
                    OwnerTaxViewDTO ownerDto = new OwnerTaxViewDTO();
                    ownerDto.setComposite_tax(ownerTaxBean.getComposite_tax());
                    ownerDto.setEducation_cess(ownerTaxBean.getEducation_cess());
                    ownerDto.setCommon_wtr_tax(ownerTaxBean.getCommon_wtr_tax());
                    ownerDto.setProperty_tax(ownerTaxBean.getProperty_tax());
                    ownerDto.setEffect_year(ownerTaxBean.getEffect_year());
                    ownerDto.setPersonal_wtr_tax(ownerTaxBean.getPersonal_wtr_tax());
                    ownerDto.setTot_yearly_tax(ownerTaxBean.getTot_yearly_tax());
                    ownerDto.setSanitation_tax(ownerTaxBean.getSanitation_tax());
                    ownerTaxList.add(ownerDto);
                }
                dto.setTaxDetails(ownerTaxList);
            }

            List<DemandDetailsBean> demandList = demandDao.getNewDemandDetailsByPropId(property.getId());
            List<String> getDueDemandEffectDate= new ArrayList<>();
            if(!demandList.isEmpty()) {
               for(DemandDetailsBean demand : demandList) {
                   getDueDemandEffectDate.add(demand.getEffect_year());
               }
               dto.setUpToYear(getDueDemandEffectDate);
            }

        }
        return dto;
    }

    @Override
    public PropertyLastPaymentDetailsViewDTO lastPaymentDetailsView(String propertyNo, String upToYear) {
        PropertyLastPaymentDetailsViewDTO response =new PropertyLastPaymentDetailsViewDTO();
        PropertyMasterBean propertyMasterBean=propertyMasterDao.findPropertyByPropNo(propertyNo);
        if(propertyMasterBean!=null) {
//            List<DemandDetailsBean> demandList = demandDao.getDueDemandByPropIdAndEffectYear(propertyMasterBean.getId(),upToYear);
        	
        	List<DemandDetailsBean> demandList = demandDao.getNewDueDemandByPropIdAndEffectYear(propertyMasterBean.getId(),upToYear);
            log.info("Demand Details Bean for the Property ------ Line 199 ----- {}",demandList.toArray());
            if(demandList.size()>0) {
                BigDecimal grandTotal=new BigDecimal(0.00);
                BigDecimal grandPenalty=new BigDecimal(0.00);
                for (DemandDetailsBean demand : demandList) {
                    grandTotal=grandTotal.add(demand.getTotal_amount());
                    grandPenalty=grandPenalty.add(demand.getPenalty());
                    response.setAmount(grandTotal);
                    response.setPenalty(grandPenalty);
                }
                Optional<String> fetchDemandDate = demandList.stream().filter(v-> v.getEffect_year().equals(upToYear)).map(v->v.getDemand_date()).findAny();
                response.setYear(upToYear);
                response.setDueDate(fetchDemandDate.get());
            }
        }
        return response;
    }
    @Override
    public void PropertyLastPaymentUpdate(MultipartFile file, String propertyNo,
                                          String receiptNo, String receiptDate, String bookNo, Long userId,
                                          String fromYear, String upToYear, BigDecimal totalAmount, BigDecimal fineAmount) throws IOException, ParseException {
        StringBuilder finalFileName=new StringBuilder();
        finalFileName.append(propertyNo);
        //finalFileName.append("-");
        //finalFileName.append(file.getOriginalFilename());
        try {
            if(propertyNo!=null) {
                LastPaymentRecordBean bean= new LastPaymentRecordBean();
             String fileName=UploadFile.AWS_Upload(accessKey, secretKey, region, bucketName, finalFileName.toString(), file);
                PropertyMasterBean propId=propertyMasterDao.findPropertyByPropNo(propertyNo);
                if(propId !=null) {
                    bean.setBook_no(bookNo);
                    bean.setProp_id(propId.getId());
                    bean.setFrm_year(fromYear);
                    bean.setTime(LocalTime.now().toString());
                    bean.setEntry_date(LocalDate.now().toString());
                    bean.setReceipt_date(receiptDate);
                    bean.setReceipt_no(receiptNo);
                    bean.setFine_amount(totalAmount);
                   ////bean.setRecpt_filename(fileName);
                    bean.setUpto_year(upToYear);
                    bean.setTot_amount(totalAmount);
                    bean.setUser_id(userId);
                   LastPaymentRecordBean lastPaymentIds=lastPaymentRecordDao.save(bean);
                   if(lastPaymentIds.getId()!=null) {
//                       List<DemandDetailsBean> demandList=demandDao.getDueDemandByPropIdAndEffectYear(propId.getId(), upToYear);
                	   
                	   List<DemandDetailsBean> demandList=demandDao.getNewDueDemandByPropIdAndEffectYear(propId.getId(), upToYear);
                      if(demandList.size()>0) {
                          for (DemandDetailsBean demand : demandList) {
                            demand.setLast_payment_id(lastPaymentIds.getId());
                            demand.setPaid_status(1);
                            demandDao.save(demand);
                          }
                      }
                   }
                }
            }
        }
        finally {
        DeleteFilesAndFolder.deleteFileFromCurrentDirectory(file.getOriginalFilename());
    }
    }
}
