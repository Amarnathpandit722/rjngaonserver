package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.*;
import gov.municipal.suda.modules.property.dao.transaction.DemandDao;
import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.master.*;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class DemandServiceImpl implements DemandService {
    @Autowired
    DemandDao demandDao;
    @Autowired
    PropertyMasterDao propertyMasterDao;

    @Autowired
    RainHarvestRateDao rainHarvestRateDao;
    @Autowired
    FinYearDao finYearDao;
    @Autowired
    LastPaymentRecordDao lastPaymentRecordDao;

    @Autowired
    OwnerTaxMasterDao ownerTaxMasterDao;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    ArvRangeDao arvRangeDao;
    @Autowired
    ArvRateDao arvRateDao;

    @Override
    public String createDemand(List<DemandRequestDto> demandRequest, Long user_id) throws Exception { 
        log.info("Holding No{} ", demandRequest.size());


        if (demandRequest != null || demandRequest.size() > 0) {

            for (DemandRequestDto demandRequestDto : demandRequest) {
                Long prop_id = 0L;
                Long ward_id = 0L;
                Integer effect_year_Int = Integer.parseInt(demandRequestDto.getEffect_year().substring(0, 4));
                List<FinYearBean> allRecords = finYearDao.findAll();
                FinYearBean finID = finYearDao.getFinIdByFinYear(demandRequestDto.getEffect_year());
                String currentFinYear = allRecords.get(allRecords.size() - 1).getFy_name();

                DemandDetailsBean demandDetailsBean = new DemandDetailsBean();
                RainHarvestRateMasterBean rainHarvestRate = null;
                String isHarvest = null;
                BigDecimal property_tax = new BigDecimal(0);
                BigDecimal sanitation_tax = new BigDecimal(0);
                BigDecimal composite_tax = new BigDecimal(0);
                BigDecimal common_wtr_tax = new BigDecimal(0);
                BigDecimal personal_wtr_tax = new BigDecimal(0);
                BigDecimal rain_harvest_charge = new BigDecimal(0);
                BigDecimal education_cess = new BigDecimal(0);
                BigDecimal penalty = new BigDecimal(0);
                BigDecimal penal_charge = new BigDecimal(0);
                BigDecimal othramt = new BigDecimal(0);
                BigDecimal total_amount = new BigDecimal(0);
                BigDecimal asanitation_tax = new BigDecimal(0);
                BigDecimal acomposite_tax = new BigDecimal(0);
                BigDecimal acommon_wtr_tax = new BigDecimal(0);
                BigDecimal apersonal_wtr_tax = new BigDecimal(0);
                BigDecimal aeducation_cess = new BigDecimal(0);
                BigDecimal apenal_charge = new BigDecimal(0);
                BigDecimal aproperty_tax = new BigDecimal(0);
                BigDecimal atotal_amount = new BigDecimal(0);
                BigDecimal arain_harvest_charge = new BigDecimal(0);
                BigDecimal totalBuildupArea = new BigDecimal(0);
                BigDecimal othCommWtrTax = new BigDecimal(0);
                BigDecimal othCompTax = new BigDecimal(0);
                BigDecimal othEduCess = new BigDecimal(0);
                BigDecimal othPenalCharge = new BigDecimal(0);
                BigDecimal othPeronalWtrTax = new BigDecimal(0);
                BigDecimal othPropTax = new BigDecimal(0);
                BigDecimal othRainHarvChrg = new BigDecimal(0);
                BigDecimal othSanitTax = new BigDecimal(0);
                BigDecimal othTotAmount = new BigDecimal(0);
                BigDecimal othamt = new BigDecimal(0);
                String effect_year = null;
                Long tax_rate_id = 0L;
                Long fy_id = 0L;
                Long old_ward_id = 0L;
                Long lastPaymentId = 0L;
                Integer paid_status = 0;
                try {
                    String actualEffectiveDate = null;
                    Long entry_fy_id = 0L;
                    prop_id = propertyMasterDao.findIdByPropNo(demandRequestDto.getHolding_no());
                    Pageable topOneYear = PageRequest.of(0, 1);
                    Integer previousDemandYear = demandDao.getDemandByPropIdAndEffectYear(prop_id, demandRequestDto.getEffect_year());


                    log.info("Current Demand Year count", previousDemandYear);


                    if (prop_id != null && previousDemandYear == 0) {
                        old_ward_id = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getOld_ward_id() : 0L;

                        Optional<List<OwnerTaxMasterBean>> ownerTaxResult = ownerTaxMasterDao.getTaxByProp_id(prop_id);
                        List<OwnerTaxMasterBean> ownerTax = ownerTaxResult.isPresent() ? ownerTaxResult.get() : null;
                        tax_rate_id = ownerTax.get(0).getId();

                        isHarvest = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getRain_harvest() : null;
                        ward_id = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getWard_id() : 0L;
                        totalBuildupArea = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getTotalbuilbup_area() : new BigDecimal(0.00);

                        Integer countYearForDemand = Integer.parseInt(demandRequestDto.getEffect_year().substring(0, 4)) - Integer.parseInt(currentFinYear.substring(5, 9));
                        if (ownerTax.size() > 0) {
                            for (OwnerTaxMasterBean ownerTaxMaster : ownerTax) {
                                property_tax = property_tax.add(ownerTaxMaster.getProperty_tax());
                                sanitation_tax = sanitation_tax.add(ownerTaxMaster.getSanitation_tax());
                                composite_tax = composite_tax.add(ownerTaxMaster.getComposite_tax());
                                common_wtr_tax = common_wtr_tax.add(ownerTaxMaster.getCommon_wtr_tax());
                                personal_wtr_tax = personal_wtr_tax.add(ownerTaxMaster.getPersonal_wtr_tax());
                                education_cess = education_cess.add(ownerTaxMaster.getEducation_cess());

                 

                                property_tax = ownerTax.get(0).getProperty_tax();
                                Integer yearCount = Integer.parseInt(currentFinYear.substring(5, 9)) - Integer.parseInt(demandRequestDto.getEffect_year().substring(0, 4));
                                Integer fromYear = Integer.parseInt(demandRequestDto.getEffect_year().substring(0, 4));

                                for (int x = 0; x < yearCount; x++) {
                                    fromYear++;

                                    if (Integer.parseInt(currentFinYear.substring(5, 9)) != fromYear) {
                                        penalty = penalty.add(property_tax.multiply(BigDecimal.valueOf(.18))).setScale(2, RoundingMode.CEILING);
                                        if (effect_year_Int > 2016) {

                                            apenal_charge = apenal_charge.add(BigDecimal.valueOf(1000.00));
                                        }

                                                            aproperty_tax = aproperty_tax.add(property_tax);
                                        othramt = othramt.add(othramt);
                                        asanitation_tax = asanitation_tax.add(sanitation_tax);
                                        aeducation_cess = aeducation_cess.add(education_cess);
                                        acomposite_tax = acomposite_tax.add(composite_tax);
                                        acommon_wtr_tax = acommon_wtr_tax.add(common_wtr_tax);
                                        apersonal_wtr_tax = apersonal_wtr_tax.add(personal_wtr_tax);


                                    }
                                }
                            }
                        }
  

                        fy_id = finID.getId();


                        Integer newDemandNo = demandDao.generateDemandNo(); // need to tuning this query, as this take more time to fetch
                        Integer checkDuplicateDemandNo = demandDao.checkDuplicateDemandNo(newDemandNo.toString());
                        if(checkDuplicateDemandNo>0) {
                            throw new BadRequestException("Duplicate DemandNo found");
                        }
                        demandDetailsBean.setProp_id(prop_id);
                        demandDetailsBean.setProperty_tax(property_tax);
                        demandDetailsBean.setWard_id(ward_id);
                        demandDetailsBean.setDemand_date(LocalDate.now().toString());
                        demandDetailsBean.setDemand_type("Re Assessment");
                        demandDetailsBean.setDemand_deactive(0);
                        demandDetailsBean.setCommon_wtr_tax(common_wtr_tax);
                        demandDetailsBean.setComposite_tax(composite_tax);
                        demandDetailsBean.setCorrection_status(0L);
                        demandDetailsBean.setEducation_cess(education_cess);
                        demandDetailsBean.setEffect_year(demandRequestDto.getEffect_year());
                        demandDetailsBean.setFy_id(fy_id);
                        demandDetailsBean.setDiff_sts(0);
                        demandDetailsBean.setLast_payment_id(lastPaymentId);
                        demandDetailsBean.setOthCommWtrTax(othramt);
                        demandDetailsBean.setRain_harvest_charge(rain_harvest_charge);
                        demandDetailsBean.setSanitation_tax(sanitation_tax);
                        demandDetailsBean.setPenalty(penalty);
                        demandDetailsBean.setPersonal_wtr_tax(personal_wtr_tax);
                        demandDetailsBean.setPenalty(penalty);
                        demandDetailsBean.setPenal_charge(penal_charge);
                        total_amount = total_amount
                                .add(property_tax)
                                .add(common_wtr_tax)
                                .add(composite_tax)
                                .add(education_cess)
                                .add(othramt)
                                .add(rain_harvest_charge)
                                .add(sanitation_tax)
                                .add(penalty)
                                .add(penal_charge).setScale(2, RoundingMode.CEILING);
                        demandDetailsBean.setTotal_amount(total_amount);
                        demandDetailsBean.setAcommon_wtr_tax(acommon_wtr_tax);
                        demandDetailsBean.setAcomposite_tax(acomposite_tax);
                        demandDetailsBean.setAeducation_cess(aeducation_cess);
                        demandDetailsBean.setAproperty_tax(aproperty_tax);
                        demandDetailsBean.setAsanitation_tax(asanitation_tax);
                        demandDetailsBean.setApersonal_wtr_tax(apersonal_wtr_tax);
                        demandDetailsBean.setArain_harvest_charge(arain_harvest_charge);
                        demandDetailsBean.setApenal_charge(apenal_charge);

                        atotal_amount = atotal_amount.add(asanitation_tax).add(aeducation_cess).add(acomposite_tax).add(apersonal_wtr_tax).add(apenal_charge).add(arain_harvest_charge).setScale(2, RoundingMode.CEILING);
                        demandDetailsBean.setAtotal_amount(atotal_amount);
                        demandDetailsBean.setDemand_no(newDemandNo.toString());
                        demandDetailsBean.setEntry_fy_id(fy_id);
                        demandDetailsBean.setTax_rate_id(tax_rate_id);
                        demandDetailsBean.setStatus(1);
                        demandDetailsBean.setUser_id(user_id);
                        demandDetailsBean.setPaid_status(paid_status);
                        demandDetailsBean.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
                        demandDetailsBean.setOld_ward_id(old_ward_id);
                        demandDetailsBean.setOthCompTax(othCompTax);
                        demandDetailsBean.setOthCommWtrTax(othCommWtrTax);
                        demandDetailsBean.setOthEduCess(othEduCess);
                        demandDetailsBean.setOthPenalCharge(othPenalCharge);
                        demandDetailsBean.setOthPropTax(othPropTax);
                        demandDetailsBean.setOthRainHarvChrg(othRainHarvChrg);
                        demandDetailsBean.setOthPeronalWtrTax(othPeronalWtrTax);
                        demandDetailsBean.setOthSanitTax(othSanitTax);
                        demandDetailsBean.setOthTotAmount(othTotAmount);
                        demandDetailsBean.setOtheramt(othamt);
                        DemandDetailsBean returnJson = demandDao.save(demandDetailsBean);

                        log.info("Data Save for Re-assement type demand Results {}", returnJson);
                    }
                } // end if of prop_id is not null condition
                catch (Exception e) {
                	log.info("Error Message===> {}",e.getMessage());
                    throw new BadRequestException(e.getMessage());
                }

            } // end of main for loop
        } else if (demandRequest.size() == 0 || demandRequest == null) {
            throw new BadRequestException("Demand Should not be blank");
        }
        return "Success";
    }

    @Override
//    @Transactional(rollbackOn = BadRequestException.class)
    public String createDemandDuringNewAssessment(List<NewDemandRequestDTO> newDemandList) throws Exception {
        log.info("Owner Tax Master JSON New Assessment{} ", newDemandList);

        if (newDemandList != null) {
            try {
                Integer from_year = 0;
                Integer countFromyear = 0;
                Integer to_year = 0;
                Integer totalYearOfDeamand = 0;
                Optional<RainHarvestRateMasterBean> rainHarvestRate = null;
                Long prop_id = 0L;
                Long old_ward_id = 0L;
                String current_fin_year = null;
                String demand_no = null;
                String demand_date = null;
                Long fy_id = 0L;
                Long ward_id = 0L;
                String isHarvest = null;
                Integer paid_status = 0;
                Long tax_rate_id = 0L;
                Integer demand_deactive = 0;

                Long lastPaymentId = 0L;
                Integer newDemandNo = demandDao.generateDemandNo(); // need to tuning this query, as this take more time to fetch
                Integer checkDuplicateDemandNo = demandDao.checkDuplicateDemandNo(newDemandNo.toString());
                if(checkDuplicateDemandNo>0) {
                    throw new BadRequestException("Duplicate DemandNo found");
                }
                for (NewDemandRequestDTO ownerTaxDetails : newDemandList) {
                    Long user_id = ownerTaxDetails.getOwnerTaxMasterRequest().getUser_id();
                    BigDecimal property_tax = new BigDecimal(0.00);
                    BigDecimal sanitation_tax = new BigDecimal(0.00);
                    BigDecimal composite_tax = new BigDecimal(0.00);
                    BigDecimal common_wtr_tax = new BigDecimal(0.00);
                    BigDecimal personal_wtr_tax = new BigDecimal(0.00);
                    BigDecimal rain_harvest_charge = new BigDecimal(0.00);
                    BigDecimal education_cess = new BigDecimal(0.00);
                    BigDecimal penalty = new BigDecimal(0.00);
                    BigDecimal penal_charge = new BigDecimal(0.00);
                    BigDecimal othramt = new BigDecimal(0.00);
                    BigDecimal total_amount = new BigDecimal(0.00);
                    BigDecimal asanitation_tax = new BigDecimal(0.00);
                    BigDecimal acomposite_tax = new BigDecimal(0.00);
                    BigDecimal acommon_wtr_tax = new BigDecimal(0.00);
                    BigDecimal apersonal_wtr_tax = new BigDecimal(0.00);
                    BigDecimal aeducation_cess = new BigDecimal(0.00);
                    BigDecimal apenal_charge = new BigDecimal(0.00);
                    BigDecimal aproperty_tax = new BigDecimal(0.00);
                    BigDecimal atotal_amount = new BigDecimal(0.00);
                    BigDecimal arain_harvest_charge = new BigDecimal(0.00);

                    BigDecimal othCommWtrTax = new BigDecimal(0.00);
                    BigDecimal othCompTax = new BigDecimal(0.00);
                    BigDecimal othEduCess = new BigDecimal(0);
                    BigDecimal othPenalCharge = new BigDecimal(0);
                    BigDecimal othPeronalWtrTax = new BigDecimal(0);
                    BigDecimal othPropTax = new BigDecimal(0);
                    BigDecimal othRainHarvChrg = new BigDecimal(0);
                    BigDecimal othSanitTax = new BigDecimal(0);
                    BigDecimal othTotAmount = new BigDecimal(0);
                    BigDecimal othamt = new BigDecimal(0.00);

                    DemandDetailsBean demandDetailsBean = new DemandDetailsBean();
                    
                    property_tax = property_tax.add(ownerTaxDetails.getOwnerTaxMasterRequest().getProperty_tax());
                    
                    log.info("Property Tax ====== {}", property_tax );
                    tax_rate_id = ownerTaxDetails.getOwnerTaxMasterRequest().getId();
                    prop_id = ownerTaxDetails.getOwnerTaxMasterRequest().getProp_id();

                    Optional<PropertyMasterBean> getWardId = propertyMasterDao.findById(prop_id);
                    List<FinYearBean> current_fin_year_result = finYearDao.findAll();
                    current_fin_year = current_fin_year_result.get(current_fin_year_result.size() - 1).getFy_name();
                    old_ward_id = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getOld_ward_id() : 0L;
                    isHarvest = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getRain_harvest() : "";
                    fy_id = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getEntry_fy_id() : 0L;
                    //BigDecimal totalBuildupArea= propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getTotalbuilbup_area() : new BigDecimal(0.00);
                    BigDecimal plot_area = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getPlot_area() : null;

                    if (fy_id <= 0L) {

                        List<FinYearBean> allRecords = finYearDao.findAll();
                        fy_id = allRecords.get(allRecords.size() - 1).getId();

                    }
                    ward_id = getWardId.isPresent() ? getWardId.get().getWard_id() : null;
                    education_cess = ownerTaxDetails.getOwnerTaxMasterRequest().getEducation_cess();
                    composite_tax = ownerTaxDetails.getOwnerTaxMasterRequest().getComposite_tax();
                    common_wtr_tax = ownerTaxDetails.getOwnerTaxMasterRequest().getCommon_wtr_tax();
                    countFromyear = Integer.parseInt(ownerTaxDetails.getOwnerTaxMasterRequest().getEffect_year().substring(5, 9));
                    from_year = Integer.parseInt(ownerTaxDetails.getOwnerTaxMasterRequest().getEffect_year().substring(0, 4));
                    to_year = Integer.parseInt(ownerTaxDetails.getOwnerTaxMasterRequest().getEffect_year().substring(5, 9));
                    totalYearOfDeamand = to_year - from_year;
                    Integer counter = 0;
                    Integer insideOneCounter = 0;
                    BigDecimal tempCompositeTax;
                    BigDecimal tempEduCess;
                    BigDecimal tempATotal = new BigDecimal(0.00);
                    BigDecimal tempAGrandTotal = new BigDecimal(0.00);
                    String doc =null;

                    tempCompositeTax = composite_tax;
                    tempEduCess = education_cess;
                    if (Integer.parseInt(current_fin_year.substring(5, 9)) != countFromyear) {

// penalty charges calculation but not for current year 
                        if (countFromyear < 2024) {
// Baad me Changes Karna hoga 22/02/2024 ko No Penal Charges Hatha Diye hai
                            apenal_charge = BigDecimal.valueOf(0000.00).setScale(2, RoundingMode.CEILING);
                            aproperty_tax = ownerTaxDetails.getOwnerTaxMasterRequest().getProperty_tax();
                            
                            aeducation_cess = tempEduCess;
                            acomposite_tax = tempCompositeTax;
                            acommon_wtr_tax = common_wtr_tax;
                            tempATotal = tempATotal.add(aproperty_tax).add(aeducation_cess).add(acomposite_tax).add(acommon_wtr_tax);
                            penalty = aproperty_tax.multiply(BigDecimal.valueOf(.05)).setScale(2, RoundingMode.CEILING);
                            tempAGrandTotal = tempAGrandTotal.add(aeducation_cess).add(acomposite_tax).add(apenal_charge).add(penalty).add(aproperty_tax).setScale(2, RoundingMode.CEILING);
                            atotal_amount = tempAGrandTotal;
                        }
                        else {
                        aproperty_tax = ownerTaxDetails.getOwnerTaxMasterRequest().getProperty_tax();
     
                        aeducation_cess = tempEduCess;
                        acomposite_tax = tempCompositeTax;
                        acommon_wtr_tax = common_wtr_tax;
                      
                        tempATotal = tempATotal.add(aproperty_tax).add(aeducation_cess).add(acomposite_tax).add(acommon_wtr_tax);
                        //penalty = tempATotal.multiply(BigDecimal.valueOf(.05)).setScale(2, RoundingMode.CEILING);
                        tempAGrandTotal = tempAGrandTotal.add(aeducation_cess).add(acomposite_tax).add(aproperty_tax).setScale(2, RoundingMode.CEILING);
                        atotal_amount = tempAGrandTotal;
                    }
                    } 
                    if (Integer.parseInt(current_fin_year.substring(5, 9)) == countFromyear) {
                        log.info("Inside one counter{}", insideOneCounter);
                        
                       
                        
                        
                        if (isHarvest.equalsIgnoreCase("N") || isHarvest.equalsIgnoreCase("No")|| isHarvest.isEmpty() || isHarvest.isBlank()) {
                            if (plot_area != null) {
                                rainHarvestRate = Optional.of(rainHarvestRateDao.getRainHarvestCharge(plot_area).get());
                                rain_harvest_charge = rain_harvest_charge.add(rainHarvestRate.get().getRain_harvest_charge());
                            }
                        }

                    }
                    
                    
                    
                    
                    
                    

                    demandDetailsBean.setProp_id(prop_id);
                    demandDetailsBean.setProperty_tax(property_tax);
                    demandDetailsBean.setWard_id(ward_id);
                    demandDetailsBean.setDemand_date(LocalDate.now().toString());
                    demandDetailsBean.setDemand_type("New Assessment");
                    demandDetailsBean.setDemand_deactive(0);
                    demandDetailsBean.setCommon_wtr_tax(common_wtr_tax);
                    demandDetailsBean.setComposite_tax(composite_tax);
                    demandDetailsBean.setCorrection_status(0L);
                    demandDetailsBean.setEducation_cess(education_cess);
                    demandDetailsBean.setEffect_year(ownerTaxDetails.getOwnerTaxMasterRequest().getEffect_year());
                    demandDetailsBean.setFy_id(fy_id);
                    demandDetailsBean.setDiff_sts(0);
                    demandDetailsBean.setLast_payment_id(lastPaymentId);
                    demandDetailsBean.setOthCommWtrTax(othramt);
                    demandDetailsBean.setRain_harvest_charge(rain_harvest_charge);
                    demandDetailsBean.setSanitation_tax(sanitation_tax);
                    demandDetailsBean.setPenalty(penalty);
                    demandDetailsBean.setPersonal_wtr_tax(personal_wtr_tax);
                    demandDetailsBean.setPenal_charge(apenal_charge);

                    total_amount = total_amount
                            .add(property_tax)
                            .add(common_wtr_tax)
                            .add(composite_tax)
                            .add(education_cess)
                            .add(othramt)
                            .add(penalty)
                            .add(apenal_charge)
                            .setScale(2, RoundingMode.CEILING);
                    
                    
                    
                    demandDetailsBean.setTotal_amount(total_amount);
                    demandDetailsBean.setAcommon_wtr_tax(acommon_wtr_tax);
                    demandDetailsBean.setAcomposite_tax(acomposite_tax);
                    demandDetailsBean.setAeducation_cess(aeducation_cess);
                    demandDetailsBean.setAproperty_tax(aproperty_tax);
                    demandDetailsBean.setAsanitation_tax(asanitation_tax);
                    demandDetailsBean.setApersonal_wtr_tax(apersonal_wtr_tax);
                    demandDetailsBean.setArain_harvest_charge(arain_harvest_charge);
                    demandDetailsBean.setApenal_charge(apenal_charge);

                    demandDetailsBean.setAtotal_amount(atotal_amount);
                    demandDetailsBean.setDemand_no(newDemandNo.toString());
                    demandDetailsBean.setEntry_fy_id(fy_id);
                    demandDetailsBean.setTax_rate_id(tax_rate_id);
                    demandDetailsBean.setStatus(1);
                    demandDetailsBean.setUser_id(user_id);
                    demandDetailsBean.setPaid_status(paid_status);
                    demandDetailsBean.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
                    demandDetailsBean.setOld_ward_id(old_ward_id);
                    demandDetailsBean.setOthCompTax(othCompTax);
                    demandDetailsBean.setOthCommWtrTax(othCommWtrTax);
                    demandDetailsBean.setOthEduCess(othEduCess);
                    demandDetailsBean.setOthPenalCharge(othPenalCharge);
                    demandDetailsBean.setOthPropTax(othPropTax);
                    demandDetailsBean.setOthRainHarvChrg(othRainHarvChrg);
                    demandDetailsBean.setOthPeronalWtrTax(othPeronalWtrTax);
                    demandDetailsBean.setOthSanitTax(othSanitTax);
                    demandDetailsBean.setOthTotAmount(othTotAmount);
                    demandDetailsBean.setOtheramt(othamt);
                    DemandDetailsBean returnJson = demandDao.save(demandDetailsBean);
                    log.info("return json =====> line 464 ===> {}",returnJson);

                    if (returnJson == null || returnJson.getDemand_no().equals(null)) {
                        throw new BadRequestException("Something Wrong!!!!");
                    }

                }


            } catch (Exception e) {
            	log.info("Error while saving NEW DEAMND ==========>{}",e.getMessage());
                throw new BadRequestException("Something Wrong!!!!");
            }

        } else if (newDemandList == null) {
            throw new BadRequestException("No records found");
        }
        return "Success";
    }
/**
 * ----------------------------------------------------------------------------------------------------------------------------
 * *******************************CALCULATION PART FOR RE Assessment   ***************************************************
 * ----------------------------------------------------------------------------------------------------------------------------
 */
    @Override
    public String createDemandDuringReAssessment(List<NewDemandRequestDTO> newDemandList) throws Exception {
        log.info("Owner Tax Master JSON in Re Assessment {} ", newDemandList);

        if (newDemandList != null) {
            try {
                Integer from_year = 0;
                Integer countFromyear = 0;
                Integer to_year = 0;
                Integer totalYearOfDeamand = 0;
                Optional<RainHarvestRateMasterBean> rainHarvestRate = null;
                Long prop_id = 0L;
                Long old_ward_id = 0L;
                String current_fin_year = null;
                Long fy_id = 0L;
                Long ward_id = 0L;
                String isHarvest = null;
                Integer paid_status = 0;
                Integer diff_status=0;
                Long tax_rate_id = 0L;
                Integer demand_deactive = 0;
                BigDecimal yearWiseDefferenceTotalAmount=new BigDecimal(0.00);
                Integer newDemandNo = demandDao.generateDemandNo(); // need to tuning this query, as this take more time to fetch
                Integer checkDuplicateDemandNo = demandDao.checkDuplicateDemandNo(newDemandNo.toString());
                if(checkDuplicateDemandNo>0) {
                    throw new BadRequestException("Duplicate DemandNo found");
                }
                Long lastPaymentId = 0L;
                Boolean isCurrentYearNoDuesDemand=false;
                String currentEffectYear=null;

                List<FinYearBean> allCurrentYearRecords = finYearDao.findAll(); // somehow current financial year need to get from the session, for this this query need not to be fire every time.
                currentEffectYear = allCurrentYearRecords.get(allCurrentYearRecords.size() - 1).getFy_name();

                prop_id=newDemandList.stream().map(v -> v.getOwnerTaxMasterRequest().getProp_id()).findFirst().get();

                Optional<List<DemandDetailsBean>> demandResultForDefferenceAmount=demandDao.getDemandForDefferenceAmount(prop_id,currentEffectYear);
                List<DemandDetailsBean> differenceDemandResultList = demandResultForDefferenceAmount.isPresent() ? demandResultForDefferenceAmount.get() : null;
                Optional<List<DemandDetailsBean>> getAllPaidDemandResult=null;
                List<DemandDetailsBean> getAllPaidDemandList=null;
                if(differenceDemandResultList !=null) {
                    if (differenceDemandResultList.size() > 0) {
                        isCurrentYearNoDuesDemand = true;
                        diff_status=1;

                    }
                }

                if (newDemandList.size() > 0) {
                    Optional<List<DemandDetailsBean>> demandResult = demandDao.getDemandByPropIdAndStatus(prop_id);
                    List<DemandDetailsBean> demandResultList = demandResult.isPresent() ? demandResult.get() : null;
                    if (demandResultList.size() > 0) {
                        for (int x = 0; x < demandResultList.size(); x++) {
                            demandResultList.get(x).setDemand_deactive(1);
                            demandDao.save(demandResultList.get(x));
                        }
                    }
                }


                for (NewDemandRequestDTO ownerTaxDetails : newDemandList) {
                    Long user_id = ownerTaxDetails.getOwnerTaxMasterRequest().getUser_id();
                    BigDecimal property_tax = new BigDecimal(0.00);
                    BigDecimal sanitation_tax = new BigDecimal(0.00);
                    BigDecimal composite_tax = new BigDecimal(0.00);
                    BigDecimal common_wtr_tax = new BigDecimal(0.00);
                    BigDecimal personal_wtr_tax = new BigDecimal(0.00);
                    BigDecimal rain_harvest_charge = new BigDecimal(0.00);
                    BigDecimal education_cess = new BigDecimal(0.00);
                    BigDecimal penalty = new BigDecimal(0.00);
                    BigDecimal penal_charge = new BigDecimal(0.00);
                    BigDecimal othramt = new BigDecimal(0.00);
                    BigDecimal total_amount = new BigDecimal(0.00);
                    BigDecimal asanitation_tax = new BigDecimal(0.00);
                    BigDecimal acomposite_tax = new BigDecimal(0.00);
                    BigDecimal acommon_wtr_tax = new BigDecimal(0.00);
                    BigDecimal apersonal_wtr_tax = new BigDecimal(0.00);
                    BigDecimal aeducation_cess = new BigDecimal(0.00);
                    BigDecimal apenal_charge = new BigDecimal(0.00);
                    BigDecimal aproperty_tax = new BigDecimal(0.00);
                    BigDecimal atotal_amount = new BigDecimal(0.00);
                    BigDecimal arain_harvest_charge = new BigDecimal(0.00);

                    BigDecimal othCommWtrTax = new BigDecimal(0.00);
                    BigDecimal othCompTax = new BigDecimal(0.00);
                    BigDecimal othEduCess = new BigDecimal(0);
                    BigDecimal othPenalCharge = new BigDecimal(0);
                    BigDecimal othPeronalWtrTax = new BigDecimal(0);
                    BigDecimal othPropTax = new BigDecimal(0);
                    BigDecimal othRainHarvChrg = new BigDecimal(0);
                    BigDecimal othSanitTax = new BigDecimal(0);
                    BigDecimal othTotAmount = new BigDecimal(0);
                    BigDecimal othamt = new BigDecimal(0.00);
                    BigDecimal temp_difference_Amount_penalty=new BigDecimal(0.00);
                    DemandDetailsBean demandDetailsBean = new DemandDetailsBean();
                    property_tax = property_tax.add(ownerTaxDetails.getOwnerTaxMasterRequest().getProperty_tax());

                    tax_rate_id = ownerTaxDetails.getOwnerTaxMasterRequest().getId();
                    prop_id = ownerTaxDetails.getOwnerTaxMasterRequest().getProp_id();

                    Optional<PropertyMasterBean> getWardId = propertyMasterDao.findById(prop_id);
                    List<FinYearBean> current_fin_year_result = finYearDao.findAll();
                    current_fin_year = current_fin_year_result.get(current_fin_year_result.size() - 1).getFy_name();
                    old_ward_id = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getOld_ward_id() : 0L;
                    isHarvest = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getRain_harvest() : "";
                    fy_id = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getEntry_fy_id() : 0L;
                    // BigDecimal totalBuildupArea= propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getTotalbuilbup_area() : new BigDecimal(0.00);
                    BigDecimal plot_area = propertyMasterDao.findById(prop_id).isPresent() ? propertyMasterDao.findById(prop_id).get().getPlot_area() : null;
                    if (fy_id <= 0L) {

                        List<FinYearBean> allRecords = finYearDao.findAll();
                        fy_id = allRecords.get(allRecords.size() - 1).getId();

                    }
                    ward_id = getWardId.isPresent() ? getWardId.get().getWard_id() : null;

                    education_cess = ownerTaxDetails.getOwnerTaxMasterRequest().getEducation_cess();
                    composite_tax = ownerTaxDetails.getOwnerTaxMasterRequest().getComposite_tax();

                    countFromyear = Integer.parseInt(ownerTaxDetails.getOwnerTaxMasterRequest().getEffect_year().substring(5, 9));
                    from_year = Integer.parseInt(ownerTaxDetails.getOwnerTaxMasterRequest().getEffect_year().substring(0, 4));
                    to_year = Integer.parseInt(ownerTaxDetails.getOwnerTaxMasterRequest().getEffect_year().substring(5, 9));
                    totalYearOfDeamand = to_year - from_year;
                    Integer counter = 0;
                    Integer insideOneCounter = 0;
                    BigDecimal tempCompositeTax;
                    BigDecimal tempEduCess;
                    BigDecimal tempATotal = new BigDecimal(0.00);
                    BigDecimal tempDefferencePenalty= new BigDecimal(0.00);
                    BigDecimal tempAGrandTotal = new BigDecimal(0.00);

                    tempCompositeTax = composite_tax;
                    tempEduCess = education_cess;
                    if (Integer.parseInt(current_fin_year.substring(5, 9)) != countFromyear) {

                    	//late fine charge
                    	 if (countFromyear < 2024) {

                             apenal_charge = BigDecimal.valueOf(0000.00).setScale(2, RoundingMode.CEILING);
                             aproperty_tax = ownerTaxDetails.getOwnerTaxMasterRequest().getProperty_tax();
                             
                             aeducation_cess = tempEduCess;
                             acomposite_tax = tempCompositeTax;
                             acommon_wtr_tax = common_wtr_tax;
                             tempATotal = tempATotal.add(aproperty_tax).add(aeducation_cess).add(acomposite_tax).add(acommon_wtr_tax);
                             penalty = tempATotal.multiply(BigDecimal.valueOf(.05)).setScale(2, RoundingMode.CEILING);
                             tempAGrandTotal = tempAGrandTotal.add(aeducation_cess).add(acomposite_tax).add(apenal_charge).add(penalty).add(aproperty_tax).setScale(2, RoundingMode.CEILING);
                             atotal_amount = tempAGrandTotal;
                         }
                         else {
                         aproperty_tax = ownerTaxDetails.getOwnerTaxMasterRequest().getProperty_tax();
      
                         aeducation_cess = tempEduCess;
                         acomposite_tax = tempCompositeTax;
                         acommon_wtr_tax = common_wtr_tax;
                       
                         tempATotal = tempATotal.add(aproperty_tax).add(aeducation_cess).add(acomposite_tax).add(acommon_wtr_tax);
                         //penalty = tempATotal.multiply(BigDecimal.valueOf(.05)).setScale(2, RoundingMode.CEILING);
                         tempAGrandTotal = tempAGrandTotal.add(aeducation_cess).add(acomposite_tax).add(aproperty_tax).setScale(2, RoundingMode.CEILING);
                         atotal_amount = tempAGrandTotal;
                     }

//                        aproperty_tax = ownerTaxDetails.getOwnerTaxMasterRequest().getProperty_tax();
//                        asanitation_tax = sanitation_tax;
//                        aeducation_cess = tempEduCess;
//                        acomposite_tax = tempCompositeTax;
//                        acommon_wtr_tax = common_wtr_tax;
//                        apersonal_wtr_tax = common_wtr_tax;
//                        tempATotal = tempATotal.add(aproperty_tax).add(asanitation_tax).add(aeducation_cess).add(acomposite_tax).add(acommon_wtr_tax).add(apersonal_wtr_tax);
//                        penalty = tempATotal.multiply(BigDecimal.valueOf(.18)).setScale(2, RoundingMode.CEILING);
//                        tempAGrandTotal = tempAGrandTotal.add(asanitation_tax).add(aeducation_cess).add(acomposite_tax).add(apersonal_wtr_tax).add(penalty).add(aproperty_tax).setScale(2, RoundingMode.CEILING);
//                        atotal_amount = tempAGrandTotal;
                    } else if (Integer.parseInt(current_fin_year.substring(5, 9)) == countFromyear) {
                        log.info("Inside one counter{}", insideOneCounter);
                        if (isHarvest.equalsIgnoreCase("N") || isHarvest.equalsIgnoreCase("No")
                                || isHarvest.isEmpty() || isHarvest.isBlank()) {
                            if (plot_area != null) {
                                rainHarvestRate = Optional.of(rainHarvestRateDao.getRainHarvestCharge(plot_area).get());
                                rain_harvest_charge = rain_harvest_charge.add(rainHarvestRate.get().getRain_harvest_charge());
                            }
                        }

                    }


                    demandDetailsBean.setProp_id(prop_id);
                    demandDetailsBean.setProperty_tax(property_tax);
                    demandDetailsBean.setWard_id(ward_id);
                    demandDetailsBean.setDemand_date(LocalDate.now().toString());
                    demandDetailsBean.setDemand_type("Re Assessment");
                    demandDetailsBean.setDemand_deactive(0);
                    demandDetailsBean.setCommon_wtr_tax(common_wtr_tax);
                    demandDetailsBean.setComposite_tax(composite_tax);
                    demandDetailsBean.setCorrection_status(0L);
                    demandDetailsBean.setEducation_cess(education_cess);
                    demandDetailsBean.setEffect_year(ownerTaxDetails.getOwnerTaxMasterRequest().getEffect_year());
                    demandDetailsBean.setFy_id(fy_id);
                    demandDetailsBean.setDiff_sts(diff_status);
                    demandDetailsBean.setLast_payment_id(lastPaymentId);
                    demandDetailsBean.setOthCommWtrTax(othramt);
                    demandDetailsBean.setRain_harvest_charge(rain_harvest_charge);
                    demandDetailsBean.setSanitation_tax(sanitation_tax);
                    demandDetailsBean.setPenalty(penalty);
                    demandDetailsBean.setPersonal_wtr_tax(personal_wtr_tax);
                    demandDetailsBean.setPenal_charge(apenal_charge);

                    total_amount = total_amount
                            .add(property_tax)
                            .add(common_wtr_tax)
                            .add(composite_tax)
                            .add(education_cess)
                            .add(othramt)
                            .add(penalty)
                            .add(apenal_charge)
                            .setScale(2, RoundingMode.CEILING);

                    if(isCurrentYearNoDuesDemand) {  // Deference Amount Calculation
                        getAllPaidDemandResult=demandDao.getPaidDemand(prop_id,ownerTaxDetails.getOwnerTaxMasterRequest().getEffect_year());
                        getAllPaidDemandList = getAllPaidDemandResult.isPresent() ? getAllPaidDemandResult.get() : null;
                       if(getAllPaidDemandList!=null) {
                           for (DemandDetailsBean previousDemandForDeferenceAmount : getAllPaidDemandList) {
                               DemandDetailsBean demandDifferenceAmount = new DemandDetailsBean();
                               othTotAmount = total_amount.subtract(previousDemandForDeferenceAmount.getTotal_amount());
                               if(!previousDemandForDeferenceAmount.getEffect_year().equals(currentEffectYear)) {
                                   tempDefferencePenalty=tempDefferencePenalty.add(othTotAmount);
                                   temp_difference_Amount_penalty=tempDefferencePenalty.multiply(BigDecimal.valueOf(.18)).setScale(2, RoundingMode.CEILING);
                               }

                               demandDifferenceAmount.setDiff_sts(diff_status);
                               demandDao.save(demandDifferenceAmount);
                           }
                       }
                       }
                    demandDetailsBean.setTotal_amount(total_amount);
                    demandDetailsBean.setAcommon_wtr_tax(acommon_wtr_tax);
                    demandDetailsBean.setAcomposite_tax(acomposite_tax);
                    demandDetailsBean.setAeducation_cess(aeducation_cess);
                    demandDetailsBean.setAproperty_tax(aproperty_tax);
                
         
                
                   
                    demandDetailsBean.setAtotal_amount(atotal_amount);
                    demandDetailsBean.setDemand_no(newDemandNo.toString());
                    demandDetailsBean.setEntry_fy_id(fy_id);
                    demandDetailsBean.setTax_rate_id(tax_rate_id);
                    demandDetailsBean.setStatus(1);
                    demandDetailsBean.setUser_id(user_id);
                    demandDetailsBean.setPaid_status(paid_status);
                    demandDetailsBean.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
                    demandDetailsBean.setOld_ward_id(old_ward_id);
                    demandDetailsBean.setOthTotAmount(othTotAmount);
                    demandDetailsBean.setOthCompTax(othCompTax);
                    demandDetailsBean.setOthCommWtrTax(othCommWtrTax);
                    demandDetailsBean.setOthEduCess(othEduCess);
                    demandDetailsBean.setOthPenalCharge(othPenalCharge);
                    demandDetailsBean.setOthPropTax(othPropTax);
                    demandDetailsBean.setOthRainHarvChrg(othRainHarvChrg);
                    demandDetailsBean.setOthPeronalWtrTax(othPeronalWtrTax);
                    demandDetailsBean.setOthSanitTax(othSanitTax);
                    demandDetailsBean.setOthPenalty(temp_difference_Amount_penalty);
                    demandDetailsBean.setOtheramt(othamt);
                    DemandDetailsBean returnJson = demandDao.save(demandDetailsBean);

                    if (returnJson == null || returnJson.getDemand_no().equals(null)) {
                        throw new BadRequestException("Something Wrong!!!!");
                    }

                }


            } catch (Exception e) {
                throw new BadRequestException("Something Wrong!!!!");
            }

        } else if (newDemandList == null) {
            throw new BadRequestException("No records found");
        }
        return "Success";

    }



    @Override
    public List<DemandDetailsBean> getDemandDetailsByPropId(Long propId) {

        return demandDao.getDemandDetailsByPropId(propId);
    }

    @Override
    public List<Object[]> getDueDetailsByPropId(Long propId) throws Exception {
        String jpql = "select COALESCE(sum(a.total_amount),0)as total_amount, COALESCE(sum(a.penalty),0)\n" +
                "as penalty,COALESCE(sum(a.penal_charge),0) as penal_charge from public.tbl_prop_demand_dtls a where \n" +
                "a.prop_id=" + propId + " and a.paid_status=0 and a.demand_deactive=0";
        Query query = entityManager.createNativeQuery(jpql);
        //log.info("query................"+jpql);
        List<Object[]> results = query.getResultList();
        return results;
    }
}

