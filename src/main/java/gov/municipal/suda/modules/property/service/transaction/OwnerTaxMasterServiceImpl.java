package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.*;
import gov.municipal.suda.modules.property.dao.transaction.DemandDao;
import gov.municipal.suda.modules.property.dao.transaction.PropertyARVDetailsDao;
import gov.municipal.suda.modules.property.dto.FloorWiseOwnerTaxDTO;
import gov.municipal.suda.modules.property.dto.NewDemandRequestDTO;
import gov.municipal.suda.modules.property.dto.OwnerTaxDTO;
import gov.municipal.suda.modules.property.model.master.*;
import gov.municipal.suda.modules.property.model.transaction.CompositeTax;
import gov.municipal.suda.modules.property.service.master.ArvRangeService;
import gov.municipal.suda.util.Calculations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OwnerTaxMasterServiceImpl implements OwnerTaxMasterService {
	@Autowired
	PropertyARVDetailsDao propertyARVDetailsDao;
	@Autowired
	OwnerTaxMasterDao ownerTaxMasterDao;
	@Autowired
	ArvRangeDao arvRangeDao;
	@Autowired
	EduAndCompositRateDao eduAndCompositRateDao;
	@Autowired
	EffectYearRateChangeDao effectYearRateChangeDao;
	@Autowired
	UtilDao utilDao;
	@Autowired
	DemandService demandService;
	@Autowired
	FinYearDao finYearDao;
	@Autowired
	DemandDao demandDao;
	@Autowired
	ArvRangeService arvService;
	@Autowired
	OwnerDetailsDao ownerDetailsDao;

	@Autowired
	CompositeTaxService compositeTaxService;
	
	private static void addDetailsMasterService(List<Map<String, Object>> detailsList,String constType, String occupancyType, String year, BigDecimal value) {
		Map<String, Object> details = new HashMap<>();
		details.put("ConstType", constType);
		details.put("OccupancyType", occupancyType);
		details.put("Year", year);
		details.put("Value", value);
		detailsList.add(details);
		}
	private static void updatedDetailsMasterService(List<Map<String, Object>> detailsList,String constType, String occupancyType, String year, BigDecimal value,BigDecimal tax_Water_Educ) {
		Map<String, Object> details = new HashMap<>();
		details.put("ConstType", constType);
		details.put("OccupancyType", occupancyType);
		details.put("Year", year);
		details.put("Value", value);
		details.put("tax_Water_Educ", tax_Water_Educ);
		detailsList.add(details);
		}

	@Override
	// @Transactional(rollbackOn = BadRequestException.class)
	public String create(List<FloorWiseOwnerTaxDTO> floorWiseOwnerTax, Map< String, Integer>calculatedAllARV,
			Map<String, String> uses_type, String occup_type) throws BadRequestException {
		log.info("SAFARVDetails TABLE JSON {}", floorWiseOwnerTax);
		log.info("SAFARVDetails TABLE Response Size {}", floorWiseOwnerTax.size());
		BigDecimal totalCompositeTax = new BigDecimal(0);
		BigDecimal totalEducationCess = new BigDecimal(0);
		BigDecimal new_composite_tax = new BigDecimal(0.00);
		BigDecimal arvCalcInsideLoop = new BigDecimal(0);
		BigDecimal commomWatr_tax = new BigDecimal(0.00);
		BigDecimal storeCommonWatr_tax = new BigDecimal(0.00);
		BigDecimal totalBeforePropertyTax = new BigDecimal(0);
		BigDecimal totalPropertyTax = new BigDecimal(0);
		BigDecimal arv = new BigDecimal(0.00);
		String entryEffect_year = null;
		Long entry_effect_fy_id = 0L;
		String entry_effect_year = null;
		String occupancyTypeOfRunningLoop ="";		
		Long prop_id = 0L;
		List<BigDecimal> sumNetArv= new ArrayList<BigDecimal>();
		List<BigDecimal> sumEduTax =new ArrayList<BigDecimal>();
		List<BigDecimal> sumCommonWtr_Tax =new ArrayList<BigDecimal>();
//		List<Map<String ,BigDecimal>> seprateAmtForFloor = new ArrayList<>();
		List<Map<String, Object>> detailsOfYearWise = new ArrayList<>();
		List<Map<String, Object>> updatedDetailsOfYearWise = new ArrayList<>();
		
		BigDecimal totalArvCalculation=new BigDecimal(0.00);
		
		
		Map<String, List<Optional<BigDecimal>>> combineYearWiseARVMap = new HashMap<>();
		Map<String, List<BigDecimal>> combineYearWiseARVMap1 = new HashMap<>();

		List<String> listOfYear = new ArrayList<>();
		for (FloorWiseOwnerTaxDTO floor : floorWiseOwnerTax) {
			for (int x = 0; x < floor.getFloorWiseOwnerTax().size(); x++) {
				listOfYear.add(floor.getFloorWiseOwnerTax().get(x).getYear());
				addDetailsMasterService(detailsOfYearWise, floor.getConstruction_type(), floor.getOccupancy_type(),floor.getFloorWiseOwnerTax().get(x).getYear(), floor.getFloorWiseOwnerTax().get(x).getCalc_arv());
			}
		}
//		Set<Integer> pickUniqueYear = listOfYear.stream().map(v -> Integer.parseInt(v)).collect(Collectors.toSet());
//		
		Set<Integer> pickUniqueYear = listOfYear.stream().map(Integer::parseInt).distinct().collect(Collectors.toSet());
		
		
		for (Integer uniqueYear : pickUniqueYear) {
			combineYearWiseARVMap.put(uniqueYear.toString(),floorWiseOwnerTax.stream().map(v -> v.getFloorWiseOwnerTax().stream().filter(t -> Integer.parseInt(t.getYear()) == uniqueYear).map(z -> z.getCalc_arv()).reduce(BigDecimal::add)).collect(Collectors.toList()));
		}

		log.info("Combine Year Wise ARV MAP ======> Line 88 {}", combineYearWiseARVMap);

		Map<String, Optional<BigDecimal>> combineYearWiseNetARV = new HashMap<>();
		for (Map.Entry<String, List<Optional<BigDecimal>>> entry : combineYearWiseARVMap.entrySet()) {
			combineYearWiseNetARV.put(entry.getKey(),entry.getValue().stream().filter(v -> v.isPresent()).map(x -> x.get()).reduce(BigDecimal::add));
		}

		log.info("NET ARV YEAR WISE ==============> {}", combineYearWiseNetARV);

		log.info("All Records ======> Line 99 =====> {}", combineYearWiseARVMap);
		log.info("Final count ======> Line 100 ======>  {}", combineYearWiseNetARV);
		List<OwnerTaxMasterBean> ownerTaxList = new ArrayList<>();
		try {
			List<NewDemandRequestDTO> newDemandList = new ArrayList<>();
			BigDecimal tempBeforePropertyTax = new BigDecimal(0.00);
			BigDecimal tempPropertyTax = new BigDecimal(0.00);
			String const_type= "";
			
			
			
			BigDecimal rebateArv = new BigDecimal(0.00);
			BigDecimal arvRangeResult = new BigDecimal(0.00);
			BigDecimal netArv = new BigDecimal(0.00);
			BigDecimal storeAllArv = new BigDecimal(0.00);
			BigDecimal addTotalEducationTax=  new BigDecimal(0.00);
			BigDecimal addTotalCommomWtrTax= new BigDecimal(0.00);
			
			FinYearBean effect_year= new FinYearBean();

			
			
			
	        for (Map<String, Object> detail : detailsOfYearWise) {

				
				prop_id = floorWiseOwnerTax.get(0).getProp_id();
				Object years = detail.get("Year");
				String oldYear =  years.toString();
				int newYear = Integer.parseInt(oldYear);
				log.info("Years value =======  {}",newYear);
				Integer getNextYear = newYear;
				Integer fromYear = newYear - 1;
				List<FinYearBean> finAllYear = finYearDao.findAll();
				Integer from_year = newYear - 1;
				Integer nextFrom_year = newYear;
				String convertFinYear = fromYear + "-" + getNextYear;
//				entryEffect_year = from_year + "-" + nextFrom_year;
				log.info("Effect Year{} ", convertFinYear);
				effect_year = finYearDao.getFinIdByFinYear(convertFinYear);
				entry_effect_fy_id = finAllYear.get(finAllYear.size() - 1).getId();
				entry_effect_year = finAllYear.get(finAllYear.size() - 1).getFy_name();
				
				String doe = null;
				
				// newDoe Implemetation
				if (from_year <= 1997) {
				doe = "1997-04-01";
				
				} else if (from_year >= 1998 && from_year < 2000) {
				doe = "1997-04-01";
				} else if (from_year >= 2000 && from_year <= 2001) {
				doe = "2001-04-01";
				} else if (from_year >= 2002 && from_year <= 2004) {
				doe = "2002-04-01";
				} else if (from_year >= 2005 && from_year <= 2010) {
				doe = "2005-04-01";
				} else if (from_year >= 2011) {
				doe = "2018-04-01";
				}
				
				ArvRangeBean arvRange = null;
				String tax = "";
				
				
				
				Optional<BigDecimal> valueOptional = combineYearWiseNetARV.get(oldYear);
				
				BigDecimal value = valueOptional.get();
				
				BigDecimal percent = arvService.findPercentageByNumberAndDate(value.intValue(), doe);
				
				percent = percent != null ? percent : BigDecimal.ZERO;
				log.info("Percent of tax ===== {}", percent);
				
				if(value.intValue()<6000) {
					// 12/02 my chages
					tempBeforePropertyTax = new BigDecimal(0.00);
					BigDecimal findEduandCommonWtrTax = new BigDecimal(value.intValue());
					tax = compositeTaxService.findCalculationRateByTypeAndYear(doe, "L");
					


				}else {
					tax = compositeTaxService.findCalculationRateByTypeAndYear(doe, "M");
				}
			    // Use 'value' as needed
			    System.out.println("Value for year " + newYear + ": " + value);
					
			    new_composite_tax = new BigDecimal(tax);
				
				
/**
 * ----------------------------------------------------------------------------------------------------------------------------
 * *******************************CALCULATION PART FOR RCC***************************************************
 * ----------------------------------------------------------------------------------------------------------------------------
 */
	            if(detail.get("ConstType").equals("RCC")&&detail.get("OccupancyType").equals("SELF")){
	            	log.info("INSIDE IF CONDITION OF RCC - SELF ======> 193 {} ");
	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
	                log.info("tempBeforePropertyTax OF RCC - SELF ======>  {} ",tempBeforePropertyTax);
	                rebateArv = Calculations.rebateOnResidentialProp(tempBeforePropertyTax, "SELF");
					netArv = rebateArv;
					log.info("Updated OF Net ARV -  ======>  {} ",netArv);
					updatedDetailsMasterService(updatedDetailsOfYearWise, "RCC", "SELF",oldYear, netArv,tempBeforePropertyTax);
					netArv=BigDecimal.ZERO;
						
			            
	            }
	            
	            if(detail.get("ConstType").equals("RCC")&&detail.get("OccupancyType").equals("TENANTED")){
	            	log.info("INSIDE IF CONDITION OF RCC - TENANTED ======> 201 {} ");
	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
	                log.info("tempBeforePropertyTax OF RCC - TENANTED ======>  {} ",tempBeforePropertyTax);
	                netArv = tempBeforePropertyTax;
	                log.info("Updated OF Net ARV -  ======>  {} ",netArv);
					updatedDetailsMasterService(updatedDetailsOfYearWise, "RCC", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
					netArv=BigDecimal.ZERO;
	            }
/**
 * ----------------------------------------------------------------------------------------------------------------------------
 * *******************************CALCULATION PART FOR RCC***************************************************
 * ----------------------------------------------------------------------------------------------------------------------------
 */  
	            
	            
	            
	            if(detail.get("ConstType").equals("ACC")&&detail.get("OccupancyType").equals("SELF")){
	            	log.info("INSIDE IF CONDITION OF ACC - SELF ======> 209 {} ");
	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
	                log.info("tempBeforePropertyTax OF ACC - SELF ======>  {} ",tempBeforePropertyTax);
	                rebateArv = Calculations.rebateOnResidentialProp(tempBeforePropertyTax, "SELF");
					netArv = rebateArv;
					log.info("Updated OF Net ARV -  ======>  {} ",netArv);
					updatedDetailsMasterService(updatedDetailsOfYearWise, "ACC", "SELF",oldYear, netArv,tempBeforePropertyTax);
					netArv=BigDecimal.ZERO;
	            }
	            
	            if(detail.get("ConstType").equals("ACC")&&detail.get("OccupancyType").equals("TENANTED")){
	            	log.info("INSIDE IF CONDITION OF ACC - TENANTED ======> 209 {} ");
	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
	                log.info("tempBeforePropertyTax OF ACC - TENANTED ======>  {} ",tempBeforePropertyTax);
	                netArv = tempBeforePropertyTax;
	                updatedDetailsMasterService(updatedDetailsOfYearWise, "ACC", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
	                netArv=BigDecimal.ZERO;
	            }
	            
/**
 * ----------------------------------------------------------------------------------------------------------------------------
 * *******************************CALCULATION PART FOR OTHERS***************************************************
 * ----------------------------------------------------------------------------------------------------------------------------
 */
	            if(detail.get("ConstType").equals("OTHERS")&&detail.get("OccupancyType").equals("SELF")){
	            	log.info("INSIDE IF CONDITION OF OTHERS - SELF ======> 209 {} ");
	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
	                log.info("tempBeforePropertyTax OF OTHERS - SELF ======>  {} ",tempBeforePropertyTax);
	                rebateArv = Calculations.rebateOnResidentialProp(tempBeforePropertyTax, "SELF");
					netArv = rebateArv;
					updatedDetailsMasterService(updatedDetailsOfYearWise, "OTHERS", "SELF",oldYear, netArv,tempBeforePropertyTax);
					netArv=BigDecimal.ZERO;
	            }
	            
	            if(detail.get("ConstType").equals("OTHERS")&&detail.get("OccupancyType").equals("TENANTED")){
	            	log.info("INSIDE IF CONDITION OF OTHERS - TENANTED ======> 209 {} ");
	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
	                log.info("tempBeforePropertyTax OF OTHERS - TENANTED ======>  {} ",tempBeforePropertyTax);
	                netArv = tempBeforePropertyTax;
	                updatedDetailsMasterService(updatedDetailsOfYearWise, "OTHERS", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
	                netArv=BigDecimal.ZERO;
	            }
	            
	           
/**
 * ----------------------------------------------------------------------------------------------------------------------------
 * *******************************CALCULATION PART FOR COMMERCIAL***************************************************
 * ----------------------------------------------------------------------------------------------------------------------------
 */

	            
	            if(detail.get("ConstType").equals("COMMERCIAL_RCC")  &&  (detail.get("OccupancyType").equals("TENANTED") || detail.get("OccupancyType").equals("SELF")  )){
	            	log.info("INSIDE IF CONDITION OF COMMERCIAL_RCC - TENANTED ======> 300 {} ");
	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
	                log.info("tempBeforePropertyTax OF COMMERCIAL_RCC - TENANTED ======>  {} ",tempBeforePropertyTax);
	                netArv = tempBeforePropertyTax;
	                updatedDetailsMasterService(updatedDetailsOfYearWise, "COMMERCIAL_RCC", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
	                netArv=BigDecimal.ZERO;
	            }
	            if(detail.get("ConstType").equals("COMMERCIAL_ACC")&&(detail.get("OccupancyType").equals("TENANTED") || detail.get("OccupancyType").equals("SELF"))){
	            	log.info("INSIDE IF CONDITION OF COMMERCIAL_ACC - TENANTED ======> 309 {} ");
	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
	                log.info("tempBeforePropertyTax OF COMMERCIAL_RCC - TENANTED ======>  {} ",tempBeforePropertyTax);
	                netArv = tempBeforePropertyTax;
	                updatedDetailsMasterService(updatedDetailsOfYearWise, "COMMERCIAL_ACC", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
	                netArv=BigDecimal.ZERO;
	            }
	            
	            
	            if(detail.get("ConstType").equals("COMMERCIAL_OTHERS")&&(detail.get("OccupancyType").equals("TENANTED") || detail.get("OccupancyType").equals("SELF"))){
	            	log.info("INSIDE IF CONDITION OF COMMERCIAL_RCC - TENANTED ======> 321 {} ");
	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
	                log.info("tempBeforePropertyTax OF COMMERCIAL_RCC - TENANTED ======>  {} ",tempBeforePropertyTax);
	                netArv = tempBeforePropertyTax;
	                updatedDetailsMasterService(updatedDetailsOfYearWise, "COMMERCIAL_OTHERS", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
	                netArv=BigDecimal.ZERO;
	            }
	            
/**
 * ----------------------------------------------------------------------------------------------------------------------------
 * *******************************CALCULATION PART FOR VACCANT***************************************************
 * ----------------------------------------------------------------------------------------------------------------------------
 */           
	            
	            
	            if(detail.get("ConstType").equals("VACCANT")&&(detail.get("OccupancyType").equals("TENANTED") || detail.get("OccupancyType").equals("SELF"))){
	            	log.info("INSIDE IF CONDITION OF COMMERCIAL_RCC - TENANTED ======> 321 {} ");
	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
	                log.info("tempBeforePropertyTax OF COMMERCIAL_RCC - TENANTED ======>  {} ",tempBeforePropertyTax);
	                netArv = tempBeforePropertyTax;
	                updatedDetailsMasterService(updatedDetailsOfYearWise, "VACCANT", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
	                netArv=BigDecimal.ZERO;
	            }
	            
	            
	            
	            
	            log.info("netArv OF ALL ======>  {} ",netArv);

	                   
	        }
			
			
	        // Create a map to store the sum of values year-wise
	      
	        Map<String, BigDecimal> sumByYear = new HashMap<>();
	        Map<String, BigDecimal> sumTaxWaterEducForSelf = new HashMap<>();
			
	        for (Map<String, Object> detail : updatedDetailsOfYearWise) {
	        	 String occupancyType = (String) detail.get("OccupancyType");
	        	    String year = (String) detail.get("Year");
	        	    BigDecimal taxWaterEduc = (BigDecimal) detail.get("tax_Water_Educ");
	            BigDecimal value = (BigDecimal) detail.get("Value");
	            log.info("Processing detail - Key: {}, Value: {}", year, value);

	            // Sum the values for each year
	            sumByYear.put(year, sumByYear.getOrDefault(year, BigDecimal.ZERO).add(value));
	            log.info("Current sum for key {}: {}", year, sumByYear.get(value));
	            if ("SELF".equals(occupancyType)) {
	                // Sum tax_Water_Educ year-wise for SELF
	                sumTaxWaterEducForSelf.put(year, sumTaxWaterEducForSelf.getOrDefault(year, BigDecimal.ZERO).add(taxWaterEduc));
	                
	                
	                
	            }else {
	            	sumTaxWaterEducForSelf.put(year, sumTaxWaterEducForSelf.getOrDefault(year, BigDecimal.ZERO).add(taxWaterEduc));
	            	
	            	
	            }
	            
	            log.info("sumTaxWaterEducForSelf --- {}",sumTaxWaterEducForSelf);
	            
	            
	        }
			
	        for (Map.Entry<String, BigDecimal> entry : sumByYear.entrySet()) {
	            System.out.println("Year: " + entry.getKey() + ", Sum: " + entry.getValue());
	            NewDemandRequestDTO newDemandRequest = new NewDemandRequestDTO();
	    		OwnerTaxMasterBean ownerTaxMasterBean = new OwnerTaxMasterBean();
	           
				String oldYear =  entry.getKey();
				int newYear = Integer.parseInt(oldYear);
				log.info("Years value =======  {}",newYear);
				Integer getNextYear = newYear;
				Integer fromYear = newYear - 1;
				List<FinYearBean> finAllYear = finYearDao.findAll();
				Integer from_year = newYear - 1;
				Integer nextFrom_year = newYear;
				String convertFinYear = fromYear + "-" + getNextYear;
//				entryEffect_year = from_year + "-" + nextFrom_year;
				log.info("Effect Year{} ", convertFinYear);
				effect_year = finYearDao.getFinIdByFinYear(convertFinYear);
				entry_effect_fy_id = finAllYear.get(finAllYear.size() - 1).getId();
				entry_effect_year = finAllYear.get(finAllYear.size() - 1).getFy_name();
				log.info("DEMAND FOR YEAR -------- {}-- {}",entry_effect_fy_id,entry_effect_year);
	            totalArvCalculation = entry.getValue();
	            log.info("Key: {}, Sum of Values: {}", entry.getKey(), entry.getValue());
//	            implementation of gov_tapConn if true then 1 % of net arv else 60 rupees.
	            
	            
	            
	            
	            if (sumTaxWaterEducForSelf.containsKey(oldYear)) {
	                BigDecimal value = sumTaxWaterEducForSelf.get(oldYear);
	                log.info("sumTaxWaterEducForSelf value : {}",value);
	                totalEducationCess = value.multiply(BigDecimal.valueOf(0.01))
							.setScale(2, RoundingMode.CEILING);
	                log.info("Total Educationn Tax -------- {}",totalEducationCess);
	                String doesOwnerHaveGovTap = ownerDetailsDao.findIsOwnerHaveGov_Tap(prop_id);
		            if(doesOwnerHaveGovTap.equals("True")) {
						commomWatr_tax = value.multiply(BigDecimal.valueOf(0.01)).setScale(2,
								RoundingMode.CEILING);
		            }else {
		            	commomWatr_tax =new BigDecimal(60.00);
		            }
		            
					 log.info("Total Educationn Tax -------- {}",commomWatr_tax);
					 
	            }
	            
	            
	            
	            
	            ownerTaxMasterBean.setProp_id(prop_id); // set prop_id
				ownerTaxMasterBean.setArv(totalArvCalculation); // set Arv

//				avi ke liye sod rhe hai
				ownerTaxMasterBean.setBeforeproperty_tax(tempBeforePropertyTax); // set before propertyTax
				log.info("l-633 tempBeforePropertyTax ================>{}", tempBeforePropertyTax);
				ownerTaxMasterBean.setProperty_tax(totalArvCalculation);
				ownerTaxMasterBean.setComposite_tax(new_composite_tax); // set composite tax

				ownerTaxMasterBean.setCommon_wtr_tax(commomWatr_tax);
				ownerTaxMasterBean.setEducation_cess(totalEducationCess);

				ownerTaxMasterBean.setUser_id(floorWiseOwnerTax.get(0).getUser_id()); // default user id;
				ownerTaxMasterBean.setStatus(1); // This tax rate active until new entry are not coming against the same
													// property
				ownerTaxMasterBean.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
				ownerTaxMasterBean.setFy_id(effect_year.getId());
				ownerTaxMasterBean.setEffect_year(effect_year.getFy_name());
				ownerTaxMasterBean.setEntry_fy_id(entry_effect_fy_id);
				BigDecimal totalYearlyTax = new BigDecimal(0);

				totalYearlyTax = totalYearlyTax.add(ownerTaxMasterBean.getProperty_tax());
				log.info("totalYearlyTax ======> Line 649 {} ", totalYearlyTax);
				totalYearlyTax = totalYearlyTax.add(ownerTaxMasterBean.getComposite_tax());
				log.info("totalYearlyTax ======> Line 651 {} ", totalYearlyTax);
				totalYearlyTax = totalYearlyTax.add(ownerTaxMasterBean.getCommon_wtr_tax());
				log.info("totalYearlyTax ======> Line 652 {} ", totalYearlyTax);
				totalYearlyTax = totalYearlyTax.add(ownerTaxMasterBean.getEducation_cess());
				log.info("totalYearlyTax ======> Line 653 {} ", totalYearlyTax);
				ownerTaxMasterBean.setTot_yearly_tax(totalYearlyTax.setScale(0, RoundingMode.HALF_UP));
				log.info("Print Owner Tax Master Bean ======> Line 6457 {} ", ownerTaxMasterBean);

				newDemandRequest.setOwnerTaxMasterRequest(ownerTaxMasterBean);
				newDemandList.add(newDemandRequest);
				
				
			
					OwnerTaxMasterBean returnResult = ownerTaxMasterDao.save(ownerTaxMasterBean);
					if(returnResult !=null) {
						log.info("Demand ======= {}", returnResult);
						ownerTaxMasterBean=null;
						returnResult=null;
					}
			

			log.info("Demand ======= {}", newDemandList);
   
	        }
	        log.info("GOING TO CRETAING DEMAND ======= {}", newDemandList);
	        
	    	demandService.createDemandDuringNewAssessment(newDemandList);

				// New Implemation for Cal ARV

				




				



				
/**
 * ----------------------------------------------------------------------------------------------------------------------------
 ******************************** CALCULATION PART FOR RESIDENDIATAL AND COMMERCIAL**********************************************
 * ------------------------------------------------------------------------------------------------------------------------------
 */
				






		} catch (BadRequestException e) {
			throw new BadRequestException(e.getMessage());
		} catch (Exception e) {
			throw new BadRequestException(e.getMessage());
		}
		return "Success";
	}

//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------
//	----------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * ----------------------------------------------------------------------------------------------------------------------------
	 * *******************************UPDATE CALCULATION PART FOR
	 * ***************************************************
	 * ----------------------------------------------------------------------------------------------------------------------------
	 */

	@Override
	public List<OwnerTaxMasterBean> getPropertyOwnerTaxByPropId(Long propId) {
		return ownerTaxMasterDao.getPropertyOwnerTaxByPropId(propId);
	}

	@Override
	//@Transactional(rollbackOn = BadRequestException.class)
	public String updateOwnerTax(List<FloorWiseOwnerTaxDTO> floorWiseOwnerTax, Map< String, Integer>calculatedAllARV, Map<String, String> uses_type, String occup_type ) throws BadRequestException {
		if (!floorWiseOwnerTax.isEmpty()) {
			log.info("SAFARVDetails TABLE JSON during Re-assessment{}", floorWiseOwnerTax);
			log.info("SAFARVDetails TABLE Response Size {}", floorWiseOwnerTax.size());
			
			BigDecimal totalCompositeTax = new BigDecimal(0);
			BigDecimal totalEducationCess = new BigDecimal(0);
			BigDecimal new_composite_tax = new BigDecimal(0.00);
			BigDecimal arvCalcInsideLoop = new BigDecimal(0);
			BigDecimal commomWatr_tax = new BigDecimal(0.00);
			BigDecimal storeCommonWatr_tax = new BigDecimal(0.00);
			BigDecimal totalBeforePropertyTax = new BigDecimal(0);
			BigDecimal totalPropertyTax = new BigDecimal(0);
			BigDecimal arv = new BigDecimal(0.00);
			String entryEffect_year = null;
			Long entry_effect_fy_id = 0L;
			String entry_effect_year = null;
			Long prop_id = floorWiseOwnerTax.get(0).getProp_id();
			// Update the Status
			Integer checkPaidDemand = demandDao.checkPaidDemandByPropertyId(prop_id);
			if (checkPaidDemand > 0) {
				Optional<List<OwnerTaxMasterBean>> updateStatusTax = ownerTaxMasterDao
						.getTaxByPropertyIdAndStatus(prop_id);
				List<OwnerTaxMasterBean> updateTaxList = updateStatusTax.isPresent() ? updateStatusTax.get() : null;
				if (updateTaxList.size() > 0) {
					for (int x = 0; x < updateTaxList.size(); x++) {
						updateTaxList.get(x).setStatus(0);
						ownerTaxMasterDao.save(updateTaxList.get(x));
					}
				}
			}
			
			List<Map<String, Object>> detailsOfYearWise = new ArrayList<>();
			List<Map<String, Object>> updatedDetailsOfYearWise = new ArrayList<>();
			
			BigDecimal totalArvCalculation=new BigDecimal(0.00);
			BigDecimal rebateArv = new BigDecimal(0.00);
			BigDecimal arvRangeResult = new BigDecimal(0.00);
			BigDecimal netArv = new BigDecimal(0.00);

			Map<String, List<Optional<BigDecimal>>> combineYearWiseARVMap = new HashMap<>();
			List<String> listOfYear = new ArrayList<>();
			for (FloorWiseOwnerTaxDTO floor : floorWiseOwnerTax) {
				for (int x = 0; x < floor.getFloorWiseOwnerTax().size(); x++) {
					listOfYear.add(floor.getFloorWiseOwnerTax().get(x).getYear());
					addDetailsMasterService(detailsOfYearWise, floor.getConstruction_type(), floor.getOccupancy_type(),floor.getFloorWiseOwnerTax().get(x).getYear(), floor.getFloorWiseOwnerTax().get(x).getCalc_arv());
				}
			}

			Set<Integer> pickUniqueYear = listOfYear.stream().map(v -> Integer.parseInt(v)).collect(Collectors.toSet());

			for (Integer uniqueYear : pickUniqueYear) {
				combineYearWiseARVMap.put(uniqueYear.toString(), floorWiseOwnerTax.stream()
						.map(v -> v.getFloorWiseOwnerTax().stream()
								.filter(t -> Integer.parseInt(t.getYear()) == uniqueYear).map(z -> z.getCalc_arv())
								
								.reduce(BigDecimal::add))
						
						.collect(Collectors.toList()));
			}

			Map<String, Optional<BigDecimal>> combineYearWiseNetARV = new HashMap<>();
			for (Map.Entry<String, List<Optional<BigDecimal>>> entry : combineYearWiseARVMap.entrySet()) {
				combineYearWiseNetARV.put(entry.getKey(),
						entry.getValue().stream().filter(v -> v.isPresent()).map(x -> x.get()).reduce(BigDecimal::add));
			}

			log.info("All Records {}", combineYearWiseARVMap);
			log.info("Final count", combineYearWiseNetARV);
			List<OwnerTaxMasterBean> ownerTaxList = new ArrayList<>();
			try {
				List<NewDemandRequestDTO> newDemandList = new ArrayList<>();
				BigDecimal tempBeforePropertyTax = new BigDecimal(0.00);
				BigDecimal tempPropertyTax = new BigDecimal(0.00);
				FinYearBean effect_year= new FinYearBean();

				
		        for (Map<String, Object> detail : detailsOfYearWise) {

					
					prop_id = floorWiseOwnerTax.get(0).getProp_id();
					Object years = detail.get("Year");
					String oldYear =  years.toString();
					int newYear = Integer.parseInt(oldYear);
					log.info("Years value =======  {}",newYear);
					Integer getNextYear = newYear;
					Integer fromYear = newYear - 1;
					List<FinYearBean> finAllYear = finYearDao.findAll();
					Integer from_year = newYear - 1;
					Integer nextFrom_year = newYear;
					String convertFinYear = fromYear + "-" + getNextYear;
//					entryEffect_year = from_year + "-" + nextFrom_year;
					log.info("Effect Year{} ", convertFinYear);
					effect_year = finYearDao.getFinIdByFinYear(convertFinYear);
					entry_effect_fy_id = finAllYear.get(finAllYear.size() - 1).getId();
					entry_effect_year = finAllYear.get(finAllYear.size() - 1).getFy_name();
					
//					String calc_arv = String.valueOf(arvDetails.getValue().get());
//					totalArvCalculation = arvDetails.getValue().get();
					String doe = null;
					
					// newDoe Implemetation
					if (from_year <= 1997) {
					doe = "1997-04-01";
					
					} else if (from_year >= 1998 && from_year < 2000) {
					doe = "1997-04-01";
					} else if (from_year >= 2000 && from_year <= 2001) {
					doe = "2001-04-01";
					} else if (from_year >= 2002 && from_year <= 2004) {
					doe = "2002-04-01";
					} else if (from_year >= 2005 && from_year <= 2010) {
					doe = "2005-04-01";
					} else if (from_year >= 2011) {
					doe = "2018-04-01";
					}
					
					ArvRangeBean arvRange = null;
					String tax = "";
					
					
					
					Optional<BigDecimal> valueOptional = combineYearWiseNetARV.get(oldYear);
					
					BigDecimal value = valueOptional.get();
					
					BigDecimal percent = arvService.findPercentageByNumberAndDate(value.intValue(), doe);
					
					percent = percent != null ? percent : BigDecimal.ZERO;
					log.info("Percent of tax ===== {}", percent);
					
					if(value.intValue()<6000) {
						// 12/02 my chages
						tempBeforePropertyTax = new BigDecimal(0.00);
						BigDecimal findEduandCommonWtrTax = new BigDecimal(value.intValue());
						tax = compositeTaxService.findCalculationRateByTypeAndYear(doe, "L");
						


					}else {
						tax = compositeTaxService.findCalculationRateByTypeAndYear(doe, "M");
					}
				    // Use 'value' as needed
				    System.out.println("Value for year " + newYear + ": " + value);
						
				    new_composite_tax = new BigDecimal(tax);
					
					
				    /**
				     * ----------------------------------------------------------------------------------------------------------------------------
				     * *******************************CALCULATION PART FOR RCC***************************************************
				     * ----------------------------------------------------------------------------------------------------------------------------
				     */
				    	            if(detail.get("ConstType").equals("RCC")&&detail.get("OccupancyType").equals("SELF")){
				    	            	log.info("INSIDE IF CONDITION OF RCC - SELF ======> 193 {} ");
				    	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
				    	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
				    	                log.info("tempBeforePropertyTax OF RCC - SELF ======>  {} ",tempBeforePropertyTax);
				    	                rebateArv = Calculations.rebateOnResidentialProp(tempBeforePropertyTax, "SELF");
				    					netArv = rebateArv;
				    					log.info("Updated OF Net ARV -  ======>  {} ",netArv);
				    					updatedDetailsMasterService(updatedDetailsOfYearWise, "RCC", "SELF",oldYear, netArv,tempBeforePropertyTax);
				    					netArv=BigDecimal.ZERO;
				    						
				    			            
				    	            }
				    	            
				    	            if(detail.get("ConstType").equals("RCC")&&detail.get("OccupancyType").equals("TENANTED")){
				    	            	log.info("INSIDE IF CONDITION OF RCC - TENANTED ======> 201 {} ");
				    	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
				    	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
				    	                log.info("tempBeforePropertyTax OF RCC - TENANTED ======>  {} ",tempBeforePropertyTax);
				    	                netArv = tempBeforePropertyTax;
				    	                log.info("Updated OF Net ARV -  ======>  {} ",netArv);
				    					updatedDetailsMasterService(updatedDetailsOfYearWise, "RCC", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
				    					netArv=BigDecimal.ZERO;
				    	            }
				    /**
				     * ----------------------------------------------------------------------------------------------------------------------------
				     * *******************************CALCULATION PART FOR RCC***************************************************
				     * ----------------------------------------------------------------------------------------------------------------------------
				     */  
				    	            
				    	            
				    	            
				    	            if(detail.get("ConstType").equals("ACC")&&detail.get("OccupancyType").equals("SELF")){
				    	            	log.info("INSIDE IF CONDITION OF ACC - SELF ======> 209 {} ");
				    	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
				    	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
				    	                log.info("tempBeforePropertyTax OF ACC - SELF ======>  {} ",tempBeforePropertyTax);
				    	                rebateArv = Calculations.rebateOnResidentialProp(tempBeforePropertyTax, "SELF");
				    					netArv = rebateArv;
				    					log.info("Updated OF Net ARV -  ======>  {} ",netArv);
				    					updatedDetailsMasterService(updatedDetailsOfYearWise, "ACC", "SELF",oldYear, netArv,tempBeforePropertyTax);
				    					netArv=BigDecimal.ZERO;
				    	            }
				    	            
				    	            if(detail.get("ConstType").equals("ACC")&&detail.get("OccupancyType").equals("TENANTED")){
				    	            	log.info("INSIDE IF CONDITION OF ACC - TENANTED ======> 209 {} ");
				    	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
				    	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
				    	                log.info("tempBeforePropertyTax OF ACC - TENANTED ======>  {} ",tempBeforePropertyTax);
				    	                netArv = tempBeforePropertyTax;
				    	                updatedDetailsMasterService(updatedDetailsOfYearWise, "ACC", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
				    	                netArv=BigDecimal.ZERO;
				    	            }
				    	            
				    /**
				     * ----------------------------------------------------------------------------------------------------------------------------
				     * *******************************CALCULATION PART FOR OTHERS***************************************************
				     * ----------------------------------------------------------------------------------------------------------------------------
				     */
				    	            if(detail.get("ConstType").equals("OTHERS")&&detail.get("OccupancyType").equals("SELF")){
				    	            	log.info("INSIDE IF CONDITION OF OTHERS - SELF ======> 209 {} ");
				    	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
				    	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
				    	                log.info("tempBeforePropertyTax OF OTHERS - SELF ======>  {} ",tempBeforePropertyTax);
				    	                rebateArv = Calculations.rebateOnResidentialProp(tempBeforePropertyTax, "SELF");
				    					netArv = rebateArv;
				    					updatedDetailsMasterService(updatedDetailsOfYearWise, "OTHERS", "SELF",oldYear, netArv,tempBeforePropertyTax);
				    					netArv=BigDecimal.ZERO;
				    	            }
				    	            
				    	            if(detail.get("ConstType").equals("OTHERS")&&detail.get("OccupancyType").equals("TENANTED")){
				    	            	log.info("INSIDE IF CONDITION OF OTHERS - TENANTED ======> 209 {} ");
				    	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
				    	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
				    	                log.info("tempBeforePropertyTax OF OTHERS - TENANTED ======>  {} ",tempBeforePropertyTax);
				    	                netArv = tempBeforePropertyTax;
				    	                updatedDetailsMasterService(updatedDetailsOfYearWise, "OTHERS", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
				    	                netArv=BigDecimal.ZERO;
				    	            }
				    	            
				    	           
				    /**
				     * ----------------------------------------------------------------------------------------------------------------------------
				     * *******************************CALCULATION PART FOR COMMERCIAL***************************************************
				     * ----------------------------------------------------------------------------------------------------------------------------
				     */

				    	            
				    	            if(detail.get("ConstType").equals("COMMERCIAL_RCC")  &&  (detail.get("OccupancyType").equals("TENANTED") || detail.get("OccupancyType").equals("SELF")  )){
				    	            	log.info("INSIDE IF CONDITION OF COMMERCIAL_RCC - TENANTED ======> 300 {} ");
				    	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
				    	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
				    	                log.info("tempBeforePropertyTax OF COMMERCIAL_RCC - TENANTED ======>  {} ",tempBeforePropertyTax);
				    	                netArv = tempBeforePropertyTax;
				    	                updatedDetailsMasterService(updatedDetailsOfYearWise, "COMMERCIAL_RCC", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
				    	                netArv=BigDecimal.ZERO;
				    	            }
				    	            if(detail.get("ConstType").equals("COMMERCIAL_ACC")&&(detail.get("OccupancyType").equals("TENANTED") || detail.get("OccupancyType").equals("SELF"))){
				    	            	log.info("INSIDE IF CONDITION OF COMMERCIAL_ACC - TENANTED ======> 309 {} ");
				    	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
				    	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
				    	                log.info("tempBeforePropertyTax OF COMMERCIAL_RCC - TENANTED ======>  {} ",tempBeforePropertyTax);
				    	                netArv = tempBeforePropertyTax;
				    	                updatedDetailsMasterService(updatedDetailsOfYearWise, "COMMERCIAL_ACC", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
				    	                netArv=BigDecimal.ZERO;
				    	            }
				    	            
				    	            
				    	            if(detail.get("ConstType").equals("COMMERCIAL_OTHERS")&&(detail.get("OccupancyType").equals("TENANTED") || detail.get("OccupancyType").equals("SELF"))){
				    	            	log.info("INSIDE IF CONDITION OF COMMERCIAL_RCC - TENANTED ======> 321 {} ");
				    	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
				    	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
				    	                log.info("tempBeforePropertyTax OF COMMERCIAL_RCC - TENANTED ======>  {} ",tempBeforePropertyTax);
				    	                netArv = tempBeforePropertyTax;
				    	                updatedDetailsMasterService(updatedDetailsOfYearWise, "COMMERCIAL_OTHERS", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
				    	                netArv=BigDecimal.ZERO;
				    	            }
				    	            
				    /**
				     * ----------------------------------------------------------------------------------------------------------------------------
				     * *******************************CALCULATION PART FOR VACCANT***************************************************
				     * ----------------------------------------------------------------------------------------------------------------------------
				     */           
				    	            
				    	            
				    	            if(detail.get("ConstType").equals("VACCANT")&&(detail.get("OccupancyType").equals("TENANTED") || detail.get("OccupancyType").equals("SELF"))){
				    	            	log.info("INSIDE IF CONDITION OF COMMERCIAL_RCC - TENANTED ======> 321 {} ");
				    	                System.out.println("ConstType: " + detail.get("ConstType") +", OccupancyType: " + detail.get("OccupancyType") +", Year: " + detail.get("Year") +", Value: " + detail.get("Value"));
				    	                tempBeforePropertyTax = new BigDecimal(detail.get("Value").toString()).multiply(percent.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.CEILING);
				    	                log.info("tempBeforePropertyTax OF COMMERCIAL_RCC - TENANTED ======>  {} ",tempBeforePropertyTax);
				    	                netArv = tempBeforePropertyTax;
				    	                updatedDetailsMasterService(updatedDetailsOfYearWise, "VACCANT", "TENANTED",oldYear, netArv,tempBeforePropertyTax);
				    	                netArv=BigDecimal.ZERO;
				    	            }
				    	            
				    	            
				    	            
				    	            
				    	            log.info("netArv OF ALL ======>  {} ",netArv);


		                   
		        }
		        
		        Map<String, BigDecimal> sumByYear = new HashMap<>();
				
		        Map<String, BigDecimal> sumTaxWaterEducForSelf = new HashMap<>();
				
		        for (Map<String, Object> detail : updatedDetailsOfYearWise) {
		        	 String occupancyType = (String) detail.get("OccupancyType");
		        	    String year = (String) detail.get("Year");
		        	    BigDecimal taxWaterEduc = (BigDecimal) detail.get("tax_Water_Educ");
		            BigDecimal value = (BigDecimal) detail.get("Value");
		            log.info("Processing detail - Key: {}, Value: {}", year, value);

		            // Sum the values for each year
		            sumByYear.put(year, sumByYear.getOrDefault(year, BigDecimal.ZERO).add(value));
		            log.info("Current sum for key {}: {}", year, sumByYear.get(value));
		            if ("SELF".equals(occupancyType)) {
		                // Sum tax_Water_Educ year-wise for SELF
		                sumTaxWaterEducForSelf.put(year, sumTaxWaterEducForSelf.getOrDefault(year, BigDecimal.ZERO).add(taxWaterEduc));
		                
		                
		                
		            }else {
		            	sumTaxWaterEducForSelf.put(year, sumTaxWaterEducForSelf.getOrDefault(year, BigDecimal.ZERO).add(taxWaterEduc));
		            	
		            	
		            }
		            
		            log.info("sumTaxWaterEducForSelf --- {}",sumTaxWaterEducForSelf);
		            
		            
		        }
		        for (Map.Entry<String, BigDecimal> entry : sumByYear.entrySet()) {
		            System.out.println("Year: " + entry.getKey() + ", Sum: " + entry.getValue());
		            NewDemandRequestDTO newDemandRequest = new NewDemandRequestDTO();
		    		OwnerTaxMasterBean ownerTaxMasterBean = new OwnerTaxMasterBean();
		           
					String oldYear =  entry.getKey();
					int newYear = Integer.parseInt(oldYear);
					log.info("Years value =======  {}",newYear);
					Integer getNextYear = newYear;
					Integer fromYear = newYear - 1;
					List<FinYearBean> finAllYear = finYearDao.findAll();
					Integer from_year = newYear - 1;
					Integer nextFrom_year = newYear;
					String convertFinYear = fromYear + "-" + getNextYear;
//					entryEffect_year = from_year + "-" + nextFrom_year;
					log.info("Effect Year{} ", convertFinYear);
					effect_year = finYearDao.getFinIdByFinYear(convertFinYear);
					entry_effect_fy_id = finAllYear.get(finAllYear.size() - 1).getId();
					entry_effect_year = finAllYear.get(finAllYear.size() - 1).getFy_name();
					log.info("DEMAND FOR YEAR -------- {}-- {}",entry_effect_fy_id,entry_effect_year);
		            totalArvCalculation = entry.getValue();
		            log.info("Key: {}, Sum of Values: {}", entry.getKey(), entry.getValue());
		            
		            if (sumTaxWaterEducForSelf.containsKey(oldYear)) {
		                BigDecimal value = sumTaxWaterEducForSelf.get(oldYear);
		                log.info("sumTaxWaterEducForSelf value : {}",value);
		                totalEducationCess = value.multiply(BigDecimal.valueOf(0.01))
								.setScale(2, RoundingMode.CEILING);
		                log.info("Total Educationn Tax -------- {}",totalEducationCess);
		                String doesOwnerHaveGovTap = ownerDetailsDao.findIsOwnerHaveGov_Tap(prop_id);
			            if(doesOwnerHaveGovTap.equals("True")) {
							commomWatr_tax = value.multiply(BigDecimal.valueOf(0.01)).setScale(2,
									RoundingMode.CEILING);
			            }else {
			            	commomWatr_tax =new BigDecimal(60.00);
			            }
			            
						 log.info("Total Educationn Tax -------- {}",commomWatr_tax);
						 
		            }
		            
		            ownerTaxMasterBean.setProp_id(prop_id); // set prop_id
					ownerTaxMasterBean.setArv(totalArvCalculation); // set Arv

//					avi ke liye sod rhe hai
					ownerTaxMasterBean.setBeforeproperty_tax(tempBeforePropertyTax); // set before propertyTax
					log.info("l-633 tempBeforePropertyTax ================>{}", tempBeforePropertyTax);
					ownerTaxMasterBean.setProperty_tax(totalArvCalculation);
					ownerTaxMasterBean.setComposite_tax(new_composite_tax); // set composite tax

					ownerTaxMasterBean.setCommon_wtr_tax(commomWatr_tax);
					ownerTaxMasterBean.setEducation_cess(totalEducationCess);

					ownerTaxMasterBean.setUser_id(floorWiseOwnerTax.get(0).getUser_id()); // default user id;
					ownerTaxMasterBean.setStatus(1); // This tax rate active until new entry are not coming against the same
														// property
					ownerTaxMasterBean.setStampdate(Timestamp.valueOf(LocalDateTime.now()));
					ownerTaxMasterBean.setFy_id(effect_year.getId());
					ownerTaxMasterBean.setEffect_year(effect_year.getFy_name());
					ownerTaxMasterBean.setEntry_fy_id(entry_effect_fy_id);
					BigDecimal totalYearlyTax = new BigDecimal(0);

					totalYearlyTax = totalYearlyTax.add(ownerTaxMasterBean.getProperty_tax());
					log.info("totalYearlyTax ======> Line 649 {} ", totalYearlyTax);
					totalYearlyTax = totalYearlyTax.add(ownerTaxMasterBean.getComposite_tax());
					log.info("totalYearlyTax ======> Line 651 {} ", totalYearlyTax);
					totalYearlyTax = totalYearlyTax.add(ownerTaxMasterBean.getCommon_wtr_tax());
					log.info("totalYearlyTax ======> Line 652 {} ", totalYearlyTax);
					totalYearlyTax = totalYearlyTax.add(ownerTaxMasterBean.getEducation_cess());
					log.info("totalYearlyTax ======> Line 653 {} ", totalYearlyTax);
					ownerTaxMasterBean.setTot_yearly_tax(totalYearlyTax.setScale(0, RoundingMode.HALF_UP));
					log.info("Print Owner Tax Master Bean ======> Line 6457 {} ", ownerTaxMasterBean);

					newDemandRequest.setOwnerTaxMasterRequest(ownerTaxMasterBean);
					newDemandList.add(newDemandRequest);
					
					
				
						OwnerTaxMasterBean returnResult = ownerTaxMasterDao.save(ownerTaxMasterBean);
						if(returnResult !=null) {
							log.info("Demand ======= {}", returnResult);
							ownerTaxMasterBean=null;
							returnResult=null;
						}
				

				log.info("Demand ======= {}", newDemandList);
	   
		        }

		        
				
				
				
				log.info("Demand ======= {}", newDemandList);

				demandService.createDemandDuringReAssessment(newDemandList);

			} catch (BadRequestException e) {
				throw new BadRequestException(e.getMessage());
			} catch (Exception e) {
				throw new BadRequestException(e.getMessage());
			}
		}
		return "Success";
	}
}