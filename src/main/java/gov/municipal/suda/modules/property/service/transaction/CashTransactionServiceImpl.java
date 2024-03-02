package gov.municipal.suda.modules.property.service.transaction;

/*import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;*/
import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.exception.dto.ErrorDTO;
import gov.municipal.suda.exception.dto.ErrorResponse;
import gov.municipal.suda.modules.property.dao.master.BankNameDao;
import gov.municipal.suda.modules.property.dao.master.LastPaymentRecordDao;
import gov.municipal.suda.modules.property.dao.master.OwnerTaxMasterDao;
import gov.municipal.suda.modules.property.dao.master.PropertyMasterDao;
import gov.municipal.suda.modules.property.dao.transaction.*;
import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.master.BankNameBean;
import gov.municipal.suda.modules.property.model.master.LastPaymentRecordBean;
import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import gov.municipal.suda.modules.property.model.transaction.*;
import gov.municipal.suda.usermanagement.service.FileStorageService;
import gov.municipal.suda.util.DeleteFilesAndFolder;
import gov.municipal.suda.util.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CashTransactionServiceImpl implements CashTransactionService {
    @Autowired
    DemandDao demandDao;
    @Autowired
    PropertyMasterDao propertyMasterDao;

    @Autowired
    OwnerTaxMasterDao ownerTax;

    @Autowired
    CashTransactionDao cashTransactionDao;

    @Autowired
    CashTransactionDetailDao cashTransactionDetailDao;

    @Autowired
    LastPaymentRecordService lastPaymentRecordService;
    @Autowired
    CollectionService collectionService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private BankNameDao bankNameDao;
    @Autowired
    LastPaymentRecordDao lastPaymentRecordDao;
    @Autowired
    TransactionViewUpdateDao transactionViewUpdateDao;
    @Autowired
    TransactionDeactivationDao transactionDeactivationDao;
    @Autowired
    CollectionDao collectionDao;

    @Override
//    @Transactional(rollbackOn = BadRequestException.class)
    public String makeCashTransaction(CashPaymentRequestDto cashPaymentRequestDto) throws Exception {
    	log.info("CashPaymentREquest Dto  =------ Line 92 ----- {}",cashPaymentRequestDto);
        long startTimerForMainCashTransactionMethod = System.currentTimeMillis();
        String generateTransactionNo = null;
        if (cashPaymentRequestDto != null) {
            try {
                long startTimefindPropNo = System.currentTimeMillis();
                Long prop_id = propertyMasterDao.findIdByPropNo(cashPaymentRequestDto.getProperty_no());
                long endTimefindPropNo = System.currentTimeMillis();
                long durationfindPropNo = endTimefindPropNo - startTimefindPropNo;
                log.info("Request take findIdByPropNo" + durationfindPropNo + " ms");
                PropertyTransactionBean cashTransaction = new PropertyTransactionBean();
                if (prop_id != null) {
                    long startTimerainHarvest = System.currentTimeMillis();
                    String rainHarvest = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getRain_harvest() : null;
                    long endTimerainHarvest = System.currentTimeMillis();
                    long durationrainHarvest = endTimerainHarvest - startTimerainHarvest;
                    log.info("Request take rainHarvest" + durationrainHarvest + " ms");

                    long startTimeold_ward_id = System.currentTimeMillis();
                    Long old_ward_id = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getOld_ward_id() : 0L;
                    long endTimeold_ward_id = System.currentTimeMillis();
                    long durationold_ward_id = endTimeold_ward_id - startTimeold_ward_id;
                    log.info("Request take old_ward_id" + durationold_ward_id + " ms");
                    long startTimeward_id = System.currentTimeMillis();
                    Long ward_id = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getWard_id() : 0L;
                    long endTimeward_id = System.currentTimeMillis();
                    long durationward_id = endTimeward_id - startTimeward_id;
                    log.info("Request take ward_id" + durationward_id + " ms");
                    long startTimeusage_type_id = System.currentTimeMillis();
                    Long usage_type_id = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getUsage_type_id() : 0L;
                    long endTimeusage_type_id = System.currentTimeMillis();
                    long durationusage_type_id = endTimeusage_type_id - startTimeusage_type_id;
                    log.info("Request take usage_type_id" + durationusage_type_id + " ms");
                    if (old_ward_id == null) {
                        old_ward_id = 0L;
                    }
                    Integer lastIndex = 0;
                    //Efffectiv Date = Jis Saal ka payment ho rha hai
                    if (cashPaymentRequestDto.getEffective_date().size() > 1) {
                        lastIndex = cashPaymentRequestDto.getEffective_date().size() - 1;
                    }
                    long startTimegetMaxValue = System.currentTimeMillis();
                    Long getMaxValue = cashTransactionDao.generateId();
                    log.info("getMaxValue ------ Line 138 ----- {}",getMaxValue);
//                    Long lastPaymentId = lastPaymentRecordDao.generateLastPaymentId();
                    long endTimegetMaxValue = System.currentTimeMillis();
                    long durationgetMaxValue = endTimegetMaxValue - startTimegetMaxValue;
                    //log.info("Request take getMaxValue" + durationgetMaxValue + " ms");
                    BigDecimal discount = new BigDecimal(0.00);
                    BigDecimal payableAmount = new BigDecimal(0.00);
                    BigDecimal rainHarvestCalculation = new BigDecimal(0.00);
                    BigDecimal penalty = new BigDecimal(0.00);
                    BigDecimal total_amount = new BigDecimal(0.00);
                    BigDecimal difference_amount = new BigDecimal(0.00);
                    String finalRemove = null;
                    long startTimeForLoopcashPaymentRequestDto = System.currentTimeMillis();
                    //This Loop is Responsible for Payment 
                    for (Long id : cashPaymentRequestDto.getId()) {

                        long startTimeEffect_year = System.currentTimeMillis();
                        String effect_year = demandDao.findById(id).isPresent() ? demandDao.findById(id).get().getEffect_year() : null;
                        long endTimeEffect_year = System.currentTimeMillis();
                        long durationEffect_year = endTimeEffect_year - startTimeEffect_year;
                        log.info("Request take Effect_year" + durationEffect_year + " ms");

                        log.info("Effect Year {}", effect_year);
                        long startTimefindActiveDemandResult = System.currentTimeMillis();
                        //Gettting a demand as per id ----- for calcuation -----
                        Optional<DemandDetailsBean> findActiveDemandResult = demandDao.findByDemandId(id);
                        long endTimefindActiveDemandResult = System.currentTimeMillis();
                        long durationfindActiveDemandResult = endTimefindActiveDemandResult - startTimefindActiveDemandResult;
                        log.info("Request take findActiveDemandResult" + durationfindActiveDemandResult + " ms");
                        DemandDetailsBean findActiveDemand = findActiveDemandResult.isPresent() ? findActiveDemandResult.get() : null;
                        log.info("FindActiveDemamd ------ Line 164 ----- {}",findActiveDemand);
                        if (findActiveDemand !=null) {
                        	//Picking Up current Date and Time -------
                            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
                            String removeDash = timestamp.toString().replaceAll("\\-", "");
                            String colonSeperation = removeDash.replaceAll("\\:", "");
                            String splitString = colonSeperation.split("\\.", 2)[0];
                            finalRemove = splitString.replaceAll("\\s+", "");
                            log.info("Final Remove ", finalRemove);
                            discount.add(discount);
                            
                       
                            String financial_Year = effect_year.substring(5, 9);
                            log.info("Financial Year ---- Line--186--- {} ",financial_Year);
                            LocalDate date =LocalDate.now();
//                            LocalDate date =LocalDate.of(2024, 06, 15);
                            	String currentFianancialYear = date.toString().substring(0, 4);
                            	log.info("Current Financial Year ---- Line 190 --- {}",currentFianancialYear);
                            	
                            if(financial_Year.equals(currentFianancialYear)){
                            
                            log.info("Get Year from TimeStamp ------ Line 179 -----{} ",date);
                            int year = date.getYear();
                            log.info("Get date from TimeStamp ------ Line 181 -----{} ",year);
                            Month month = date.getMonth();
                            log.info("Get Month from TimeStamp ------ Line 179 -----{} ",month);
                            int dayOfMonth = date.getDayOfMonth();
                            log.info("Get Month from TimeStamp ------ Line 179 -----{} ",dayOfMonth);

                            if ((month == Month.APRIL && dayOfMonth >= 1) || (month == Month.MAY) || (month == Month.JUNE)
                                    || (month == Month.JULY && dayOfMonth <= 31)) {
                                // 1st-April to 31st-July, discount = 6.25%
                                discount = new BigDecimal(6.25);
                                discount.add(discount);
                                cashPaymentRequestDto.setDiscount(discount);
                                total_amount =findActiveDemand.getTotal_amount();
                                log.info("Temp Total Before ------ Line 197 ---- {}",total_amount);
                                BigDecimal tempTotal = findActiveDemand.getTotal_amount();
                                log.info("TempTotal ------ Line 197 ---- {}",tempTotal);
                                tempTotal= tempTotal.multiply(discount).divide(new BigDecimal(100.00));
                                log.info("TempTotal ------ Line 197 ---- {}",tempTotal);
                                total_amount =total_amount.subtract(tempTotal).setScale(2,RoundingMode.CEILING);
                                log.info("Total Amout After Discount ------ Line 197 ---- {}",total_amount);
//                                total_amount = total_amount.add(findActiveDemand.getTotal_amount()).multiply(discount);
                                DemandDetailsBean foundDemand =findActiveDemand;
                                if(foundDemand==null)
                                	throw new Exception("Demand No found");
                                log.info("Demand No Generated ----- Line 224 -----{}",foundDemand);
                                foundDemand.setFinalAmountAfterDiscount(total_amount);
                                foundDemand.setAllowance_percent(discount);
                                demandDao.save(foundDemand);
                            } else if ((month == Month.AUGUST && dayOfMonth >= 1) || (month == Month.SEPTEMBER)
                                    || (month == Month.OCTOBER) || (month == Month.NOVEMBER) || (month == Month.DECEMBER && dayOfMonth <= 31)) {
                                // 1st-August to 31st-December, discount = 4%
                            	discount = new BigDecimal(4);
                            	discount.add(discount);
                            	 cashPaymentRequestDto.setDiscount(discount);
                                total_amount =findActiveDemand.getTotal_amount();
                                log.info("Temp Total Before ------ Line 197 ---- {}",total_amount);
                                BigDecimal tempTotal = findActiveDemand.getTotal_amount();
                                log.info("TempTotal ------ Line 197 ---- {}",tempTotal);
                                tempTotal= tempTotal.multiply(discount).divide(new BigDecimal(100.00));
                                log.info("TempTotal ------ Line 197 ---- {}",tempTotal);
                                total_amount =total_amount.subtract(tempTotal).setScale(2,RoundingMode.CEILING);
                                log.info("Total Amout After Discount ------ Line 197 ---- {}",total_amount);
                                
                                DemandDetailsBean foundDemand =findActiveDemand;
                                if(foundDemand==null)
                                	throw new Exception("Demand No found");
                                log.info("Demand No Generated ----- Line 224 -----{}",foundDemand);
                                foundDemand.setFinalAmountAfterDiscount(total_amount);
                                foundDemand.setAllowance_percent(discount);
                                demandDao.save(foundDemand);
                                
                            } else {
                                // 1st-January to 31st-March, discount = 0
                            	discount = new BigDecimal(0);
                                discount.add(discount);
//                                total_amount = total_amount.add(findActiveDemand.getTotal_amount());
                                total_amount = findActiveDemand.getTotal_amount();
                                cashPaymentRequestDto.setDiscount(discount);
                                DemandDetailsBean foundDemand =findActiveDemand;
                                if(foundDemand==null)
                                	throw new Exception("Demand No found");
                                log.info("Demand No Generated ----- Line 224 -----{}",foundDemand);
                                foundDemand.setFinalAmountAfterDiscount(total_amount);
                                foundDemand.setAllowance_percent(discount);
                                demandDao.save(foundDemand);
                                
                                log.info("Total Amount ------ Line 207 ---- {}",total_amount);
                            }
                            log.info("discount Percent ------ Line 205 ---- {}",discount);
                            
                            log.info("Total Amount ------ Line 211 ---- {}",total_amount);
                            }
                            else {
                                // 1st-January to 31st-March, discount = 0
                            	discount = new BigDecimal(0);
                                discount.add(discount);
//                                total_amount = total_amount.add(findActiveDemand.getTotal_amount());
                                total_amount = findActiveDemand.getTotal_amount();
                                cashPaymentRequestDto.setDiscount(discount);
                                DemandDetailsBean foundDemand =findActiveDemand;
//                                DemandDetailsBean tempDemandDetails= foundDemand.get(0);
                                if(foundDemand==null) {
                                	throw new Exception("Demand No found");
                                }
                                log.info("Demand No Generated ----- Line 224 -----{}",foundDemand);
                                foundDemand.setFinalAmountAfterDiscount(total_amount);
                                foundDemand.setAllowance_percent(discount);
                                demandDao.save(foundDemand);
//                                
//                                for(DemandDetailsBean demandDetails: foundDemand) {
//                                    log.info("Demand No Generated ----- Line 224 -----{}",foundDemand);
//                                    demandDetails.setFinalAmountAfterDiscount(discount);
//                                    demandDetails.setAllowance_percent(discount);
//                                    demandDao.save(demandDetails);
//                                    }
                                    
                                
                                
                                
                                log.info("Total Amount ------ Line 207 ---- {}",total_amount);
                            }
                            log.info("discount Percent ------ Line 205 ---- {}",discount);
                            
                            log.info("Total Amount ------ Line 211 ---- {}",total_amount);
                           
                            rainHarvestCalculation=rainHarvestCalculation.add(findActiveDemand.getRain_harvest_charge());

                            long startTimecreateCollection = System.currentTimeMillis();
                            collectionService.createCollection(prop_id, effect_year, cashPaymentRequestDto.getUser_id(), getMaxValue);
                            long endTimecreateCollection = System.currentTimeMillis();
                            long durationcreateCollection = endTimecreateCollection - startTimecreateCollection;
                            log.info("Request take createCollection" + durationcreateCollection + " ms");

                            findActiveDemand.setPaid_status(1);
                            findActiveDemand.setLast_payment_id(0L);
                            long startTimeActiveDemandsave = System.currentTimeMillis();
                            demandDao.save(findActiveDemand); // set paid status
                            long endTimeActiveDemandsave = System.currentTimeMillis();
                            long durationActiveDemandsave = endTimeActiveDemandsave - startTimeActiveDemandsave;
                            log.info("Request take ActiveDemandsave" + durationActiveDemandsave + " ms");

                        } else if (findActiveDemand==null) {
                          return "No Active Demand";

                        }
                    }



                    cashTransaction.setDemand_payment(cashPaymentRequestDto.getDemand_payment().setScale(2, RoundingMode.CEILING));
                    cashTransaction.setCash_verify_id(0L);
//                    cashTransaction.setPayable_amt(cashPaymentRequestDto.getPayable_amt().setScale(2, RoundingMode.CEILING));
                    cashTransaction.setPayable_amt(total_amount);
                    cashTransaction.setPenalty(cashPaymentRequestDto.getPenalty().setScale(2, RoundingMode.CEILING));
                    cashTransaction.setDiscount(cashPaymentRequestDto.getDiscount().setScale(2, RoundingMode.CEILING));
                    cashTransaction.setForm_fee(BigDecimal.valueOf(5.00));
                    //Long ownerTaxId = findActiveDemand.getTax_rate_id();

                    Integer nextId = getMaxValue.intValue();//cashTransactionDao.generateId(); // need to ask
                    //nextId= nextId+1;
                    generateTransactionNo = nextId.toString().concat(finalRemove);
                    log.info("Current Transaction -------LINE 272---{}", generateTransactionNo);
                    cashTransaction.setTransaction_no(generateTransactionNo);
                    cashTransaction.setTransaction_date(Timestamp.valueOf(LocalDateTime.now())); // need to ask to reform
                    cashTransaction.setPayment_mode(cashPaymentRequestDto.getPayment_mode());
                    cashTransaction.setRemarks(cashPaymentRequestDto.getNarration());
                    cashTransaction.setIp_address(cashPaymentRequestDto.getIp_address());
                    cashTransaction.setCancel_status(0);
                    cashTransaction.setCash_verify_id(0L);
                    cashTransaction.setCash_verify_stts(0);
                    cashTransaction.setVerify_date(null);
                    cashTransaction.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
                    cashTransaction.setUser_id(cashPaymentRequestDto.getUser_id());
                    cashTransaction.setStatus(1);
                    difference_amount=cashPaymentRequestDto.getDifference_amount();
                    if(difference_amount==null) {
                        difference_amount=new BigDecimal(0.00);
                    }
                    cashTransaction.setRain_wtr_harvest(cashPaymentRequestDto.getRain_harvesting());
                    cashTransaction.setOld_ward_id(old_ward_id);
                    cashTransaction.setProp_id(prop_id);
                    cashTransaction.setWard_id(ward_id);
                    cashTransaction.setUsage_type_id(usage_type_id);
                    cashTransaction.setDifference_amount(difference_amount);
                    PropertyTransactionBean returnResult = cashTransactionDao.save(cashTransaction);
                    if(returnResult==null)
                    throw new Exception("Property Transaction BEan is not Saved ----- Line 298  -------");                  	
                    
                    if (cashPaymentRequestDto.getPayment_mode().equals("CHEQUE") || cashPaymentRequestDto.getPayment_mode().equals("Cheque")) {
                        ChequeDDCardTransactionBean chequeDDCardTransactionBean = new ChequeDDCardTransactionBean();
                        chequeDDCardTransactionBean.setTransaction_id(getMaxValue);
                        chequeDDCardTransactionBean.setProp_id(prop_id);
                        chequeDDCardTransactionBean.setCheque_no(cashPaymentRequestDto.getCheque_no());
                        chequeDDCardTransactionBean.setCheque_dt(cashPaymentRequestDto.getCheque_date());
                        chequeDDCardTransactionBean.setCheck_status(0);
                        if (cashPaymentRequestDto.getBank_name().equals("OTHERS")) {
                            chequeDDCardTransactionBean.setBank_name(cashPaymentRequestDto.getOthers_bank_name());
                            BankNameBean bankNameBean = new BankNameBean();
                            bankNameBean.setBank_name(cashPaymentRequestDto.getOthers_bank_name());
                            bankNameDao.save(bankNameBean);
                        } else {
                            chequeDDCardTransactionBean.setBank_name(cashPaymentRequestDto.getBank_name());
                        }
                        chequeDDCardTransactionBean.setBranch_name(cashPaymentRequestDto.getBranch());
                        chequeDDCardTransactionBean.setAmount(cashPaymentRequestDto.getPayable_amt().setScale(2, RoundingMode.CEILING));
                        cashTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }
                    if (cashPaymentRequestDto.getPayment_mode().equals("CARD") || cashPaymentRequestDto.getPayment_mode().equals("Card")) {
                        ChequeDDCardTransactionBean chequeDDCardTransactionBean = new ChequeDDCardTransactionBean();
                        chequeDDCardTransactionBean.setTransaction_id(getMaxValue);
                        chequeDDCardTransactionBean.setProp_id(prop_id);
                        chequeDDCardTransactionBean.setCard_no(cashPaymentRequestDto.getCard_no());
                        chequeDDCardTransactionBean.setCard_type(cashPaymentRequestDto.getCard_type());
                        chequeDDCardTransactionBean.setCard_holder_name(cashPaymentRequestDto.getCard_holder_name());
                        if (cashPaymentRequestDto.getBank_name().equals("OTHERS")) {
                            chequeDDCardTransactionBean.setBank_name(cashPaymentRequestDto.getOthers_bank_name());
                            BankNameBean bankNameBean = new BankNameBean();
                            bankNameBean.setBank_name(cashPaymentRequestDto.getOthers_bank_name());
                            bankNameDao.save(bankNameBean);
                        } else {
                            chequeDDCardTransactionBean.setBank_name(cashPaymentRequestDto.getBank_name());
                        }
                        chequeDDCardTransactionBean.setBranch_name(cashPaymentRequestDto.getBranch());
                        chequeDDCardTransactionBean.setAmount(cashPaymentRequestDto.getPayable_amt().setScale(2, RoundingMode.CEILING));
                        cashTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }
                    if (cashPaymentRequestDto.getPayment_mode().equals("DD") || cashPaymentRequestDto.getPayment_mode().equals("Dd")) {
                        ChequeDDCardTransactionBean chequeDDCardTransactionBean = new ChequeDDCardTransactionBean();
                        chequeDDCardTransactionBean.setTransaction_id(getMaxValue);
                        chequeDDCardTransactionBean.setProp_id(prop_id);
                        chequeDDCardTransactionBean.setCheque_no(cashPaymentRequestDto.getDd_no());
                        chequeDDCardTransactionBean.setCheque_dt(cashPaymentRequestDto.getDd_date());
                        if (cashPaymentRequestDto.getBank_name().equals("OTHERS")) {
                            chequeDDCardTransactionBean.setBank_name(cashPaymentRequestDto.getOthers_bank_name());
                            BankNameBean bankNameBean = new BankNameBean();
                            bankNameBean.setBank_name(cashPaymentRequestDto.getOthers_bank_name());
                            bankNameDao.save(bankNameBean);
                        } else {
                            chequeDDCardTransactionBean.setBank_name(cashPaymentRequestDto.getBank_name());
                        }
                        chequeDDCardTransactionBean.setBranch_name(cashPaymentRequestDto.getBranch());
                        chequeDDCardTransactionBean.setAmount(cashPaymentRequestDto.getPayable_amt());
                        cashTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }
                    if (cashPaymentRequestDto.getPayment_mode().equals("RTGS") || cashPaymentRequestDto.getPayment_mode().equals("Rtgs")) {
                        ChequeDDCardTransactionBean chequeDDCardTransactionBean = new ChequeDDCardTransactionBean();
                        chequeDDCardTransactionBean.setTransaction_id(getMaxValue);
                        chequeDDCardTransactionBean.setProp_id(prop_id);
                        chequeDDCardTransactionBean.setCheque_no(cashPaymentRequestDto.getRtgs_no());
                        chequeDDCardTransactionBean.setCheque_dt(cashPaymentRequestDto.getRtgs_date());
                        if (cashPaymentRequestDto.getBank_name().equals("OTHERS")) {
                            chequeDDCardTransactionBean.setBank_name(cashPaymentRequestDto.getOthers_bank_name());
                            BankNameBean bankNameBean = new BankNameBean();
                            bankNameBean.setBank_name(cashPaymentRequestDto.getOthers_bank_name());
                            bankNameDao.save(bankNameBean);
                        } else {
                            chequeDDCardTransactionBean.setBank_name(cashPaymentRequestDto.getBank_name());
                        }
                        chequeDDCardTransactionBean.setBranch_name(cashPaymentRequestDto.getBranch());
                        chequeDDCardTransactionBean.setAmount(total_amount);
                        ChequeDDCardTransactionBean savedTransaction =cashTransactionDetailDao.save(chequeDDCardTransactionBean);
                        if(savedTransaction==null) 
                        	throw new Exception("-------Transaction not saved------");
                        
                    }
                    if (cashPaymentRequestDto.getPayment_mode().equals("NEFT") || cashPaymentRequestDto.getPayment_mode().equals("Neft")) {
                        ChequeDDCardTransactionBean chequeDDCardTransactionBean = new ChequeDDCardTransactionBean();
                        chequeDDCardTransactionBean.setTransaction_id(getMaxValue);
                        chequeDDCardTransactionBean.setProp_id(prop_id);
                        chequeDDCardTransactionBean.setCheque_no(cashPaymentRequestDto.getNeft_no());
                        chequeDDCardTransactionBean.setCheque_dt(cashPaymentRequestDto.getNeft_date());
                        if (cashPaymentRequestDto.getBank_name().equals("OTHERS")) {
                            chequeDDCardTransactionBean.setBank_name(cashPaymentRequestDto.getOthers_bank_name());
                            BankNameBean bankNameBean = new BankNameBean();
                            bankNameBean.setBank_name(cashPaymentRequestDto.getOthers_bank_name());
                            bankNameDao.save(bankNameBean);
                        } else {
                            chequeDDCardTransactionBean.setBank_name(cashPaymentRequestDto.getBank_name());
                        }
                        chequeDDCardTransactionBean.setBranch_name(cashPaymentRequestDto.getBranch());
                        chequeDDCardTransactionBean.setAmount(cashPaymentRequestDto.getPayable_amt().setScale(2, RoundingMode.CEILING));
                        cashTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }

                } else if (prop_id == null) {
                    return "property id not found, please make new assessment";
                }
            } catch (Exception e) {
                throw new BadRequestException(e.getMessage());
            }
        } else if (cashPaymentRequestDto == null) {
            throw new BadRequestException("Property Id or effective date should'nt be blank");
        }

        long endTimeForMainCashTransactionMethod = System.currentTimeMillis();
        long durationForMainCashTransactionMethod = endTimeForMainCashTransactionMethod - startTimerForMainCashTransactionMethod;
        log.info("Request take ForMainCashTransactionMethod" + durationForMainCashTransactionMethod + " ms");

        return generateTransactionNo;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    @Override
    public List<PaymentViewBeforeTransDTO> paymentView(PaymentViewBeforeTransDTO paymentViewBeforeTransDTO) {
        //List<DemandDetailsBean> findPayableDemandResult =demandDao.findAll().stream().filter(v->v.getPaid_status()==0).collect(Collectors.toList());
        BigDecimal discount = new BigDecimal(0.00);
        BigDecimal payableAmount = new BigDecimal(0.00);
        BigDecimal rainHarvestCalculation = new BigDecimal(0.00);
        BigDecimal penalty = new BigDecimal(0.00);
        BigDecimal total_amount = new BigDecimal(0.00);
        PaymentViewBeforeTransDTO leastPayableDemand = new PaymentViewBeforeTransDTO();
        List<PaymentViewBeforeTransDTO> leastPayable = new ArrayList<>();
        List<Long> id1 = new ArrayList<>();
//        List<String> years = paymentViewBeforeTransDTO.getEffective_date();
//        String[] array = years.toArray(new String[years.size()]);
//        Integer length=array.length-1;
//        String from_year;
//        String to_year = array[length];
        for (Long id : paymentViewBeforeTransDTO.getId()) {
            //log.info(String.valueOf(id));
            Optional<DemandDetailsBean> findPayableDemand = demandDao.findByDemandId(id);
            DemandDetailsBean findActiveDemand = findPayableDemand.isPresent() ? findPayableDemand.get() : null;
            //log.info(String.valueOf(findActiveDemand.getTotal_amount()));
            Calendar currentCalendar = Calendar.getInstance();
            if (currentCalendar.get(Calendar.YEAR) == Integer.parseInt(findActiveDemand.getEffect_year().substring(0, 4))) {
                if (currentCalendar.get(Calendar.MONTH) == 3 || currentCalendar.get(Calendar.MONTH) == 5) {
                    discount = discount.multiply(findActiveDemand.getTotal_amount().multiply(BigDecimal.valueOf(0.625)));
                } else if (currentCalendar.get(Calendar.MONTH) == 6 || currentCalendar.get(Calendar.MONTH) == 7) {
                    discount = discount.multiply(findActiveDemand.getTotal_amount().multiply(BigDecimal.valueOf(0.05)));

                } else if (currentCalendar.get(Calendar.MONTH) == 8 || currentCalendar.get(Calendar.MONTH) == 9) {
                    discount = discount.multiply(findActiveDemand.getTotal_amount().multiply(BigDecimal.valueOf(0.04)));

                } else if (currentCalendar.get(Calendar.MONTH) == 10 || currentCalendar.get(Calendar.MONTH) == 11) {
                    discount = discount.multiply(findActiveDemand.getTotal_amount().multiply(BigDecimal.valueOf(0.02)));

                }
            }

            total_amount = total_amount.add(findActiveDemand.getTotal_amount());
            log.info(String.valueOf(total_amount));
            penalty = penalty.add(findActiveDemand.getPenalty());
            discount = discount.add(discount);
            payableAmount = total_amount.add(BigDecimal.valueOf(3.00)).subtract(discount);
            id1.add(id);

        }
        leastPayableDemand.setDemand_payment(total_amount);
        leastPayableDemand.setForm_fee(BigDecimal.valueOf(5.00));
        leastPayableDemand.setDiscount(discount);
        leastPayableDemand.setPenalty(penalty);
        leastPayableDemand.setPayable_amt(payableAmount);
        leastPayableDemand.setId(id1);
//        leastPayableDemand.setFrom_year(from_year);
//        leastPayableDemand.setFrom_year(to_year);
        leastPayable.add(leastPayableDemand);
        return leastPayable;
    }

    @Override
    public Page<PaymentTransactionViewDTO> paymentTransactionView(String fromDate, String toDate, String payment_mode, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PaymentTransactionViewDTO> query = builder.createQuery(PaymentTransactionViewDTO.class);
        Root<PaymentTransactionViewDTO> root = query.from(PaymentTransactionViewDTO.class);
        List<PaymentTransactionViewDTO> entities =new ArrayList<>();
        List<PaymentTransactionViewDTO> tempEntities =new ArrayList<>();
        List<PaymentTransactionViewDTO> tempCash =new ArrayList<>();
        List<PaymentTransactionViewDTO> tempCheque =new ArrayList<>();

        TypedQuery<PaymentTransactionViewDTO> typedQuery=null;
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //log.info("paymentMode......" + payment_mode);
        Timestamp todayStart = Timestamp.valueOf(fromDate + " 00:00:00.00");
        Timestamp todayEnd = Timestamp.valueOf(toDate + " 23:59:59.999999999");
        log.info(String.valueOf(todayStart));
        log.info(String.valueOf(todayEnd));
        if (payment_mode.isEmpty() || payment_mode.equals(null) || payment_mode.isBlank()) {
            query.where(builder.between(root.get("stampdate"), todayStart, todayEnd));
            typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            typedQuery.setMaxResults(pageable.getPageSize());
            tempEntities = typedQuery.getResultList();
            for(PaymentTransactionViewDTO dto : tempEntities) {
                if(dto.getPayment_mode().equalsIgnoreCase("Cash")) {
                    tempCash=tempEntities.stream().filter(v-> Objects.nonNull(v.getCash_verify_stts()))
                            .filter(v->v.getCash_verify_stts().equals("0"))
                            .collect(Collectors.toList());
                }
                else if(dto.getPayment_mode().equalsIgnoreCase("Cheque")) {
                    tempCheque=tempEntities.stream().filter(v-> Objects.nonNull(v.getCheck_status()))
                            .filter(v-> v.getCheck_status().equals("0"))
                            .collect(Collectors.toList());
                }
            }
            entities.addAll(tempCash);
            entities.addAll(tempCheque);

        } else if(payment_mode.equalsIgnoreCase("Cash")){
            query.where(
                    builder.between(root.get("stampdate"), todayStart, todayEnd),
                    builder.equal(root.get("payment_mode"), payment_mode),
                    builder.equal(root.get("cash_verify_stts"),0),
                    builder.equal(root.get("status"),1));
        }
        else if(payment_mode.equalsIgnoreCase("Cheque")) {

                query.where(
                        builder.between(root.get("stampdate"), todayStart, todayEnd),
                        builder.equal(root.get("payment_mode"), payment_mode),
                        builder.equal(root.get("check_status"),0),
                        builder.equal(root.get("status"),1));
        }

        //String queryString = typedQuery.unwrap(org.hibernate.query.Query.class).getQueryString();
        //System.out.println("Query: " + queryString);

        if (!payment_mode.isEmpty()) {
            typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            typedQuery.setMaxResults(pageable.getPageSize());
            entities = typedQuery.getResultList();
        }
        Long count = countEntitiesByParameters(todayStart, todayEnd, payment_mode);

        return new PageImpl<>(entities, pageable, count);
    }

    private Long countEntitiesByParameters(Timestamp todayStart, Timestamp todayEnd, String payment_mode) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<PaymentTransactionViewDTO> root = query.from(PaymentTransactionViewDTO.class);

        if (payment_mode.equals("") || payment_mode.equals(null)) {
            query.select(builder.count(root)).where(builder.between(root.get("stampdate"), todayStart, todayEnd));

        } else {
            query.select(builder.count(root)).where(builder.between(root.get("stampdate"), todayStart, todayEnd),
                    builder.and(builder.equal(root.get("payment_mode"), payment_mode)));
        }
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public Optional<ReconcillationViewDTO> reconciliationViewById(Long id) {
        ReconcillationViewDTO reconcillationViewDTO = new ReconcillationViewDTO();
        reconcillationViewDTO.setPropertyTransactionBeans(cashTransactionDao.findById(id).stream().collect(Collectors.toList()));
        reconcillationViewDTO.setChequeDDCardTransactionBeans(cashTransactionDetailDao.findReconUpdateById(id).stream().collect(Collectors.toList()));
        //return cashTransactionDao.findById(id);
        return Optional.of(reconcillationViewDTO);
    }
    @Override
    public void reconciliationUpdateById(ReconcillationUpdateDTO reconcillationUpdateDTO) {
        log.info("inside payment update");
        Optional<PropertyTransactionBean> transactionMasterBean=cashTransactionDao.findById(reconcillationUpdateDTO.getTransaction_id());
        if(transactionMasterBean.isPresent()) {
            ChequeDDCardTransactionBean transactionDetail = cashTransactionDetailDao.findReconUpdateById(reconcillationUpdateDTO.getTransaction_id()).orElse(null);
            if(transactionMasterBean.get().getPayment_mode().equalsIgnoreCase("Cheque")) {
            if(reconcillationUpdateDTO.getCheck_status()==1) { // for status cleared
                transactionDetail.setCleared_by(reconcillationUpdateDTO.getUser_id());
                transactionDetail.setClear_stampdate(Timestamp.valueOf(LocalDateTime.now()));
                transactionDetail.setReconcilation_dt(Timestamp.valueOf(LocalDateTime.now()));
                transactionDetail.setRemarks(reconcillationUpdateDTO.getRemarks());
                transactionDetail.setCheck_status(reconcillationUpdateDTO.getCheck_status());
                ChequeDDCardTransactionBean count = cashTransactionDetailDao.save(transactionDetail);
            } 
         // for status Bounce
            else if (reconcillationUpdateDTO.getCheck_status()==3) {  
                transactionMasterBean.get().setCancel_status(1);
                transactionMasterBean.get().setStatus(0);
                cashTransactionDao.save(transactionMasterBean.get());
                transactionDetail.setCleared_by(reconcillationUpdateDTO.getUser_id());
                transactionDetail.setClear_stampdate(Timestamp.valueOf(LocalDateTime.now()));
                transactionDetail.setReconcilation_dt(Timestamp.valueOf(LocalDateTime.now()));
                transactionDetail.setRemarks(reconcillationUpdateDTO.getRemarks());
                transactionDetail.setCheck_status(reconcillationUpdateDTO.getCheck_status());
                ChequeDDCardTransactionBean count = cashTransactionDetailDao.save(transactionDetail);
                List<PropertyCollectionMstrBean> collectionList=collectionDao.findCollectionByPropIdAndTransactionId(transactionMasterBean.get().getProp_id(),transactionMasterBean.get().getId());
                if(!collectionList.isEmpty()) {
                    for(PropertyCollectionMstrBean collection: collectionList) {
                        PropertyCollectionMstrBean collectionBean=collectionDao.findById(collection.getId()).orElse(null);
                        if(collectionBean!=null) {
                            collectionBean.setStatus(0);
                            collectionDao.save(collectionBean);
                        }
                       DemandDetailsBean demand=demandDao.findById(collection.getDemand_id()).orElse(null);
                       if(demand !=null) {
                           demand.setPaid_status(0);
                           demandDao.save(demand);
                       }

                    }
                }
            }

        }
        else if(transactionMasterBean.get().getPayment_mode().equalsIgnoreCase("Cash")) {
                if(reconcillationUpdateDTO.getCheck_status()==1) { // for status cleared
                    transactionMasterBean.get().setCash_verify_id(reconcillationUpdateDTO.getUser_id());
                    transactionMasterBean.get().setCash_verify_stts(1);
                    transactionMasterBean.get().setVerify_date(Timestamp.valueOf(LocalDateTime.now()));
                    cashTransactionDao.save(transactionMasterBean.get());

                }
                else if (reconcillationUpdateDTO.getCheck_status()==3) { // for status bounced
                    transactionMasterBean.get().setStatus(0);
                    transactionMasterBean.get().setCash_verify_id(reconcillationUpdateDTO.getUser_id());
                    transactionMasterBean.get().setVerify_date(Timestamp.valueOf(LocalDateTime.now()));
                    transactionMasterBean.get().setCancel_status(1);

                    cashTransactionDao.save(transactionMasterBean.get());

                    List<PropertyCollectionMstrBean> collectionList=collectionDao.findCollectionByPropIdAndTransactionId(transactionMasterBean.get().getProp_id(),transactionMasterBean.get().getId());
                    if(!collectionList.isEmpty()) {
                        for(PropertyCollectionMstrBean collection: collectionList) {
                            PropertyCollectionMstrBean collectionBean=collectionDao.findById(collection.getId()).orElse(null);
                            if(collectionBean!=null) {
                                collectionBean.setStatus(0);
                                collectionDao.save(collectionBean);
                            }
                            DemandDetailsBean demand=demandDao.findById(collection.getDemand_id()).orElse(null);
                            if(demand !=null) {
                                demand.setPaid_status(0);
                                demandDao.save(demand);
                            }

                        }
                    }

                }
        }
        }
    }

    @Override
    public List<TransactionViewUpdateDTO> transactionViewByTrnNo(String transactionNo) {
        List<TransactionViewUpdateDTO> response= new ArrayList<>();
        List<TransactionViewUpdateDTO> transactionBeans = transactionViewUpdateDao.transactionViewByTrnNo(transactionNo);
        if(transactionBeans.get(0).getPayment_mode().equalsIgnoreCase("Cash")) {
            response=transactionViewUpdateDao.transactionViewByTrnNoAndPaymentModeByCash(transactionNo);
        }
        else if(transactionBeans.get(0).getPayment_mode().equalsIgnoreCase("Cheque")) {
            response=transactionViewUpdateDao.transactionViewByTrnNoAndPaymentModeByCheque(transactionNo);
        }
//        else {
//            response=transactionViewUpdateDao.transactionViewByTrnNoAndPaymentModeByCheque(transactionNo);
//
//        }

        return response;
    }

    @Override
    @Transactional(rollbackOn = BadRequestException.class)
    public void transactionModeUpdate(TransactionViewUpdateDTO transactionViewUpdateDTO) {
        try {
            List<PropertyTransactionBean> transactionMaster=cashTransactionDao.fetchTransactionBYTrnNo(transactionViewUpdateDTO.getTransaction_no());
            String captureOldPaymentMode=transactionMaster.get(0).getPayment_mode();
            ChequeDDCardTransactionBean transactionDetail = cashTransactionDetailDao.findReconUpdateById(transactionViewUpdateDTO.getId()).orElse(null);
            List<PropertyTransactionBean> propertyTransactionBeans = cashTransactionDao.fetchTransactionBYTrnNo(transactionViewUpdateDTO.getTransaction_no());
            Long count = cashTransactionDetailDao.transactionRecorCount(transactionViewUpdateDTO.getId());
            PropertyTransactionBean cashTransaction = new PropertyTransactionBean();
            log.info("transaction mode"+transactionViewUpdateDTO.getTransaction_mode());
            //propertyTransactionBeans.get(0).setPayment_mode(transactionViewUpdateDTO.getTransaction_mode());
            //cashTransactionDao.save(cashTransaction);
            if (captureOldPaymentMode.equalsIgnoreCase("Cash")) {
                PropertyTransactionBean transactionMstrBean=cashTransactionDao.fetchTransactionMasterByTransactionNoForCash(transactionViewUpdateDTO.getTransaction_no());
                if(transactionMstrBean !=null) {
                    transactionMstrBean.setPayment_mode(transactionViewUpdateDTO.getPayment_mode());
                    cashTransactionDao.save(transactionMstrBean);
                }
                else if(!transactionViewUpdateDTO.getPayment_mode().equalsIgnoreCase("Cash")) {
                    if(count==1) {
                        ChequeDDCardTransactionBean deleteDetails = cashTransactionDetailDao.findTransactionDetailsByTransactionId(transactionMaster.get(0).getId());
                        if(deleteDetails !=null) {
                            ChequeDDCardTransactionBean deleteRecord = cashTransactionDetailDao.fetchTransactionDetailsById(deleteDetails.getId());
                            cashTransactionDetailDao.delete(deleteRecord);
                        }

                    }
                    //else if(count==0) {
                        ChequeDDCardTransactionBean chequeDDCardTransactionBean = new ChequeDDCardTransactionBean();
                        chequeDDCardTransactionBean.setTransaction_id(transactionViewUpdateDTO.getId());
                        chequeDDCardTransactionBean.setProp_id(propertyTransactionBeans.get(0).getProp_id());
                        chequeDDCardTransactionBean.setCheque_no(transactionViewUpdateDTO.getCheque_no());
                        chequeDDCardTransactionBean.setCheque_dt(transactionViewUpdateDTO.getCheque_dt());
                        chequeDDCardTransactionBean.setBranch_name(transactionViewUpdateDTO.getBranch_name());
                        chequeDDCardTransactionBean.setAmount(transactionViewUpdateDTO.getPayable_amt().setScale(2, RoundingMode.CEILING));
                        if(transactionViewUpdateDTO.getPayment_mode().equalsIgnoreCase("Card")) {
                            chequeDDCardTransactionBean.setCard_no(Integer.valueOf(transactionViewUpdateDTO.getCard_no()));
                            chequeDDCardTransactionBean.setCard_holder_name(transactionViewUpdateDTO.getCard_holder_name());
                            chequeDDCardTransactionBean.setCard_type(transactionViewUpdateDTO.getCard_type());
                        }
                    if(!transactionViewUpdateDTO.getPayment_mode().equalsIgnoreCase("Card")) {
                        chequeDDCardTransactionBean.setCard_no(0);
                        chequeDDCardTransactionBean.setCard_holder_name("NA");
                        chequeDDCardTransactionBean.setCard_type("NA");
                    }

                        chequeDDCardTransactionBean.setCheck_status(0);
                        cashTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }
                }

                else{
                    PropertyTransactionBean transactionMstrBean=cashTransactionDao.getOne(transactionMaster.get(0).getId());
                    transactionMstrBean.setPayment_mode(transactionViewUpdateDTO.getPayment_mode());
                    cashTransactionDao.save(transactionMstrBean);
                    if(count > 0) {
                        if(transactionViewUpdateDTO.getPayment_mode().equalsIgnoreCase("Cash")) {
                            ChequeDDCardTransactionBean deleteDetails = cashTransactionDetailDao.findTransactionDetailsByTransactionId(transactionMaster.get(0).getId());
                            if(deleteDetails !=null) {
                                ChequeDDCardTransactionBean deleteRecord = cashTransactionDetailDao.fetchTransactionDetailsById(deleteDetails.getId());
                                cashTransactionDetailDao.delete(deleteRecord);
                            }
                        }

                        if(!transactionViewUpdateDTO.getPayment_mode().equalsIgnoreCase("Cash")) {
                            // here in Transaction Details table should be update not insert
                            ChequeDDCardTransactionBean updateDetails = cashTransactionDetailDao.findTransactionDetailsByTransactionId(transactionMaster.get(0).getId());
                            updateDetails.setTransaction_id(transactionViewUpdateDTO.getId());
                            updateDetails.setProp_id(propertyTransactionBeans.get(0).getProp_id());
                            updateDetails.setCheque_no(transactionViewUpdateDTO.getCheque_no());
                            updateDetails.setCheque_dt(transactionViewUpdateDTO.getCheque_dt());
                            updateDetails.setBranch_name(transactionViewUpdateDTO.getBranch_name());
                            updateDetails.setAmount(transactionViewUpdateDTO.getPayable_amt().setScale(2, RoundingMode.CEILING));
                            if (transactionViewUpdateDTO.getPayment_mode().equalsIgnoreCase("Card")) {
                                updateDetails.setCard_no(Integer.valueOf(transactionViewUpdateDTO.getCard_no()));
                                updateDetails.setCard_holder_name(transactionViewUpdateDTO.getCard_holder_name());
                                updateDetails.setCard_type(transactionViewUpdateDTO.getCard_type());
                            }
                            if (!transactionViewUpdateDTO.getPayment_mode().equalsIgnoreCase("Card")) {
                                updateDetails.setCard_no(0);
                                updateDetails.setCard_holder_name("NA");
                                updateDetails.setCard_type("NA");
                            }

                            updateDetails.setCheck_status(0);
                            cashTransactionDetailDao.save(updateDetails);
                        }
                    }
                    else if(count==0) {
                        //here insert the record in the transaction details no update
                        ChequeDDCardTransactionBean chequeDDCardTransactionBean = new ChequeDDCardTransactionBean();
                        chequeDDCardTransactionBean.setTransaction_id(transactionViewUpdateDTO.getId());
                        chequeDDCardTransactionBean.setProp_id(propertyTransactionBeans.get(0).getProp_id());
                        chequeDDCardTransactionBean.setCheque_no(transactionViewUpdateDTO.getCheque_no());
                        chequeDDCardTransactionBean.setCheque_dt(transactionViewUpdateDTO.getCheque_dt());
                        chequeDDCardTransactionBean.setBank_name(transactionViewUpdateDTO.getBank_name());
                        chequeDDCardTransactionBean.setBranch_name(transactionViewUpdateDTO.getBranch_name());
                        chequeDDCardTransactionBean.setAmount(transactionViewUpdateDTO.getPayable_amt().setScale(2, RoundingMode.CEILING));
                        if(transactionViewUpdateDTO.getPayment_mode().equalsIgnoreCase("Card")) {
                            chequeDDCardTransactionBean.setCard_no(Integer.valueOf(transactionViewUpdateDTO.getCard_no()));
                            chequeDDCardTransactionBean.setCard_holder_name(transactionViewUpdateDTO.getCard_holder_name());
                            chequeDDCardTransactionBean.setCard_type(transactionViewUpdateDTO.getCard_type());
                        }
                        if(!transactionViewUpdateDTO.getPayment_mode().equalsIgnoreCase("Card")) {
                            chequeDDCardTransactionBean.setCard_no(0);
                            chequeDDCardTransactionBean.setCard_holder_name("NA");
                            chequeDDCardTransactionBean.setCard_type("NA");
                        }

                        chequeDDCardTransactionBean.setCheck_status(0);
                        cashTransactionDetailDao.save(chequeDDCardTransactionBean);
                    }
                }
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    @Value("${aws.accessKeyId}")
    private String accessKey;
  @Value("${aws.secretKey}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;
    @Value("${aws.property.deactivate.buketName}")
    private String bucketName;
    @Transactional(rollbackOn = BadRequestException.class)
    @Override
    public void transactionDeactivate(MultipartFile file, Long propId, Long transactionId, Long userId, Long wardId, String reason,String stampdate)throws IOException {
        TransactionDeactiveBean transactionDeactiveBean1 = new TransactionDeactiveBean();

        String propertyNo = propertyMasterDao.findById(propId).get().getProperty_no();
        if (propertyNo != null) {

            //            LocalDateTime localDateTime = LocalDateTime.now();
//            File convertedFile = convertMultiPartFileToFile1(file);
//            String finalFileName = propId + "-" + file.getOriginalFilename().toString();
//            log.info("file name is..........." + finalFileName);
//            log.info("prop id name is..........." + propId);
//            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                    .withRegion(region) // replace with your preferred region
//                    .build();
//            PutObjectRequest putRequest = new PutObjectRequest(bucketName, finalFileName, convertedFile);
//            s3Client.putObject(putRequest);

            StringBuilder finalFileName = new StringBuilder();
            finalFileName.append(propertyNo);
            try {

                String fileName = UploadFile.AWS_Upload_With_Converted_File(accessKey, secretKey, region, bucketName, finalFileName.toString(), file);
                int count=cashTransactionDao.updateTransactionById(transactionId);
                if(count > 0) {
                    transactionDeactiveBean1.setTransaction_id(transactionId);
                    transactionDeactiveBean1.setProp_id(propId);
                    transactionDeactiveBean1.setWard_id(wardId);
                    transactionDeactiveBean1.setUser_id(userId);
                    transactionDeactiveBean1.setStampdate(stampdate);
                    transactionDeactiveBean1.setReason(reason);
                    transactionDeactiveBean1.setDocument(fileName);
                    transactionDeactivationDao.save(transactionDeactiveBean1);

                    List<PropertyCollectionMstrBean> collectionList = collectionDao.findCollectionByPropIdAndTransactionId(propId,transactionId);
                    if(!collectionList.isEmpty()) {
                        for (PropertyCollectionMstrBean collection : collectionList) {
                            collection.setStatus(0);
                            collectionDao.save(collection);
                            DemandDetailsBean demand=demandDao.findOneRecordByActivePayment(collection.getDemand_id());
                            if(demand !=null) {
                                demand.setPaid_status(0);
                                demandDao.save(demand);
                            }
                        }
                    }

                }
                else if(count <=0) {
                    throw new BadRequestException("Record not updated");
                }

                //deleteLastPayment
                //lastPaymentRecordDao.deleteLastPaymentRecord(lastPaymentRecordBean.get().getId());
            } catch (Exception e) {
                log.info(e.getMessage());
                throw new BadRequestException("No data found");
            }
            finally {
                DeleteFilesAndFolder.deleteFileFromCurrentDirectory(file.getOriginalFilename());
            }
        }
    }


   private File convertMultiPartFileToFile1(MultipartFile approval_letter) throws IOException {
       File convertedFile = new File(approval_letter.getOriginalFilename());
       FileUtils.writeByteArrayToFile(convertedFile, approval_letter.getBytes());
    return convertedFile;
  }
}
