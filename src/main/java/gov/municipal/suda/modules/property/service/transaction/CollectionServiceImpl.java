package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.PropertyMasterDao;
import gov.municipal.suda.modules.property.dao.transaction.CollectionDao;
import gov.municipal.suda.modules.property.dao.transaction.DemandDao;
import gov.municipal.suda.modules.property.dao.transaction.PropDemandDetailsRepositoryDao;
import gov.municipal.suda.modules.property.dao.transaction.TeamWiseCollectionDao;
import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.PropertyCollectionMstrBean;
import gov.municipal.suda.modules.watermgmt.dao.transaction.ConsumerDemandDetailsDao;
import gov.municipal.suda.modules.watermgmt.dto.transaction.ConsumerDemandDetialsDTO;
import gov.municipal.suda.util.DBFunctionCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jogamp.common.util.ArrayHashSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.rowset.CachedRowSet;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Service
@Slf4j
public class CollectionServiceImpl implements CollectionService{
@Autowired
 CollectionDao collectionDao;
    @Autowired
    DemandDao demandDao;
    @Autowired
    PropertyMasterDao propertyMasterDao;
    @Autowired
    TeamWiseCollectionDao teamWiseCollectionDao;
    @Autowired
    EntityManager entityManager;
    
    @Autowired
    PropDemandDetailsRepositoryDao propDemandDetailsDao;
    
    @Autowired
    ConsumerDemandDetailsDao consumerDemandDetailsdao;
    
    @Autowired
    DailyAssessmentPropertyReport dailyAssessmentPropertyReport;


    @Override
    public Map<String, BigDecimal> getCollectionReport() {
//        LocalDateTime firstdateTime = LocalDateTime.of(year, 1, 1, 0, 0, 0,0);
//        LocalDateTime lastdateTime = LocalDateTime.of(year, 12, 31, 23, 59, 59,999999999);
        Map<String, BigDecimal> collectionReports = new HashMap<>();
        LocalDate currentDate = LocalDate.now();

        int year = LocalDate.now().getYear();
        log.info("year of the Month {}"+year);
        // Create a LocalDateTime instance for the first day of the year
        LocalDate firstdateTime = LocalDate.of(year-5, 1, 1);
        LocalDate lastdateTime = LocalDate.of(year, 12, 31);	
        // Format the LocalDateTime instance as a string in the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //DailyReport
        Timestamp todayStart = Timestamp.valueOf(currentDate.format(formatter)+" 00:00:00.00");
        Timestamp todayEnd = Timestamp.valueOf(currentDate.format(formatter)+" 23:59:59.999999999");
        log.info( "Inside Today Start {} ____ Inside Today End"+  todayStart+"______"+todayEnd);
        BigDecimal dailyReport = collectionDao.findByDateBetween(todayStart,todayEnd);
        collectionReports.put("today", dailyReport);

        //WeeklyReport
        Timestamp firstDayOfWeek = Timestamp.valueOf(currentDate.with(DayOfWeek.MONDAY)+" 00:00:00.00");
        Timestamp lastDayOfWeek = Timestamp.valueOf(currentDate.with(DayOfWeek.SUNDAY)+" 23:59:59.999999999");
        log.info("Inside First Day of Week {} ____ Last Day of Week {}"+  firstDayOfWeek+"______"+lastDayOfWeek);
        BigDecimal weeklyReport = collectionDao.findByDateBetween(firstDayOfWeek,lastDayOfWeek);
        collectionReports.put("thisWeek", weeklyReport);

        //MonthlyReport
        YearMonth currentMonth = YearMonth.from(currentDate);
        Timestamp firstDayOfMonth = Timestamp.valueOf(currentMonth.atDay(1)+" 00:00:00.00");
        Timestamp lastDayOfMonth = Timestamp.valueOf(currentMonth.atEndOfMonth()+" 23:59:59.999999999");
        log.info("fisrtDayofMonth {} __ lastDayOfMonth {}"+firstDayOfMonth+"______"+lastDayOfMonth);
        BigDecimal monthlyReport = collectionDao.findByDateBetween(firstDayOfMonth,lastDayOfMonth);
        collectionReports.put("thisMonth", monthlyReport);

        //Yearly Report
        Timestamp firstDayOfYear = Timestamp.valueOf(firstdateTime.format(formatter)+" 00:00:00.00");
        Timestamp lastDayOfYear = Timestamp.valueOf(lastdateTime.format(formatter)+" 23:59:59.999999999");
        log.info("Inside First Day of the Year {} ___ Inside LastDayof the Year  "+firstDayOfYear+"______"+lastDayOfYear);
        BigDecimal yearlyReport = collectionDao.findByDateBetween(firstDayOfYear, lastDayOfYear);
        collectionReports.put("thisYear", yearlyReport);
        
        
        
        
        
        log.info("Collection Reports {} "+collectionReports);
        return collectionReports;
        
        
        
    }

    @Override
    @Transactional(rollbackOn= BadRequestException.class)
    public void createCollection(Long prop_id, String effect_year, Long user_id,Long getMaxValue) throws Exception {
        PropertyCollectionMstrBean propertyCollectionMstrBean=new PropertyCollectionMstrBean();
        if(prop_id !=null) {
            try {
                List<DemandDetailsBean> demandDetailsByPropId = demandDao.getDemandDetailsByPropId1(prop_id,effect_year);
                Long ward_id= propertyMasterDao.findById(prop_id).get().getWard_id();
                if (demandDetailsByPropId.size() > 0) {
                    for(DemandDetailsBean demandInsideLoop : demandDetailsByPropId) {
                        propertyCollectionMstrBean.setProp_id(prop_id);
                        propertyCollectionMstrBean.setWard_id(ward_id);
                        propertyCollectionMstrBean.setDemand_id(demandInsideLoop.getId());
                        propertyCollectionMstrBean.setTransaction_id(getMaxValue);
                        propertyCollectionMstrBean.setProperty_tax(demandInsideLoop.getProperty_tax());
                        propertyCollectionMstrBean.setSanitation_tax(demandInsideLoop.getSanitation_tax());
                        propertyCollectionMstrBean.setComposite_tax(demandInsideLoop.getComposite_tax());
                        propertyCollectionMstrBean.setCommon_wtr_tax(demandInsideLoop.getCommon_wtr_tax());
                        propertyCollectionMstrBean.setPersonel_wtr_tax(demandInsideLoop.getPersonal_wtr_tax());
                        propertyCollectionMstrBean.setEducation_cess(demandInsideLoop.getEducation_cess());
                        propertyCollectionMstrBean.setPenalty(demandInsideLoop.getPenalty());
                        propertyCollectionMstrBean.setPenal_charge(demandInsideLoop.getPenal_charge());
                        propertyCollectionMstrBean.setOtheramt(demandInsideLoop.getOtheramt());
                        propertyCollectionMstrBean.setTot_amount(demandInsideLoop.getTotal_amount());
                        propertyCollectionMstrBean.setAproperty_tax(demandInsideLoop.getAproperty_tax());
                        propertyCollectionMstrBean.setAsanitation_tax(demandInsideLoop.getAsanitation_tax());
                        propertyCollectionMstrBean.setAcomposite_tax(demandInsideLoop.getAcomposite_tax());
                        propertyCollectionMstrBean.setAcommon_wtr_tax(demandInsideLoop.getAcommon_wtr_tax());
                        propertyCollectionMstrBean.setApersonal_wtr_tax(demandInsideLoop.getApersonal_wtr_tax());
                        propertyCollectionMstrBean.setAeducation_cess(demandInsideLoop.getAeducation_cess());
                        propertyCollectionMstrBean.setApenal_charge(demandInsideLoop.getApenal_charge());
                        propertyCollectionMstrBean.setAtotal_amount(demandInsideLoop.getAtotal_amount());
                        propertyCollectionMstrBean.setFy_id(demandInsideLoop.getFy_id());
                        propertyCollectionMstrBean.setFor_year(effect_year);
                        propertyCollectionMstrBean.setUser_id(user_id);
                        propertyCollectionMstrBean.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
                        propertyCollectionMstrBean.setStatus(1);
                        PropertyCollectionMstrBean newcollection   =collectionDao.save(propertyCollectionMstrBean);
                        if(newcollection==null)
                        	throw new Exception("Data not saved of propertyCollectionMstrBean ------- Line 140 -----");
                    }
                }
            }
            catch (Exception e){
                throw new BadRequestException(e.getMessage());
            }
        }
    }

    @Override
    public List<CounterCollectionReportDTO> getCollectionByWardTcMode(String dateFrom, String  dateTo, String ward_id, String user_id, String paymentMode) throws ParseException,Exception {
        LocalDate localFromDate = LocalDate.parse(dateFrom);
        Instant instantFromDate = localFromDate.atStartOfDay(ZoneId.of("Asia/Kolkata")).toInstant();
        LocalDate localToDate = LocalDate.parse(dateTo);
        Instant instantToDate = localToDate.atStartOfDay(ZoneId.of("Asia/Kolkata")).toInstant();

        Timestamp fromdate = Timestamp.from(instantFromDate);
        //log.info(fromdate.toString());
        Timestamp todate = Timestamp.from(instantToDate);
        //log.info(fromdate.toString());
        //log.info(todate.toString());
        Map<Object, Object> inputParameter=new LinkedHashMap<>();
        List<CounterCollectionReportDTO> counterCollectionReport=new ArrayList<>();
        Long wardId = null;
        Long userId=null;
        if(ward_id.equalsIgnoreCase("All")) {
            wardId=0L;
        }
        else if(!ward_id.equalsIgnoreCase("All")) {
            wardId=Long.parseLong(ward_id);
        }
        if(user_id.equalsIgnoreCase("All")) {
            userId=-1L;
        } else if (!user_id.equalsIgnoreCase("All")) {
            userId=Long.parseLong(user_id);
        }
        if(paymentMode.equalsIgnoreCase("All")) {
            paymentMode="";
        }

        try {
            inputParameter.put(fromdate, fromdate);
            inputParameter.put(todate, todate);
            inputParameter.put(wardId, wardId);
            inputParameter.put(userId,userId);
            inputParameter.put(paymentMode,paymentMode);

            CachedRowSet crs= DBFunctionCall.simpleFunctionCallWithParameter("public.get_counter_collection_report(?,?,?,?,?)", 1, inputParameter);;
           /* CachedRowSet crs=null;
            if(ward_id.equalsIgnoreCase("All") && paymentMode.equals("All") && user_id.equalsIgnoreCase("All")) {
                inputParameter.put(fromdate, fromdate);
                inputParameter.put(todate, todate);
                crs = DBFunctionCall.simpleFunctionCallWithParameter("public.get_counter_collection_report_with_from_date_and_to_date(?,?)", 1, inputParameter);
            }
            else if(ward_id.equalsIgnoreCase("All") && !paymentMode.equals("All") && !user_id.equals("All")) {
                inputParameter.put(fromdate, fromdate);
                inputParameter.put(todate, todate);
                inputParameter.put(user_id,user_id);
                inputParameter.put(paymentMode,paymentMode);
                 crs = DBFunctionCall.simpleFunctionCallWithParameter("public.get_counter_collection_report_without_ward_id (?,?,?,?)", 1, inputParameter);
            }
           else if(paymentMode.equals("All") && !ward_id.equalsIgnoreCase("All") && !user_id.equals("All")) {
                Long wardId=Long.parseLong(ward_id);
                inputParameter.put(fromdate, fromdate);
                inputParameter.put(todate, todate);
                inputParameter.put(wardId, wardId);
                inputParameter.put(user_id,user_id);
                crs = DBFunctionCall.simpleFunctionCallWithParameter("public.get_counter_collection_report_without_payment_mode(?,?,?,?)", 1, inputParameter);
            }
           else if(!paymentMode.equals("All") && !ward_id.equalsIgnoreCase("All") && user_id.equals("All")) {
                Long wardId=Long.parseLong(ward_id);
                inputParameter.put(fromdate, fromdate);
                inputParameter.put(todate, todate);
                inputParameter.put(wardId, wardId);
                inputParameter.put(paymentMode,paymentMode);
                crs = DBFunctionCall.simpleFunctionCallWithParameter("public.get_counter_collection_report_without_operator(?,?,?,?)", 1, inputParameter);
            }
            else if(!ward_id.equalsIgnoreCase("All") && !paymentMode.equals("All") && !user_id.equals("All")) {
                Long wardId=Long.parseLong(ward_id);
                inputParameter.put(fromdate, fromdate);
                inputParameter.put(todate, todate);
                inputParameter.put(wardId, wardId);
                inputParameter.put(user_id,user_id);
                inputParameter.put(paymentMode,paymentMode);
                crs = DBFunctionCall.simpleFunctionCallWithParameter("public.get_counter_collection_report(?,?,?,?,?)", 1, inputParameter);
            }*/
             while (crs.next()) {
                CounterCollectionReportDTO dto = new CounterCollectionReportDTO();
                dto.setWard_id(crs.getLong(1));
                dto.setAcomposite_tax(crs.getBigDecimal(2));
                dto.setAproperty_tax(crs.getBigDecimal(3));
                dto.setBank_name(crs.getString(4));
                dto.setBranch_name(crs.getString(5));
                dto.setCheque_dd_aprcode(crs.getString(6));
                dto.setComposite_tax(crs.getBigDecimal(7));
                dto.setDiscount(crs.getBigDecimal(8));
                dto.setEducation_cess(crs.getBigDecimal(9));
                dto.setAeducation_cess(crs.getBigDecimal(10));
                dto.setForm_fee(crs.getBigDecimal(11));
                dto.setMobile_no(crs.getString(12));
                dto.setOwner_name(crs.getString(13));
                dto.setPayment_mode(crs.getString(14));
                dto.setPenal_charge(crs.getBigDecimal(15));
                dto.setPenalty(crs.getBigDecimal(16));
                dto.setProperty_no(crs.getString(17));
                dto.setProperty_tax(crs.getBigDecimal(18));
                dto.setRain_harvest_charge(crs.getBigDecimal(19));
                dto.setStampdate(crs.getString(20).substring(0,10));
                dto.setTax_collector(crs.getString(21));
                dto.setTot_amount(crs.getBigDecimal(22));
                dto.setTransaction_date(crs.getString(23));
                dto.setTransaction_no(crs.getString(24).substring(0,10));
                dto.setUser_id(crs.getLong(25));
                dto.setWard_name(crs.getString(26));
                dto.setFor_year(crs.getString(27));
                counterCollectionReport.add(dto);
            }
        }catch (Exception e) {
            log.info(e.getMessage());
        }
        return counterCollectionReport;
    }

    @Override
    public CollectionViewByPayModeDTO getCollectionByPayMode(String dateFrom, String dateTo, String ward_id,String user_id) {
        Timestamp fromdate = Timestamp.valueOf(dateFrom+" 00:00:00.00");
        Timestamp todate = Timestamp.valueOf(dateTo+" 23:59:59.999999999");
        log.info("ward id is .............."+ward_id);
        String jpql = "";
        CollectionViewByPayModeDTO  accountDescription = new CollectionViewByPayModeDTO();
         BigDecimal property_tax=new BigDecimal(0.00);
         BigDecimal sanitation_tax=new BigDecimal(0.00);
         BigDecimal composite_tax=new BigDecimal(0.00);
         BigDecimal common_water_tax=new BigDecimal(0.00);
         BigDecimal personal_water_tax=new BigDecimal(0.00);
         BigDecimal education_cess=new BigDecimal(0.00);
         BigDecimal diff_amount=new BigDecimal(0.00);
         BigDecimal penal_charge=new BigDecimal(0.00);
         BigDecimal rain_wtr_harvest=new BigDecimal(0.00);
         BigDecimal form_fee=new BigDecimal(0.00);
         BigDecimal penalty=new BigDecimal(0.00);
         BigDecimal discount=new BigDecimal(0.00);
         BigDecimal adjustment=new BigDecimal(0.00);
         BigDecimal account_total=new BigDecimal(0.00);
         BigDecimal payment_mode_total=new BigDecimal(0.00);
         BigDecimal payment_mode_net_total=new BigDecimal(0.00);

         // in future, we can write this query by below way with create view table.
         /*
         select a.payment_mode,count(a.payment_mode) as transaction_count, sum(a.payable_amt) as total_collection,sum(c.property_tax) as property_tax,sum(c.sanitation_tax) as sanitation_tax, sum(c.composite_tax) as composite_tax,sum(c.common_wtr_tax) as common_wtr_tax,sum(c.personel_wtr_tax) as personel_wtr_tax,
sum(c.education_cess) as education_cess,sum(a.difference_amount) as difference_amount, sum(c.penal_charge) as penal_charge,sum(c.rain_harvest_charge) as rain_harvest_charge,sum(a.form_fee) as form_fee,sum(c.penalty) as penalty,sum(a.discount) as discount
from public.tbl_prop_tranction_mstr a inner join public.tbl_prop_collection_mstr c on a.id=c.transaction_id where a.stampdate between
'2022-01-01 00:00:00.00' and '2023-08-01'  and a.status=1 group by  a.payment_mode;
          */
        if(dateFrom==null || dateTo==null || ward_id ==null || user_id==null) {
            throw new BadRequestException("Invalid Input");
        }

        Map<Object, Object> inputParameter=new LinkedHashMap<>();
        Long userId=null;
        Long wardId=null;
        if(ward_id.equalsIgnoreCase("All")) {
            wardId=0L;
        }
        else if(!ward_id.equalsIgnoreCase("All")) {
            wardId=Long.parseLong(ward_id);
        }
        if(user_id.equalsIgnoreCase("All")) {
            userId=-1L;
        } else if (!user_id.equalsIgnoreCase("All")) {
            userId=Long.parseLong(user_id);
        }

            inputParameter.put(fromdate, fromdate);
            inputParameter.put(todate, todate);
            inputParameter.put(wardId, wardId);
            inputParameter.put(userId,userId);
            //log.info(String.valueOf(inputParameter.size()));
        List<ModeWiseCollectionDto> collectionViewByPayModeDTOS= new ArrayList<>();
            try {
                CachedRowSet crs = DBFunctionCall.simpleFunctionCallWithParameter("public.get_payment_mode_collection_report(?,?,?,?)", 1, inputParameter);
                while (crs.next()) {
                    ModeWiseCollectionDto collectionViewByPayModeDTO= new ModeWiseCollectionDto();
                    collectionViewByPayModeDTO.setPayment_mode(crs.getString(1));
                    collectionViewByPayModeDTO.setTotal_collection(crs.getBigDecimal(2));
                    collectionViewByPayModeDTOS.add(collectionViewByPayModeDTO);
                    property_tax=property_tax.add(crs.getBigDecimal(3));
                    sanitation_tax=sanitation_tax.add(crs.getBigDecimal(4));
                    composite_tax=composite_tax.add(crs.getBigDecimal(5));
                    common_water_tax=common_water_tax.add(crs.getBigDecimal(6));
                    personal_water_tax=personal_water_tax.add(crs.getBigDecimal(7));
                    education_cess=education_cess.add(crs.getBigDecimal(8));
                    diff_amount=diff_amount.add(crs.getBigDecimal(9));
                    penal_charge=penal_charge.add(crs.getBigDecimal(10));
                    rain_wtr_harvest = rain_wtr_harvest.add(crs.getBigDecimal(11));
                    form_fee=form_fee.add(crs.getBigDecimal(12));
                    penalty=penalty.add(crs.getBigDecimal(13));
                    discount=discount.add(crs.getBigDecimal(14));
                    //account_total=account_total.add((BigDecimal) dtoult[15]);
                }
            } catch (Exception e) {
                log.info(e.getMessage());
            }
       /* if(wardId.equals("All")) {
            jpql =

                   "select a.payment_mode,a.payable_amt,c.property_tax,c.sanitation_tax, c.composite_tax,c.common_wtr_tax,c.personel_wtr_tax,\n" +
                    "c.education_cess,a.difference_amount, c.penal_charge,c.rain_harvest_charge,a.form_fee,c.penalty,a.discount\n" +
                    "from public.tbl_prop_tranction_mstr a inner join public.tbl_prop_collection_mstr c on a.id=c.transaction_id where a.stampdate between" +
                    " '" + fromdate + "' and '" + todate + "'  and a.status=1";

                   // "select a.payment_mode,count(a.payment_mode) as transaction_count,sum(a.payable_amt)as total_collection  from public.tbl_prop_tranction_mstr a where a.stampdate between '" + fromdate + "' and '" + todate + "' GROUP BY a.payment_mode";
        }
        else{
         jpql =


                "select a.payment_mode,a.payable_amt,c.property_tax,c.sanitation_tax, c.composite_tax,c.common_wtr_tax,c.personel_wtr_tax,\n" +
                    "c.education_cess,a.difference_amount, c.penal_charge,c.rain_harvest_charge,a.form_fee,c.penalty,a.discount\n" +
                    "from public.tbl_prop_tranction_mstr a inner join public.tbl_prop_collection_mstr c on a.id=c.transaction_id where a.stampdate between" +
                    " '" + fromdate + "' and '" + todate + "' and a.ward_id='" + wardId + "' and a.status=1";

                    //"select a.payment_mode,count(a.payment_mode) as transaction_count,sum(a.payable_amt)as total_collection  from public.tbl_prop_tranction_mstr a where a.stampdate between '" + fromdate + "' and '" + todate + "' and a.ward_id='" + wardId + "' GROUP BY a.payment_mode";

        }
        Query query = entityManager.createNativeQuery(jpql);
        log.info("query................"+jpql);
        List<Object[]> dtoults = query.getResultList();
       List<ModeWiseCollectionDto> collectionViewByPayModeDTOS= new ArrayList<>();
        //CollectionReportByPaymentModeResponse results=new CollectionReportByPaymentModeResponse();

        for (Object[] dtoult : dtoults) {

          ModeWiseCollectionDto collectionViewByPayModeDTO= new ModeWiseCollectionDto();
            collectionViewByPayModeDTO.setPayment_mode((String) dtoult[0]);
            collectionViewByPayModeDTO.setTotal_collection((BigDecimal) dtoult[1]);
            collectionViewByPayModeDTOS.add(collectionViewByPayModeDTO);
            property_tax=property_tax.add((BigDecimal) dtoult[2]);
            sanitation_tax=sanitation_tax.add((BigDecimal) dtoult[3]);
            composite_tax=composite_tax.add((BigDecimal) dtoult[4]);
            common_water_tax=common_water_tax.add((BigDecimal) dtoult[5]);
            personal_water_tax=personal_water_tax.add((BigDecimal) dtoult[6]);
            education_cess=education_cess.add((BigDecimal) dtoult[7]);
            diff_amount=diff_amount.add((BigDecimal) dtoult[8]);
            penal_charge=penal_charge.add((BigDecimal) dtoult[9]);
            if(dtoult[10]!=null) {
                rain_wtr_harvest = rain_wtr_harvest.add((BigDecimal) dtoult[10]);
            }
            form_fee=form_fee.add((BigDecimal) dtoult[11]);
            penalty=penalty.add((BigDecimal) dtoult[12]);
            discount=discount.add((BigDecimal) dtoult[13]);
            //adjustment=adjustment.add((BigDecimal) dtoult[14]);
            //account_total=account_total.add((BigDecimal) dtoult[15]);
        }*/

      List<ModeWiseCollectionDto> results = new ArrayList<>();
        List<String> paymentMode=collectionViewByPayModeDTOS.stream().map(v->v.getPayment_mode()).distinct().collect(Collectors.toList());
        for(String payMode : paymentMode) {
            ModeWiseCollectionDto dto = new ModeWiseCollectionDto();
            dto.setPayment_mode(payMode);
            dto.setTotal_collection(collectionViewByPayModeDTOS.stream().filter(v->v.getPayment_mode().equals(payMode)).map(v->v.getTotal_collection()).reduce(BigDecimal.ZERO,BigDecimal::add));
            dto.setTransaction_count(BigInteger.valueOf(collectionViewByPayModeDTOS.stream().filter(f->f.getPayment_mode().equals(payMode)).count()));
            results.add(dto);
        }

        payment_mode_total=results.stream().map(v->v.getTotal_collection()).reduce(BigDecimal::add).orElse(new BigDecimal(0.00));

        accountDescription.setModeWise(results);
        accountDescription.setProperty_tax(property_tax);
        accountDescription.setSanitation_tax(sanitation_tax);
        accountDescription.setComposite_tax(composite_tax);
        accountDescription.setCommon_water_tax(common_water_tax);
        accountDescription.setPersonal_water_tax(personal_water_tax);
        accountDescription.setEducation_cess(education_cess);
        accountDescription.setDiff_amount(diff_amount);
        accountDescription.setPenal_charge(penal_charge);
        accountDescription.setRain_wtr_harvest(rain_wtr_harvest);
        accountDescription.setForm_fee(form_fee);
        accountDescription.setPenalty(penalty);
        accountDescription.setDiscount(discount);
        accountDescription.setAdjustment(adjustment); /// Adjustment column not found, need to know which table's column it's belong
      account_total=account_total.add(property_tax).add(sanitation_tax).add(composite_tax).add(common_water_tax)
                .add(personal_water_tax).add(education_cess).add(diff_amount).add(penal_charge).add(rain_wtr_harvest).add(form_fee)
                .add(penalty).subtract(discount).subtract(adjustment);
        accountDescription.setAccount_total(account_total);
        accountDescription.setPayment_mode_total_collection(payment_mode_total);

        return accountDescription;
    }

    @Override
    public List<WardWiseDemandViewDTO> getDemandReportByWard(String dateFrom, String dateTo,String wardId) {
        Timestamp fromdate = Timestamp.valueOf(dateFrom+" 00:00:00.00");
        Timestamp todate = Timestamp.valueOf(dateTo+" 23:59:59.999999999");
        String jpql =null;

        if(wardId.equalsIgnoreCase("All")) {
            jpql= "select f.ward_name,d.property_no,e.owner_name,e.guardian_name,e.mobile_no,e.owner_address\n" +
                    ",sum(a.penalty) as penalty,(sum(a.total_amount)-sum(a.penalty)) as demand_amount,sum(a.total_amount) as total_amount \n" +
                    ",g.fy_name from tbl_prop_demand_dtls a join tbl_property_mstr d on d.id=a.prop_id join tbl_prop_owner_details e on\n" +
                    "e.prop_id=a.prop_id join tbl_ward_mstr f on f.id=d.ward_id join tbl_fy g on g.id=a.entry_fy_id\n" +
                    "where a.paid_status=0  and  a.stampdate between '" + fromdate + "' and '" + todate + "' group by f.ward_name,d.property_no,e.owner_name,\n" +
                    "e.guardian_name,e.mobile_no,e.owner_address,g.fy_name ";
        }
        else if(dateFrom!=null && dateTo !=null && !wardId.equalsIgnoreCase("All")) {
            jpql= "select f.ward_name,d.property_no,e.owner_name,e.guardian_name,e.mobile_no,e.owner_address\n" +
                    ",sum(a.penalty) as penalty,(sum(a.total_amount)-sum(a.penalty)) as demand_amount,sum(a.total_amount) as total_amount \n" +
                    ",g.fy_name from tbl_prop_demand_dtls a join tbl_property_mstr d on d.id=a.prop_id join tbl_prop_owner_details e on\n" +
                    "e.prop_id=a.prop_id join tbl_ward_mstr f on f.id=d.ward_id join tbl_fy g on g.id=a.entry_fy_id\n" +
                    "where a.paid_status=0 and a.ward_id='"+wardId+"' and  a.stampdate between '" + fromdate + "' and '" + todate + "' group by f.ward_name,d.property_no,e.owner_name,\n" +
                    "e.guardian_name,e.mobile_no,e.owner_address,g.fy_name ";
        }
        Query query = entityManager.createNativeQuery(jpql);
        log.info("query................"+jpql);
        List<Object[]> dtoults = query.getResultList();
        List<WardWiseDemandViewDTO> wardWiseDemandViewDTOS= new ArrayList<>();
        for (Object[] dtoult : dtoults) {
            WardWiseDemandViewDTO wardWiseDemandViewDTO=new WardWiseDemandViewDTO();
            wardWiseDemandViewDTO.setWard_name((String) dtoult[0]);
            wardWiseDemandViewDTO.setProperty_no((String) dtoult[1]);
            wardWiseDemandViewDTO.setOwner_name((String) dtoult[2]);
            wardWiseDemandViewDTO.setGuardian_name((String) dtoult[3]);
            wardWiseDemandViewDTO.setMobile_no((BigInteger) dtoult[4]);
            wardWiseDemandViewDTO.setOwner_address((String) dtoult[5]);
            wardWiseDemandViewDTO.setPenalty((BigDecimal) dtoult[6]);
            wardWiseDemandViewDTO.setDemand_amount((BigDecimal) dtoult[7]);
            wardWiseDemandViewDTO.setTotal_amount((BigDecimal) dtoult[8]);
            wardWiseDemandViewDTO.setFy_name((String) dtoult[9]);
            wardWiseDemandViewDTOS.add(wardWiseDemandViewDTO);
        }
        return wardWiseDemandViewDTOS;
    }

    @Override
    public List<TeamWiseCollectionDTO> getCollectionByTeamWise(String dateFrom, String dateTo, String userId) {
        Timestamp fromdate = Timestamp.valueOf(dateFrom+" 00:00:00.00");
        Timestamp todate = Timestamp.valueOf(dateTo+" 23:59:59.999999999");
        String jpql = "";
        if(userId.equals("")||userId.equals(null)|| userId.equals("All")) {
            jpql = "select d.collector_name,d.user_type," +
                    "SUM(CASE WHEN payment_mode = 'Cash' THEN d.total_collection ELSE 0 END) AS CashTotal,\n" +
                    "SUM(CASE WHEN payment_mode = 'Card' THEN d.total_collection ELSE 0 END) AS CardTotal,\n" +
                    "SUM(CASE WHEN payment_mode = 'Cheque' THEN d.total_collection ELSE 0 END) AS ChequeTotal,\n" +
                    "SUM(CASE WHEN payment_mode = 'DD' THEN d.total_collection ELSE 0 END) AS DDTotal,\n" +
                    "SUM(CASE WHEN payment_mode = 'RTGS' THEN d.total_collection ELSE 0 END) AS RTGSTotal,\n" +
                    "SUM(CASE WHEN payment_mode = 'NEFT' THEN d.total_collection ELSE 0 END) AS NEFTTotal,\n " +
                    "sum(d.total_collection)as total_collection ,count(d.property_count) as property_count  from tbl_teamcollection_view d where d.stampdate between '" + fromdate + "' and '" + todate + "' GROUP BY collector_name,user_type";
        }
        else{
            jpql = "select d.collector_name,d.user_type," +
                    "SUM(CASE WHEN payment_mode = 'Cash' THEN d.total_collection ELSE 0 END) AS CashTotal,\n" +
                    "SUM(CASE WHEN payment_mode = 'Card' THEN d.total_collection ELSE 0 END) AS CardTotal,\n" +
                    "SUM(CASE WHEN payment_mode = 'Cheque' THEN d.total_collection ELSE 0 END) AS ChequeTotal,\n" +
                    "SUM(CASE WHEN payment_mode = 'DD' THEN d.total_collection ELSE 0 END) AS DDTotal,\n" +
                    "SUM(CASE WHEN payment_mode = 'RTGS' THEN d.total_collection ELSE 0 END) AS RTGSTotal,\n" +
                    "SUM(CASE WHEN payment_mode = 'NEFT' THEN d.total_collection ELSE 0 END) AS NEFTTotal,\n" +
                    "sum(d.total_collection)as total_collection ,count(d.property_count) as property_count  from tbl_teamcollection_view d where d.stampdate between '" + fromdate + "' and '" + todate + "' and d.user_id='" + userId + "' GROUP BY collector_name,user_type";
        }
        Query query = entityManager.createNativeQuery(jpql);
        log.info("query................"+jpql);
        List<Object[]> dtoults = query.getResultList();
        List<TeamWiseCollectionDTO> teamWiseCollectionDTOS= new ArrayList<>();
        for (Object[] dtoult : dtoults) {
            TeamWiseCollectionDTO teamWiseCollectionDTO=new TeamWiseCollectionDTO();
            teamWiseCollectionDTO.setCollector_name((String) dtoult[0]);
            teamWiseCollectionDTO.setUser_type((String) dtoult[1]);
            teamWiseCollectionDTO.setCashTotal((BigDecimal) dtoult[2]);
            teamWiseCollectionDTO.setCardTotal((BigDecimal) dtoult[3]);
            teamWiseCollectionDTO.setChequeTotal((BigDecimal) dtoult[4]);
            teamWiseCollectionDTO.setDDTotal((BigDecimal) dtoult[5]);
            teamWiseCollectionDTO.setRTGSTotal((BigDecimal) dtoult[6]);
            teamWiseCollectionDTO.setNEFTTotal((BigDecimal) dtoult[7]);
            teamWiseCollectionDTO.setTotal_collection((BigDecimal) dtoult[8]);
            teamWiseCollectionDTO.setProperty_count((BigInteger) dtoult[9]);
            teamWiseCollectionDTOS.add(teamWiseCollectionDTO);
        }
        return teamWiseCollectionDTOS;
    }

    @Override
    public List<CollectionByPayModeDTO> getCollectionByPayMode1(String dateFrom, String dateTo, BigInteger wardId) {
        Timestamp fromdate = Timestamp.valueOf(dateFrom+" 00:00:00.00");
        Timestamp todate = Timestamp.valueOf(dateTo+" 23:59:59.999999999");
        log.info("ward id is .............."+wardId);
        String jpql = "";
        if(wardId.equals("")||wardId.equals(null)) {
            jpql = "select\n" +
                    "    sum(a.property_tax) AS property_tax,\n" +
                    "\tsum(a.sanitation_tax) AS sanitation_tax,\n" +
                    "    sum(a.composite_tax) AS composite_tax,\n" +
                    "\tsum(a.common_wtr_tax) AS common_wtr_tax,\n" +
                    "\tsum(a.personal_wtr_tax) AS personal_wtr_tax,\n" +
                    "    sum(a.education_cess) AS education_cess,\n" +
                    "\tsum(a.penal_charge) AS penal_charge,\n" +
                    "    sum(a.penalty) AS penalty,\n" +
                    "    sum(a.rain_harvest_charge) AS rain_harvest_charge,\n" +
                    "\tsum(a.oth_tot_amount) + sum(a.oth_penalty) as diff_amount,\n" +
                    "    sum(3) AS form_fee,\n" +
                    "    sum(0) AS discount,\n" +
                    "    sum(0) AS adv_adjust,\n" +
                    "    sum(a.total_amount) AS tot_amount  FROM tbl_prop_demand_dtls a where a.paid_status=1 and  a.stampdate between '" + fromdate + "' and '" + todate + "'";
        }
        else{
            jpql = "select\n" +
                    "    sum(a.property_tax) AS property_tax,\n" +
                    "\tsum(a.sanitation_tax) AS sanitation_tax,\n" +
                    "    sum(a.composite_tax) AS composite_tax,\n" +
                    "\tsum(a.common_wtr_tax) AS common_wtr_tax,\n" +
                    "\tsum(a.personal_wtr_tax) AS personal_wtr_tax,\n" +
                    "    sum(a.education_cess) AS education_cess,\n" +
                    "\tsum(a.penal_charge) AS penal_charge,\n" +
                    "    sum(a.penalty) AS penalty,\n" +
                    "    sum(a.rain_harvest_charge) AS rain_harvest_charge,\n" +
                    "\tsum(a.oth_tot_amount) + sum(a.oth_penalty) as diff_amount,\n" +
                    "    sum(3) AS form_fee,\n" +
                    "    sum(0) AS discount,\n" +
                    "    sum(0) AS adv_adjust \n" +
                    "    FROM tbl_prop_demand_dtls a where a.ward_id='" + wardId + "' and a.paid_status=1 and a.stampdate between '" + fromdate + "' and '" + todate + "' ";

        }
        Query query = entityManager.createNativeQuery(jpql);
        //log.info("query................"+jpql);
        List<Object[]> dtoults = query.getResultList();
        List<CollectionByPayModeDTO> collectionByPayModeDTOS= new ArrayList<>();
        for (Object[] dtoult : dtoults) {
            CollectionByPayModeDTO collectionByPayModeDTO=new CollectionByPayModeDTO();
            collectionByPayModeDTO.setProperty_tax((BigDecimal) dtoult[0]);
            collectionByPayModeDTO.setSanitation_tax((BigDecimal) dtoult[1]);
            collectionByPayModeDTO.setComposite_tax((BigDecimal) dtoult[2]);
            collectionByPayModeDTO.setCommon_wtr_tax((BigDecimal) dtoult[3]);
            collectionByPayModeDTO.setPersonal_wtr_tax((BigDecimal) dtoult[4]);
            collectionByPayModeDTO.setEducation_cess((BigDecimal) dtoult[5]);
            collectionByPayModeDTO.setPenal_charge((BigDecimal) dtoult[6]);
            collectionByPayModeDTO.setPenalty((BigDecimal) dtoult[7]);
            collectionByPayModeDTO.setRain_harvest_charge((BigDecimal) dtoult[8]);
            collectionByPayModeDTO.setDiff_amount((BigDecimal) dtoult[9]);
            collectionByPayModeDTO.setForm_fee((BigInteger) dtoult[10]);
            collectionByPayModeDTO.setDiscount((BigInteger) dtoult[11]);
            collectionByPayModeDTO.setAdv_adjust((BigInteger) dtoult[12]);
            collectionByPayModeDTOS.add(collectionByPayModeDTO);
        }
        return collectionByPayModeDTOS;
    }

    @Override
    public List<CollectionBouncedChequeDDResponseDto> bounceReport(String dateFrom, String dateTo, Long wardId, Long userId) {
        List<CollectionBouncedChequeDDResponseDto> response=new ArrayList<>();
        Timestamp fromdate = Timestamp.valueOf(dateFrom+" 00:00:00.00");
        Timestamp todate = Timestamp.valueOf(dateTo+" 23:59:59.999999999");
        Map<Object, Object> inputParameter=new LinkedHashMap<>();
        inputParameter.put(fromdate, fromdate);
        inputParameter.put(todate, todate);
        inputParameter.put(wardId, wardId);
        inputParameter.put(userId,userId);
        CollectionBouncedChequeDDResponseDto dto = new CollectionBouncedChequeDDResponseDto();

        try {
            CachedRowSet crs = DBFunctionCall.simpleFunctionCallWithParameter("public.get_cheque_bounce_counter_collection_report(?,?,?,?)", 1, inputParameter);
            while (crs.next()) {

               /* dto.setWard_id(crs.getLong(1));
                dto.setAcomposite_tax(crs.getBigDecimal(2));
                dto.setAproperty_tax(crs.getBigDecimal(3));
                dto.setBank_name(crs.getString(4));
                dto.setBranch_name(crs.getString(5));
                dto.setCheque_dd_aprcode(crs.getString(6));
                dto.setComposite_tax(crs.getBigDecimal(7));
                dto.setDiscount(crs.getBigDecimal(8));
                dto.setEducation_cess(crs.getBigDecimal(9));
                dto.setAeducation_cess(crs.getBigDecimal(10));
                dto.setForm_fee(crs.getBigDecimal(11));
                dto.setMobile_no(crs.getLong(12));
                dto.setOwner_name(crs.getString(13));*/
                dto.setPayment_mode(crs.getString(1));
                dto.setNo_of_transaction(crs.getLong(2));
                /*dto.setPenal_charge(crs.getBigDecimal(15));
                dto.setPenalty(crs.getBigDecimal(16));
                dto.setProperty_no(crs.getString(17));
                dto.setProperty_tax(crs.getBigDecimal(18));
                dto.setRain_harvest_charge(crs.getBigDecimal(19));
                dto.setStampdate(crs.getString(20));
                dto.setTax_collector(crs.getString(21));*/
                dto.setTot_amount(crs.getBigDecimal(3));
               /* dto.setTransaction_date(crs.getString(23));
                dto.setUser_id(crs.getLong(24));
                dto.setTransaction_no(crs.getString(25));
                dto.setWard_name(crs.getString(26));
                dto.setFor_year(crs.getString(27));*/
                response.add(dto);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return response;

    }

	@Override
	public List<Object> getAllModule(String date_from, String date_to) {

		List<Object[]> dto = propDemandDetailsDao.getArrearAndCurrentAmount(date_from,date_to);
		 LocalDate date_frm = LocalDate.parse(date_from);
		 LocalDate date_t = LocalDate.parse(date_to);

	        // Formatting the date to "31/12/2024" format
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        String formattedDateStart = date_frm.format(formatter);
	        String dormatredDateEnd = date_t.format(formatter);
	        
	       // List<Object[]> mergedList = new ArrayList<>();
		List<Object[]>waterdto = consumerDemandDetailsdao.getArrearAndCurrentAmount(formattedDateStart.toString(), dormatredDateEnd.toString());
	
//		ConsumerDemandDetialsDTO cddDto = new ConsumerDemandDetialsDTO();
//		PropDemandDetailsDTO newDto = new PropDemandDetailsDTO();
//		for(Object [] printdto: dto) {
//			
//			
//			newDto.setProperty_tax_arrear( (BigDecimal) printdto[0] );
//			newDto.setCommon_wtr_tax_arrear((BigDecimal)printdto[1] );
//			newDto.setComposite_tax_arrear((BigDecimal) printdto[2]);
//			newDto.setProperty_tax_current((BigDecimal) printdto[3]);
//			newDto.setCommon_wtr_tax_current((BigDecimal) printdto[4]);
//			newDto.setComposite_tax_current((BigDecimal) printdto[5]);
//			newDto.setPenal_charge(new BigDecimal(0));
//			newDto.setPenalty((BigDecimal) printdto[7]);
//		
//		}
//		
//		for(Object [] printDto : waterdto) {
//			cddDto.setWater_CurrentAmount((BigDecimal) printDto[0]);
//			cddDto.setWater_ArrearAmount((BigDecimal) printDto[1]);
//			
//		}
		
		// Assuming these lists are of the same size
		List<Object> mergedList = new ArrayList<>();
		GetAllModuleDTO getAllModule = new GetAllModuleDTO();

		for (int i = 0; i < dto.size(); i++) {
			ConsumerDemandDetialsDTO cddDto = new ConsumerDemandDetialsDTO();
			PropDemandDetailsDTO newDto = new PropDemandDetailsDTO();
		    Object[] printdto = dto.get(i);
		    Object[] printWaterDto = waterdto.get(i);
		    

		    getAllModule.setProperty_tax_arrear((BigDecimal) printdto[0]);
		    getAllModule.setCommon_wtr_tax_arrear((BigDecimal) printdto[1]);
		    getAllModule.setComposite_tax_arrear((BigDecimal) printdto[2]);
		    getAllModule.setProperty_tax_current((BigDecimal) printdto[3]);
		    getAllModule.setCommon_wtr_tax_current((BigDecimal) printdto[4]);
		    getAllModule.setComposite_tax_current((BigDecimal) printdto[5]);
		    getAllModule.setPenal_charge(new BigDecimal(0));
		    getAllModule.setPenalty((BigDecimal) printdto[7]);

		    getAllModule.setWater_CurrentAmount((BigDecimal) printWaterDto[0]);
		    getAllModule.setWater_ArrearAmount((BigDecimal) printWaterDto[1]);
		    
		    mergedList.add(getAllModule);
		}
		
		
		//log.info("Property Tax Current : {} ,Property Tax Arrear : {} ,Composite Tax Arrear : {} ,Composite Tax Current : {} ,Common Tax Arrear : {} ,Common Tax Current : {}, PenalCharge : {}, Penalty : {}  ",newDto.getProperty_tax_arrear(),newDto.getProperty_tax_current(), newDto.getComposite_tax_arrear(), newDto.getComposite_tax_current(), newDto.getCommon_wtr_tax_arrear(), newDto.getCommon_wtr_tax_current(), newDto.getPenalCharge(), newDto.getPenalty());
		
		return mergedList;
	}

//	@Override
//	public List<Object> getSummaryData(String date_from, String date_to) {
//		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		 
//		Timestamp todayStart = Timestamp.valueOf(date_from.formatted(formatter)+" 00:00:00.00");
//        Timestamp todayEnd = Timestamp.valueOf(date_to.formatted(formatter)+" 23:59:59.999999999");
//
//		List<Object[]> getSummary = dailyAssessmentPropertyReport.getSummaryDataRepo(todayStart, todayEnd);
//		List<Object> mergedList = new ArrayList<>();
//		for (int i = 0; i < getSummary.size(); i++) {
//			DailyAssessmentReportDTO dto = new DailyAssessmentReportDTO();
//			
//			Object[] printDto = getSummary.get(i);
//			
//			dto.setWardid( printDto[0].toString());
//			
//			dto.setAssessmentType(  printDto[1].toString());
//			dto.setProertyNo(printDto[2].toString());
//			dto.setPropertyAddress(printDto[3].toString());
//			dto.setEntryDate(printDto[4].toString());
//			dto.setOwnerName(printDto[5].toString());
//			dto.setUserId( (Long) printDto[6]);
//			dto.setTotal_amount_sum((BigDecimal) printDto[7]);
//			dto.setCommon_wtr_tax_sum((BigDecimal) printDto[8]);
//			dto.setEducation_cess_sum((BigDecimal) printDto[9]);
//			dto.setUser_name(printDto[10].toString());
//			
//			mergedList.add(dto);			
//		}
//	
//		return mergedList;
//	}
	
	@Override
	public List<DailyAssessmentReportDTO> getSummaryData(String date_from, String date_to) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	    Timestamp todayStart = Timestamp.valueOf(date_from.formatted(formatter) + " 00:00:00.00");
	    Timestamp todayEnd = Timestamp.valueOf(date_to.formatted(formatter) + " 23:59:59.999999999");

	    List<Object[]> getSummary = dailyAssessmentPropertyReport.getSummaryDataRepo(todayStart, todayEnd);
	    List<DailyAssessmentReportDTO> mergedList = new ArrayList<>();
	    
	    for (Object[] printDto : getSummary) {
	    	DailyAssessmentReportDTO singleReport = new DailyAssessmentReportDTO();
	        singleReport.setWardid( printDto[0].toString());
	        singleReport.setAssessmentType(printDto[1].toString());
	        singleReport.setProertyNo(printDto[2].toString());
	        singleReport.setPropertyAddress(printDto[3].toString());
	        singleReport.setEntryDate(printDto[4].toString());
	        singleReport.setOwnerName(printDto[5].toString());
	        singleReport.setUserId( (Long) printDto[6]);
	        singleReport.setTotal_amount_sum( (BigDecimal) printDto[7] );
	        singleReport.setCommon_wtr_tax_sum((BigDecimal) printDto[8]);
	        singleReport.setEducation_cess_sum((BigDecimal) printDto[9]);
	        singleReport.setUser_name(printDto[10].toString());
	        mergedList.add(singleReport);
	        singleReport=null;
	    }

	    return mergedList;
	}


}
