package gov.municipal.suda.modules.property.service.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.*;
import gov.municipal.suda.modules.property.dao.transaction.*;
import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.master.FinYearBean;
import gov.municipal.suda.modules.property.model.master.OwnerDetailsBean;
import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import gov.municipal.suda.modules.property.model.transaction.*;
import gov.municipal.suda.util.DBFunctionCall;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.sql.rowset.CachedRowSet;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j

public class PaymentReceiptServiceImpl implements PaymentReceiptService{
   @Autowired
   PropertyMasterDao propertyMasterDao;
   @Autowired
   OwnerDetailsDao ownerDetailsDao;
   @Autowired
   LastPaymentRecordDao lastPaymentRecordDao;
   @Autowired
   OwnerTaxMasterDao ownerTaxMasterDao;
   @Autowired
   SAFARVDetailsDao safarvDetailsDao;
   @Autowired
   UsesTypeDao usesTypeDao;
   @Autowired
   CashTransactionDao cashTransactionDao;
   @Autowired
   DemandDao demandDao;
    @Autowired
    WardDao wardDao;
    @Autowired
    ARVDetailsDao arvDetailsDao;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    CashTransactionDetailDao cashTransactionDetailDao;


    @Autowired
    FinYearDao finYearDao;

    ObjectMapper mapper = new ObjectMapper();
    @Override
    public List<PaymentReceiptResponse> getPaymentReceipt(String property_no, String from_date, String to_date,String trn_no,String payment_mode) {
        List<PaymentReceiptResponse> receipt= new ArrayList<>();
        List<PayReceiptFloorDTO> floorList= new ArrayList<>();
        PaymentReceiptResponse response = new PaymentReceiptResponse();
        String cutFromYear=from_date.substring(0,4);
        log.info("CutFrom Year......."+cutFromYear);
        String cutToYear=to_date.substring(5,9);
        BigDecimal penality=new BigDecimal(0.00);
        BigDecimal pennaleCharge=new BigDecimal(0.00);
        BigDecimal rainHarvestTotal = new BigDecimal(0.00);
        BigDecimal propertyTax=new BigDecimal(0.00);
        BigDecimal apropertyTax=new BigDecimal(0.00);
        BigDecimal compositeTax=new BigDecimal(0.00);
        BigDecimal acompositeTax=new BigDecimal(0.00);
        BigDecimal educationCess=new BigDecimal(0.00);
        BigDecimal aeducationCess=new BigDecimal(0.00);
        Boolean isDifferenceAmountReceipt=Boolean.FALSE;
        BigDecimal adjustmentAmount=new BigDecimal(0.00);
        String receipt_no=null;
        String receipt_date=null;
        Long uses_type_id=0L;
        Long ward_id=0L;
        BigDecimal totalDifferenceAmount=new BigDecimal(0.00);
       // Boolean isDifferentAmountCase=false;
        BigDecimal differnce_penalty=new BigDecimal(0.00);
        LocalDate date = LocalDate.now();
        BigDecimal total_Amount=new BigDecimal(0.00);
        BigDecimal total_Amount_After_Discount=new BigDecimal(0.00);
        BigDecimal totalReciveableAmount= new BigDecimal(0.00);
        BigDecimal totalPayableAmount= new BigDecimal(0.00);
        Long prop_id=propertyMasterDao.findIdByPropNo(property_no);
        Long propertyTypeId=propertyMasterDao.findById(prop_id).get().getProperty_type_id();
        SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
        if(prop_id !=null) {
            List<FinYearBean> finYear=finYearDao.findAll();
            List<PropertyMasterBean> propertyMasterBeans=propertyMasterDao.fetchAllPropById(prop_id);
            String ward_name=wardDao.findWardNameById(propertyMasterBeans.get(0).getWard_id());
            String entry_type = propertyMasterBeans.get(0).getEntry_type();
            String uses_type_name=usesTypeDao.findUsesTypeNameById(propertyMasterBeans.get(0).getUsage_type_id());
            List<PropertyTransactionBean> propertyTransactionBeans=cashTransactionDao.fetchTransactionBYTrnNo(trn_no);
           
            Long transaction_id=propertyTransactionBeans.get(0).getId();
            String chkStatus=cashTransactionDetailDao.findChkStatusByTrnId(transaction_id);
            if(chkStatus==null||chkStatus.equals("")){
                chkStatus="0";
            }
            Long check_status=Long.parseLong(chkStatus);
            Optional<ChequeDDCardTransactionBean> chequeDDCardTransactionBeans=cashTransactionDetailDao.findReconUpdateById(transaction_id);

            response.setReceipt_no(trn_no); // need to be fetch
            response.setDate(String.valueOf(propertyTransactionBeans.get(0).getTransaction_date()).substring(0,10)); // need to be fetch
            response.setWard_no(ward_name);
            response.setProperty_no(property_no);
            response.setPropertyTypeId(propertyTypeId);
            response.setUses_type_name(uses_type_name);
            response.setTotal_builtup_area(propertyMasterBeans.get(0).getTotalbuilbup_area());
            List<OwnerDetailsBean> ownerDetail = ownerDetailsDao.findOwnerDetails(prop_id);
            response.setOwner_name(ownerDetail.get(0).getOwner_name());
            response.setAddress(ownerDetail.get(0).getOwner_address());
            response.setMobile_no(ownerDetail.get(0).getMobile_no().toString());
            Optional<List<ARVDetailsBean>> floorDetails = arvDetailsDao.getArvByPropAndYear(prop_id, from_date);
            List<ARVDetailsBean> floorDetailsResult = floorDetails.isPresent() ? floorDetails.get() : null;
           if(floorDetailsResult !=null) {
               for(ARVDetailsBean floor : floorDetailsResult) {
                   PayReceiptFloorDTO dto = new PayReceiptFloorDTO();
                   dto.setBuilt_up_area(floor.getBuilt_up_area());
                   Long uses_id=floor.getUsage_type_id();
                   String usesName=usesTypeDao.findById(uses_id).get().getUses_type_name();
                   dto.setUsage(usesName);
                   dto.setFloor_name(floor.getFloor_name());
                   floorList.add(dto);
                   response.setFloor_details(floorList);
               }

           }
           else if(floorDetailsResult==null) {
               throw new BadRequestException("Please check floor details in arv details table against the property no and effective date");
           }

            Integer differenceAmountDemandCount=demandDao.countDifferenceAmountDemandByPropId(prop_id,cutFromYear,cutToYear);
            Optional<List<DemandDetailsBean>> demandResult=Optional.empty();
            List<DemandDetailsBean> demand=new ArrayList<>();
            Optional<List<DemandDetailsBean>> adjustmentAmountResult=Optional.empty();
            List<DemandDetailsBean> adjustmentAmountList=new ArrayList<>();
            Optional<List<DemandDetailsBean>> totalDiffAmount =Optional.empty();
            List<DemandDetailsBean> differenceAmount=new ArrayList<>();

            if(differenceAmountDemandCount > 0) {
               isDifferenceAmountReceipt=Boolean.TRUE;
           }
           if(Boolean.TRUE.equals(isDifferenceAmountReceipt)) {
               adjustmentAmountResult=demandDao.showPaidDemandDetailsForPaymentReceiptForAdjustmentAmount(prop_id, cutFromYear, cutToYear);
               adjustmentAmountList=adjustmentAmountResult.isPresent()?adjustmentAmountResult.get(): null;
               if(adjustmentAmountList !=null) {
                   adjustmentAmount=adjustmentAmountList.stream().map(r -> r.getTotal_amount()).reduce(BigDecimal::add).get();
               }

               totalDiffAmount=demandDao.findDifferenceAmount(prop_id);
                differenceAmount=totalDiffAmount.isPresent()?totalDiffAmount.get():null;
               if(differenceAmount !=null) {
                   if(differenceAmount.size() > 0) {

                       for(DemandDetailsBean differ : differenceAmount) {
                           differnce_penalty=differnce_penalty.add(differ.getOthPenalty());
                           totalDifferenceAmount=totalDifferenceAmount.add(differ.getOthTotAmount()).add(differ.getOthPenalty());

                       }

                   }
               }
               demandResult = demandDao.showPaidDemandDetailsForPaymentReceiptForDifferenceAmount(prop_id, cutFromYear, cutToYear);
               demand = demandResult.isPresent()? demandResult.get(): null;
           }
           else if(Boolean.FALSE.equals(isDifferenceAmountReceipt)) {
               demandResult = demandDao.showPaidDemandDetailsForPaymentReceipt(prop_id, cutFromYear, cutToYear);
               demand = demandResult.isPresent()? demandResult.get(): null;
           }

        if(demandResult.isPresent()) {
            //List<DemandDetailsBean> demand1 = demand.stream().filter(e -> e.getDemand_type().equals("New Assessment")).collect(Collectors.toList());
            List<PaymentReceiptDetailsDTO> listPaymentDetails = new ArrayList<>();
            PaymentReceiptDetailsDTO paymentDetails = new PaymentReceiptDetailsDTO();
            String currentFinYear=finYear.get(finYear.size()-1).getFy_name();
            String previousYearGetEffectYear=null;
            String previousYearSingleEffectYear=null;
            String currentYearEffectYear=null;
            String currentEffectYearCombine=null;
            List<String> getPreviousEffectYear=null;

            for (DemandDetailsBean result : demand) {

                if(!result.getEffect_year().equals(currentFinYear)) {
                    if(demand.size()>1) {
                        getPreviousEffectYear = demand.stream().filter(v -> !v.getEffect_year().equals(currentFinYear)).map(r -> r.getEffect_year()).collect(Collectors.toList());
                        previousYearGetEffectYear = getPreviousEffectYear.get(0).substring(0,4) + " To " + getPreviousEffectYear.get(getPreviousEffectYear.size() - 1).substring(5,9);
                        currentYearEffectYear=demand.stream().filter(v-> v.getEffect_year().equals(currentFinYear)).map(r-> r.getEffect_year()).findAny().orElse(null);
                        currentEffectYearCombine=currentYearEffectYear;
                    }
                    else if(demand.size()==1) {
                        previousYearSingleEffectYear=demand.stream().filter(v -> !v.getEffect_year().equals(currentFinYear)).map(r -> r.getEffect_year()).findAny().orElse(null);
                        previousYearGetEffectYear=previousYearSingleEffectYear;
                    }
                    apropertyTax=apropertyTax.add(result.getAproperty_tax());
                    aeducationCess=aeducationCess.add(result.getAeducation_cess());
                    acompositeTax=acompositeTax.add(result.getAcomposite_tax());
                }
                else if(result.getEffect_year().equals(currentFinYear)) {
                    currentYearEffectYear=demand.stream().filter(v-> v.getEffect_year().equals(currentFinYear)).map(r-> r.getEffect_year()).findAny().orElse(null);
                    currentEffectYearCombine=currentYearEffectYear;
                    propertyTax=propertyTax.add(result.getProperty_tax());
                    educationCess=educationCess.add(result.getEducation_cess());
                    compositeTax=compositeTax.add(result.getComposite_tax());
                }

                penality = penality.add(result.getPenalty());
                pennaleCharge = pennaleCharge.add(result.getPenal_charge());
                rainHarvestTotal = rainHarvestTotal.add(result.getRain_harvest_charge());
                total_Amount = total_Amount.add(result.getTotal_amount());
                total_Amount_After_Discount= total_Amount_After_Discount.add(result.getFinalAmountAfterDiscount());

            }
            paymentDetails.setPreviousEffectYear(previousYearGetEffectYear);
            paymentDetails.setCurrentEffectYear(currentEffectYearCombine);
            paymentDetails.setProperty_tax(propertyTax.setScale(0, RoundingMode.HALF_UP));
            paymentDetails.setProperty_tax_arrear(apropertyTax.setScale(0, RoundingMode.HALF_UP));
            paymentDetails.setSamerik_kar(compositeTax);
            paymentDetails.setSmerik_kar_arrear(acompositeTax);
            paymentDetails.setEducation_cess_arrear(aeducationCess.setScale(0, RoundingMode.HALF_UP));
            paymentDetails.setEducation_cess(educationCess.setScale(0, RoundingMode.HALF_UP));
            listPaymentDetails.add(paymentDetails);
            response.setPayment_details(listPaymentDetails);
        }
            response.setPenal_charge(pennaleCharge.setScale(0, RoundingMode.HALF_UP));
            if(Boolean.TRUE.equals(isDifferenceAmountReceipt)) {
                response.setPenalty_amount(differnce_penalty.setScale(0, RoundingMode.HALF_UP));
               // totalReciveableAmount=total_Amount.subtract(adjustmentAmount).add(differnce_penalty).add(BigDecimal.valueOf(3.00)).setScale(0, RoundingMode.HALF_UP);
                totalReciveableAmount=total_Amount.subtract(adjustmentAmount).add(BigDecimal.valueOf(3.00)).setScale(0, RoundingMode.HALF_UP);

                totalPayableAmount=totalReciveableAmount;
            }
            else if(Boolean.FALSE.equals(isDifferenceAmountReceipt)) {
                response.setPenalty_amount(penality.setScale(0, RoundingMode.HALF_UP));
                //totalReciveableAmount=total_Amount.add(penality).add(BigDecimal.valueOf(3.00)).setScale(0, RoundingMode.HALF_UP);
//                totalReciveableAmount=total_Amount.add(BigDecimal.valueOf(3.00)).setScale(0, RoundingMode.HALF_UP);
                totalReciveableAmount = total_Amount;
                totalPayableAmount=total_Amount_After_Discount;
            }

            response.setForm_fee(BigDecimal.valueOf(5.00));
            response.setPayable_amount(total_Amount_After_Discount);
            response.setTotal(total_Amount.setScale(0, RoundingMode.HALF_UP));
            response.setAdjustment_amount(adjustmentAmount.setScale(0, RoundingMode.HALF_UP));
            response.setReceivable_amount(total_Amount_After_Discount.setScale(0, RoundingMode.HALF_UP));
            response.setAccount_description("Holding Tax & Others"); // need to verify and ask
            response.setDepartment_section("Revenue Section");
            response.setMode_of_payment(payment_mode);
            
            response.setDiference_amount(totalDifferenceAmount.setScale(0, RoundingMode.HALF_UP));
            response.setCheck_status(check_status);
            if(chequeDDCardTransactionBeans.isPresent()) {
                if(payment_mode.equalsIgnoreCase("Cheque") ||
                        payment_mode.equalsIgnoreCase("DD")) {
                    if(chequeDDCardTransactionBeans.get().getCheck_status()==1){
                        Instant localDateTime=chequeDDCardTransactionBeans.get()
                                .getClear_stampdate().toInstant();
                        response.setDrawn_on(localDateTime.atZone(ZoneId.of("UTC")).toLocalDate().toString());
                    }
                }
                response.setBank_name(chequeDDCardTransactionBeans.get().getBank_name());
                response.setBranch_location(chequeDDCardTransactionBeans.get().getBranch_name());
                response.setCheck_no(chequeDDCardTransactionBeans.get().getCheque_no());
                response.setCheck_dt(chequeDDCardTransactionBeans.get().getCheque_dt());
                response.setCard_holder_name(chequeDDCardTransactionBeans.get().getCard_holder_name());
                response.setCard_type(chequeDDCardTransactionBeans.get().getCard_type());
            }
            receipt.add(response);
        }
        if(prop_id==null) {
            throw new BadRequestException("Property Id shouldn't be blank");
        }
        log.info("reciept Details ----- Line 285 ---- {}",receipt);
        return receipt;
    }

    @Override
    public List<BulkPaymentReceipt> getPaymentReceiptByTC(String frmDate, String toDate,String user_id) {

        List<FinYearBean> finYearList = finYearDao.findAll();
        String currentFinYear = finYearList.get(finYearList.size()-1).getFy_name();

        List<BulkPaymentReceipt>  results =new ArrayList<>();
        Set<BulkPaymentReceipt> tempSets = new HashSet<>();
        Map<Object, Object> inputParameter=new LinkedHashMap<>();
        Long userId=null;
        if(user_id.equalsIgnoreCase("All")) {
            userId=0L;
        }
        else {
            userId=Long.parseLong(user_id);
        }
        if(null!=currentFinYear) {
            inputParameter.put(frmDate, frmDate);
            inputParameter.put(toDate, toDate);
            inputParameter.put(userId, userId);
            inputParameter.put(currentFinYear,currentFinYear);
            log.info(String.valueOf(inputParameter.size()));

            try {
                CachedRowSet crs = DBFunctionCall.simpleFunctionCallWithParameter("public.get_bulk_receipt_report(?,?,?,?)", 1, inputParameter);
                log.info("crs {} ",crs);
                while (crs.next()) {
                    BulkPaymentReceipt dto = new BulkPaymentReceipt();
                    dto.setReceipt_no(crs.getString(1));
                    dto.setDepartment_section(crs.getString(2));
                    dto.setAccount_description(crs.getString(3));
                    dto.setWard_no(crs.getString(4));
                    dto.setDate(crs.getString(5));
                    dto.setProperty_no(crs.getString(6));
                    dto.setPropertyTypeId(crs.getLong(7));
                    dto.setOwner_name(crs.getString(8));
                    dto.setAddress(crs.getString(9));
                    dto.setMobile_no(String.valueOf(crs.getLong(10)));
                    dto.setTotal_builtup_area(crs.getBigDecimal(11));
                    List<PayReceiptFloorDTO> floorDetailsByGroupingList=mapper.readValue(crs.getString(12),
                            TypeFactory.defaultInstance().constructCollectionType(List.class,PayReceiptFloorDTO.class));

                    List<PaymentReceiptDetailsDTO> previousYearList=mapper.readValue(crs.getString(13),
                            TypeFactory.defaultInstance().constructCollectionType(List.class,PaymentReceiptDetailsDTO.class));
                    List<PaymentReceiptDetailsDTO> currentYearList=mapper.readValue(crs.getString(14),
                            TypeFactory.defaultInstance().constructCollectionType(List.class,PaymentReceiptDetailsDTO.class));


                    List<PayReceiptFloorDTO> finalFloorDetailsAfterGrouping = new ArrayList<>();

                    for (PayReceiptFloorDTO floorDTO : floorDetailsByGroupingList) {
                       if(crs.getString(1).equals(floorDTO.getTransaction_no())){
                           PayReceiptFloorDTO setInDto = new PayReceiptFloorDTO();
                           setInDto.setUsage(floorDTO.getUsage());
                           setInDto.setFloor_name(floorDTO.getFloor_name());
                           setInDto.setBuilt_up_area(floorDTO.getBuilt_up_area());
                           setInDto.setTransaction_no(floorDTO.getTransaction_no());
                           finalFloorDetailsAfterGrouping.add(setInDto);

                       }
                    }

                    for(PaymentReceiptDetailsDTO paymentReceiptDTO: previousYearList) {
                        if(crs.getString(1).equals(paymentReceiptDTO.getTransaction_no())){
                            PaymentReceiptDetailsDTO previousYearDTO =new PaymentReceiptDetailsDTO();
                            previousYearDTO.setCurrentEffectYear(paymentReceiptDTO.getPreviousEffectYear());
                            previousYearDTO.setPreviousEffectYear(paymentReceiptDTO.getCurrentEffectYear());
                            previousYearDTO.setSamerik_kar(paymentReceiptDTO.getSamerik_kar());
                            previousYearDTO.setProperty_tax(paymentReceiptDTO.getProperty_tax());
                            previousYearDTO.setEducation_cess(paymentReceiptDTO.getEducation_cess());
                            previousYearDTO.setTransaction_no(paymentReceiptDTO.getTransaction_no());
                            previousYearDTO.setProperty_tax_arrear(paymentReceiptDTO.getProperty_tax_arrear());
                            previousYearDTO.setEducation_cess_arrear(paymentReceiptDTO.getEducation_cess_arrear());
                            previousYearDTO.setSmerik_kar_arrear(paymentReceiptDTO.getSmerik_kar_arrear());
                            dto.setPrevious_year(previousYearDTO);

                        }
                    }

                    for(PaymentReceiptDetailsDTO paymentReceiptDTO: currentYearList) {
                        if(crs.getString(1).equals(paymentReceiptDTO.getTransaction_no())){
                            PaymentReceiptDetailsDTO currentYearDTO =new PaymentReceiptDetailsDTO();
                            currentYearDTO.setCurrentEffectYear(paymentReceiptDTO.getPreviousEffectYear());
                            currentYearDTO.setPreviousEffectYear(paymentReceiptDTO.getCurrentEffectYear());
                            currentYearDTO.setSamerik_kar(paymentReceiptDTO.getSamerik_kar());
                            currentYearDTO.setProperty_tax(paymentReceiptDTO.getProperty_tax());
                            currentYearDTO.setEducation_cess(paymentReceiptDTO.getEducation_cess());
                            currentYearDTO.setTransaction_no(paymentReceiptDTO.getTransaction_no());
                            currentYearDTO.setProperty_tax_arrear(paymentReceiptDTO.getProperty_tax_arrear());
                            currentYearDTO.setEducation_cess_arrear(paymentReceiptDTO.getEducation_cess_arrear());
                            currentYearDTO.setSmerik_kar_arrear(paymentReceiptDTO.getSmerik_kar_arrear());
                            dto.setCurrent_year(currentYearDTO);

                        }
                    }
                   /* Map<String,List<PayReceiptFloorDTO>> grouped=floorDetailsByGrouping.stream().collect(Collectors.groupingBy(value ->
                            value.getUsage() + " " +value.getFloor_name()));

                    List<PayReceiptFloorDTO> finalFloorDetailsAfterGrouping=grouped.values().stream()
                                    .map(group -> {
                                        return group.stream().reduce((obj1,obj2)-> {
                                           BigDecimal sum=new BigDecimal(obj1.getBuilt_up_area()).add(new BigDecimal(obj2.getBuilt_up_area()));
                                           obj1.setBuilt_up_area(String.valueOf(sum));
                                           return obj1;
                                        }).get();
                                    }).collect(Collectors.toList());*/

                    dto.setFloor_details(finalFloorDetailsAfterGrouping);
                   // dto.setPrevious_year(mapper.readValue(crs.getString(13),PaymentReceiptDetailsDTO.class));
                    //dto.setCurrent_year(mapper.readValue(crs.getString(14),PaymentReceiptDetailsDTO.class));
                    dto.setPayable_amount(crs.getBigDecimal(15));
                    dto.setMode_of_payment(crs.getString(16));
                    dto.setBank_name(crs.getString(17));
                    dto.setBranch_location(crs.getString(18));
                    dto.setEffect_year(crs.getString(19));
                    dto.setPenal_charge(crs.getBigDecimal(20));
                    dto.setForm_fee(crs.getBigDecimal(21));
                    dto.setPenalty_amount(crs.getBigDecimal(22));
                    dto.setAdjustment_amount(crs.getBigDecimal(23));
                    dto.setDiference_amount(crs.getBigDecimal(24));
                    dto.setTotal(crs.getBigDecimal(25));
                    dto.setRain_water_harvesting(crs.getBigDecimal(26));
                    dto.setReceivable_amount(crs.getBigDecimal(27));
                    dto.setUses_type_name(crs.getString(28));
                    dto.setCheck_no(crs.getString(29));
                    dto.setCheck_dt(crs.getString(30));
                    dto.setCard_holder_name(crs.getString(31));
                    dto.setCard_type(crs.getString(32));
                    dto.setCheck_status(crs.getLong(33));

                    tempSets.add(dto);

                }
                log.info("results.addAll(tempSets) {} ",results.addAll(tempSets));     
                results.addAll(tempSets); //remove duplicate with the help of Sets, but I think this approach is not work for
                // the JSON object (in future need to try with some other approach)
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
        //log.info(results.toString());
        return results;


       // DBFunctionCall.simpleFunctionCallWithParameter();

        /*Timestamp todayStart = Timestamp.valueOf(frmDate + " 00:00:00.00");
        Timestamp todayEnd = Timestamp.valueOf(toDate + " 23:59:59.999999999");
        List<PropertyTransactionBean> receiptList = cashTransactionDao.fetchTransactionByUserIdDate(userId, todayStart, todayEnd);
        for (PropertyTransactionBean prtyTrnBean : receiptList) {
            List<PayReceiptFloorDTO> floorList= new ArrayList<>();
            PaymentReceiptResponse response = new PaymentReceiptResponse();
            BigDecimal penality=new BigDecimal(0.00);
            BigDecimal pennaleCharge=new BigDecimal(0.00);
            BigDecimal rainHarvestTotal = new BigDecimal(0.00);
            BigDecimal propertyTax=new BigDecimal(0.00);
            BigDecimal apropertyTax=new BigDecimal(0.00);
            BigDecimal compositeTax=new BigDecimal(0.00);
            BigDecimal acompositeTax=new BigDecimal(0.00);
            BigDecimal educationCess=new BigDecimal(0.00);
            BigDecimal aeducationCess=new BigDecimal(0.00);
            Boolean isDifferenceAmountReceipt=Boolean.FALSE;
            BigDecimal adjustmentAmount=new BigDecimal(0.00);
            String receipt_no=null;
            String receipt_date=null;
            Long uses_type_id=0L;
            Long ward_id=0L;
            BigDecimal totalDifferenceAmount=new BigDecimal(0.00);
            // Boolean isDifferentAmountCase=false;
            BigDecimal differnce_penalty=new BigDecimal(0.00);
            LocalDate date = LocalDate.now();
            BigDecimal total_Amount=new BigDecimal(0.00);
            BigDecimal totalReciveableAmount= new BigDecimal(0.00);
            BigDecimal totalPayableAmount= new BigDecimal(0.00);
            Long prop_id=prtyTrnBean.getProp_id();
            Long propertyTypeId=propertyMasterDao.findById(prop_id).get().getProperty_type_id();
            SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
            if(prop_id !=null) {
                List<FinYearBean> finYear=finYearDao.findAll();
                List<PropertyMasterBean> propertyMasterBeans=propertyMasterDao.fetchAllPropById(prop_id);
                String ward_name=wardDao.findWardNameById(propertyMasterBeans.get(0).getWard_id());
                String entry_type = propertyMasterBeans.get(0).getEntry_type();
                String uses_type_name=usesTypeDao.findUsesTypeNameById(propertyMasterBeans.get(0).getUsage_type_id());
                //ward_id = propertyMasterDao.findById(prop_id).get().getWard_id();
                //Optional<List<LastPaymentRecordBean>> lastPaymentResults = lastPaymentRecordDao.getLastPaymentBetweenTheDate(prop_id);
                //List<LastPaymentRecordBean> lastPaymentRecord = lastPaymentResults.isPresent() ? lastPaymentResults.get() : null;
                receipt_no = prtyTrnBean.getTransaction_no();;
                List<PropertyTransactionBean> propertyTransactionBeans=cashTransactionDao.fetchTransactionBYTrnNo(receipt_no);
                Long transaction_id=propertyTransactionBeans.get(0).getId();
                String chkStatus=cashTransactionDetailDao.findChkStatusByTrnId(transaction_id);
                if(chkStatus==null||chkStatus.equals("")){
                    chkStatus="0";
                }
                Long check_status=Long.parseLong(chkStatus);
                Optional<ChequeDDCardTransactionBean> chequeDDCardTransactionBeans=cashTransactionDetailDao.findReconUpdateById(transaction_id);
                response.setReceipt_no(receipt_no);
                //String cutFromYear = lastPaymentRecord.get(0).getFrm_year().substring(0, 4);
                //log.info("CutFrom Year......." + cutFromYear);
               // String cutToYear = lastPaymentRecord.get(0).getUpto_year().substring(5, 9);
                //log.info("CutFrom Year......." + cutToYear);
                //if (lastPaymentResults.isPresent()) {
                    //log.info("inside lastpyamentRecords");
                   // receipt_date = lastPaymentRecord.get(0).getReceipt_date();

                //}

                response.setDate(prtyTrnBean.getTransaction_date().toString());
                response.setWard_no(ward_name);
                response.setProperty_no(propertyMasterBeans.get(0).getProperty_no());
                response.setPropertyTypeId(propertyTypeId);
                response.setUses_type_name(uses_type_name);
                response.setTotal_builtup_area(propertyMasterBeans.get(0).getTotalbuilbup_area());
                List<OwnerDetailsBean> ownerDetail = ownerDetailsDao.findOwnerDetails(prop_id);
                response.setOwner_name(ownerDetail.get(0).getOwner_name());
                response.setAddress(ownerDetail.get(0).getOwner_address());
                response.setMobile_no(ownerDetail.get(0).getMobile_no().toString());
                log.info("Prop Id............."+prop_id);
                //log.info("Effect year............."+lastPaymentRecord.get(0).getFrm_year());
                Optional<List<ARVDetailsBean>> floorDetails = arvDetailsDao.getArvByPropAndYear(prop_id, frmDate.substring(0,4));
                List<ARVDetailsBean> floorDetailsResult = floorDetails.isPresent() ? floorDetails.get() : null;
                if(floorDetailsResult !=null) {
                    for(ARVDetailsBean floor : floorDetailsResult) {
                        PayReceiptFloorDTO dto = new PayReceiptFloorDTO();
                        dto.setBuilt_up_area(floor.getBuilt_up_area());
                        Long uses_id=floor.getUsage_type_id();
                        String usesName=usesTypeDao.findById(uses_id).get().getUses_type_name();
                        dto.setUsage(usesName);
                        dto.setFloor_name(floor.getFloor_name());
                        floorList.add(dto);
                        response.setFloor_details(floorList);
                    }

                }

                Integer differenceAmountDemandCount=demandDao.countDifferenceAmountDemandByPropId(prop_id,frmDate.substring(0,6),toDate.substring(0,6));
                Optional<List<DemandDetailsBean>> demandResult=Optional.empty();
                List<DemandDetailsBean> demand=new ArrayList<>();
                Optional<List<DemandDetailsBean>> adjustmentAmountResult=Optional.empty();
                List<DemandDetailsBean> adjustmentAmountList=new ArrayList<>();
                Optional<List<DemandDetailsBean>> totalDiffAmount =Optional.empty();
                List<DemandDetailsBean> differenceAmount=new ArrayList<>();

                if(differenceAmountDemandCount > 0) {
                    isDifferenceAmountReceipt=Boolean.TRUE;
                }
                if(Boolean.TRUE.equals(isDifferenceAmountReceipt)) {
                    adjustmentAmountResult=demandDao.showPaidDemandDetailsForPaymentReceiptForAdjustmentAmount(prop_id,frmDate.substring(0,6),toDate.substring(0,6));
                    adjustmentAmountList=adjustmentAmountResult.isPresent()?adjustmentAmountResult.get(): null;
                    if(adjustmentAmountList !=null) {
                        adjustmentAmount=adjustmentAmountList.stream().map(r -> r.getTotal_amount()).reduce(BigDecimal::add).get();
                    }

                    totalDiffAmount=demandDao.findDifferenceAmount(prop_id);
                    differenceAmount=totalDiffAmount.isPresent()?totalDiffAmount.get():null;
                    if(differenceAmount !=null) {
                        if(!differenceAmount.isEmpty()) {

                            for(DemandDetailsBean differ : differenceAmount) {
                                differnce_penalty=differnce_penalty.add(differ.getOthPenalty());
                                totalDifferenceAmount=totalDifferenceAmount.add(differ.getOthTotAmount()).add(differ.getOthPenalty());

                            }

                        }
                    }
                    demandResult = demandDao.showPaidDemandDetailsForPaymentReceiptForDifferenceAmount(prop_id, frmDate.substring(0,6),toDate.substring(0,6));
                    demand = demandResult.isPresent()? demandResult.get(): null;
                }
                else if(Boolean.FALSE.equals(isDifferenceAmountReceipt)) {
                    demandResult = demandDao.showPaidDemandDetailsForPaymentReceipt(prop_id, frmDate.substring(0,6),toDate.substring(0,6));
                    demand = demandResult.isPresent()? demandResult.get(): null;
                }

                if(demandResult.isPresent()) {
                    //List<DemandDetailsBean> demand1 = demand.stream().filter(e -> e.getDemand_type().equals("New Assessment")).collect(Collectors.toList());
                    List<PaymentReceiptDetailsDTO> listPaymentDetails = new ArrayList<>();
                    PaymentReceiptDetailsDTO paymentDetails = new PaymentReceiptDetailsDTO();
                    String currentFinYear=finYear.get(finYear.size()-1).getFy_name();
                    String previousYearGetEffectYear=null;
                    String previousYearSingleEffectYear=null;
                    String currentYearEffectYear=null;
                    String currentEffectYearCombine=null;
                    List<String> getPreviousEffectYear=null;

                    if(!demand.isEmpty()) {
                    for (DemandDetailsBean result : demand) {

                        if (!result.getEffect_year().equals(currentFinYear)) {
                            if (demand.size() > 1) {
                                getPreviousEffectYear = demand.stream().filter(v -> !v.getEffect_year().equals(currentFinYear)).map(r -> r.getEffect_year()).collect(Collectors.toList());
                                previousYearGetEffectYear = getPreviousEffectYear.get(0).substring(0, 4) + " To " + getPreviousEffectYear.get(getPreviousEffectYear.size() - 1).substring(5, 9);
                                currentYearEffectYear = demand.stream().filter(v -> v.getEffect_year().equals(currentFinYear)).map(r -> r.getEffect_year()).findFirst().orElse(null);
                                currentEffectYearCombine = currentYearEffectYear;
                            } else if (demand.size() == 1) {
                                previousYearSingleEffectYear = demand.stream().filter(v -> !v.getEffect_year().equals(currentFinYear)).map(r -> r.getEffect_year()).findFirst().get();
                                previousYearGetEffectYear = previousYearSingleEffectYear;
                            }
                            apropertyTax = apropertyTax.add(result.getAproperty_tax());
                            aeducationCess = aeducationCess.add(result.getAeducation_cess());
                            acompositeTax = acompositeTax.add(result.getAcomposite_tax());
                        } else if (result.getEffect_year().equals(currentFinYear)) {
                            currentYearEffectYear = demand.stream().filter(v -> v.getEffect_year().equals(currentFinYear)).map(r -> r.getEffect_year()).findFirst().get();
                            currentEffectYearCombine = currentYearEffectYear;
                            propertyTax = propertyTax.add(result.getProperty_tax());
                            educationCess = educationCess.add(result.getEducation_cess());
                            compositeTax = compositeTax.add(result.getComposite_tax());
                        }

                        penality = penality.add(result.getPenalty());
                        pennaleCharge = pennaleCharge.add(result.getPenal_charge());
                        rainHarvestTotal = rainHarvestTotal.add(result.getRain_harvest_charge());
                        total_Amount = total_Amount.add(result.getTotal_amount());
                    }

                    }
                    paymentDetails.setPreviousEffectYear(previousYearGetEffectYear);
                    paymentDetails.setCurrentEffectYear(currentEffectYearCombine);
                    paymentDetails.setProperty_tax(propertyTax.setScale(0, RoundingMode.HALF_UP));
                    paymentDetails.setProperty_tax_arrear(apropertyTax.setScale(0, RoundingMode.HALF_UP));
                    paymentDetails.setSamerik_kar(compositeTax);
                    paymentDetails.setSmerik_kar_arrear(acompositeTax);
                    paymentDetails.setEducation_cess_arrear(aeducationCess.setScale(0, RoundingMode.HALF_UP));
                    paymentDetails.setEducation_cess(educationCess.setScale(0, RoundingMode.HALF_UP));
                    listPaymentDetails.add(paymentDetails);
                    response.setPayment_details(listPaymentDetails);
                }
                response.setPenal_charge(pennaleCharge.setScale(0, RoundingMode.HALF_UP));
                if(Boolean.TRUE.equals(isDifferenceAmountReceipt)) {
                    response.setPenalty_amount(differnce_penalty.setScale(0, RoundingMode.HALF_UP));
                    totalReciveableAmount=total_Amount.subtract(adjustmentAmount).add(differnce_penalty).add(BigDecimal.valueOf(3.00)).setScale(0, RoundingMode.HALF_UP);
                    totalPayableAmount=totalReciveableAmount;
                }
                else if(Boolean.FALSE.equals(isDifferenceAmountReceipt)) {
                    response.setPenalty_amount(penality.setScale(0, RoundingMode.HALF_UP));
                    totalReciveableAmount=total_Amount.add(penality).add(BigDecimal.valueOf(3.00)).setScale(0, RoundingMode.HALF_UP);
                    totalPayableAmount=totalReciveableAmount.setScale(0, RoundingMode.HALF_UP);
                }

                response.setForm_fee(BigDecimal.valueOf(3.00));
                response.setPayable_amount(totalPayableAmount);
                response.setTotal(total_Amount.setScale(0, RoundingMode.HALF_UP));
                response.setAdjustment_amount(adjustmentAmount.setScale(0, RoundingMode.HALF_UP));
                response.setReceivable_amount(totalReciveableAmount.setScale(0, RoundingMode.HALF_UP));
                response.setAccount_description("Holding Tax & Others"); // need to verify and ask
                response.setDepartment_section("Revenue Section");
                response.setMode_of_payment(prtyTrnBean.getPayment_mode());
                response.setRain_water_harvesting(rainHarvestTotal.setScale(0, RoundingMode.HALF_UP));
                response.setDiference_amount(totalDifferenceAmount.setScale(0, RoundingMode.HALF_UP));
                response.setCheck_status(check_status);
                if(chequeDDCardTransactionBeans.isPresent()) {
                    response.setBank_name(chequeDDCardTransactionBeans.get().getBank_name());
                    response.setBranch_location(chequeDDCardTransactionBeans.get().getBranch_name());
                    response.setCheck_no(chequeDDCardTransactionBeans.get().getCheque_no());
                    response.setCheck_dt(chequeDDCardTransactionBeans.get().getCheque_dt());
                    response.setCard_holder_name(chequeDDCardTransactionBeans.get().getCard_holder_name());
                    response.setCard_type(chequeDDCardTransactionBeans.get().getCard_type());
                }
                receipt.add(response);
            }
            if(prop_id==null) {
                throw new BadRequestException("Property Id should'nt be blank");
            }
        }*/


    }
}
