package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.modules.property.dao.master.*;
import gov.municipal.suda.modules.property.dao.transaction.PropertyARVDetailsDao;
import gov.municipal.suda.modules.property.dao.transaction.SAFARVDetailsDao;

import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.master.*;

import gov.municipal.suda.modules.property.model.transaction.PropertyARVDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.SAFARVDetailsBean;
import gov.municipal.suda.modules.property.service.master.ArvRangeService;
import gov.municipal.suda.util.Calculations;
import gov.municipal.suda.util.Generate;
import gov.municipal.suda.util.enumtype.FloorType;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spring.web.json.Json;

import org.hibernate.type.BigDecimalType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SAFARVDetailsServiceImpl implements SAFARVDetailsService {
	@Autowired
	private SAFARVDetailsDao sAFARVDetailsDao;
	@Autowired
	ArvRateDao arvRateDao;
	@Autowired
	FloorDao floorDao;
	@Autowired
	FinYearDao finYearDao;
	@Autowired
	EffectYearRateChangeDao effectYearRateChangeDao;
	@Autowired
	OwnerTaxMasterService ownerTaxMasterService;
	@Autowired
	ArvRangeDao arvRangeDao;
	@Autowired
	EduAndCompositRateDao eduAndCompositRateDao;

	@Autowired
	VacantLandRateDao vacantLandRateDao;
	@Autowired
	PropertyARVDetailsDao propertyARVDetailsDao;

	@Autowired
	ArvRangeService arvService;

	@Override
	// @Transactional(rollbackOn = BadRequestException.class)
	public String createSAFARV(Long prop_id, SAFEntryRequestDto safEntryRequestDto) throws BadRequestException { 
		Long usesType = null;

		Long usage_type_id=0L;;
		String uses_type = "";
		String occup_type="";
		Map<String, Integer> calculatedArvandBuiltType  = new HashMap<String, Integer>();
		Map<String, String> uses_type_map  = new HashMap<String, String>();
		
		List<BigDecimal> prop_tax = new ArrayList<BigDecimal>();
		List<BigDecimal> calculatedAllARV = new ArrayList<>();
		log.info("Floor Entry Details JSON {}", safEntryRequestDto);
		if (safEntryRequestDto != null) {
			log.info("Inside not null ");
			try {

				List<SAFARVDetailsBean> safarvDetailsBeans = new ArrayList<>();
				Integer codeCounter = 0;
				List<OwnerTaxMasterBean> ownerTaxMasterEntry = new ArrayList<>();
				List<SAFARVDetailsDTO> floor_details = new ArrayList<>();
				Long entry_fy_id = 0L;
				String entry_fy_name = null;
				OwnerTaxMasterBean ownerTaxMasterBean = new OwnerTaxMasterBean();
				List<FinYearBean> finYear = finYearDao.findAll(); // to find financial Year
				entry_fy_id = finYear.get(finYear.size() - 1).getId();
				entry_fy_name = finYear.get(finYear.size() - 1).getFy_name();
				String effect_year = null;
				List<FloorWiseOwnerTaxDTO> floorWiseOwnerTaxDetails = new ArrayList<>();

				
//				this loop is working on the size of floor 
				for (SAFARVDetailsDTO safarvDetailsDTO : safEntryRequestDto.getFloor_details()) {
					occup_type=safarvDetailsDTO.getOccupancy_type();
					uses_type = safarvDetailsDTO.getUses_type_name();
					usage_type_id = safarvDetailsDTO.getUsage_type_id();
					log.info("Total size of floor building {} ", safEntryRequestDto.getFloor_details().size());
					if (safarvDetailsDTO.getTo_date() == null) {
						safarvDetailsDTO
								.setTo_date(finYearDao.findAll().get(finYearDao.findAll().size() - 1).getFy_name());
					}
					String finalComputeYear = "01/04/" + safarvDetailsDTO.getFrom_date().substring(0, 4);
					String finalComputeToYear = "01/03/" + safarvDetailsDTO.getTo_date().substring(5, 9);
					log.info(" from final ComputeYear {} to Final Compute Year {}", finalComputeYear,
							finalComputeToYear);
		

					FloorWiseOwnerTaxDTO floorYearWiseOwner = new FloorWiseOwnerTaxDTO();

					ownerTaxMasterBean.setProp_id(prop_id);

					String cutToYear = null;
					String finalToDate = null;
					String toDate = null;

					String cutFromYear = safarvDetailsDTO.getFrom_date().substring(0, 4);
					if (!safarvDetailsDTO.getFrom_date().isBlank() || !safarvDetailsDTO.getFrom_date().isEmpty() || !safarvDetailsDTO.getFrom_date().equals("")) {

						log.info("Inside  SAF ARV from date not equal to cut from year  {} to cut to year {} ",
								cutFromYear, cutToYear);

						if (safarvDetailsDTO.getTo_date().equals("") /// year = null error
								|| safarvDetailsDTO.getTo_date().isBlank() || safarvDetailsDTO.getTo_date().isEmpty()) {
							log.info("Inside  SAF ARV toDate not empty");
							toDate = Generate.fiscalYear();
							cutToYear = toDate.substring(5, 9);

							finalToDate = cutToYear + "-" + "04" + "-" + "01";
							log.info("Current Fiscal Year using Generate Function {} ", toDate);
							if (toDate.length() > 8) {
								log.info("Inside SAF ARV  Fiscal Year");

							} else if (toDate.length() < 9) {
								log.info("Inside SAF ARVFiscal Year Exception ");
								throw new BadRequestException("Fiscal Year Exception");
							}
						} else if (!safarvDetailsDTO.getTo_date().equals("") || !safarvDetailsDTO.getTo_date().isBlank()
								|| !safarvDetailsDTO.getTo_date().isEmpty()) {
							toDate = safarvDetailsDTO.getTo_date(); // set ToDate from request
							cutToYear = safarvDetailsDTO.getTo_date().substring(5, 9);
							log.info("Inside SAF ARV toDate not null and toDate {}", toDate);
						}
					}
					Integer convertedToIntFromYear = Integer.parseInt(cutFromYear);
					Integer countYear = Integer.parseInt(cutToYear) - Integer.parseInt(cutFromYear);

					BigDecimal finalArvRateAfterDiscount = new BigDecimal(0);
					BigDecimal totalBuiltUpArea = new BigDecimal(safarvDetailsDTO.getBuilt_up_area());
					BigDecimal grossArv = new BigDecimal(0);
					BigDecimal setArvRate = new BigDecimal(0);
					Long setArvRateId = 0L;
					SAFARVDetailsBean safarvDetailsBean = new SAFARVDetailsBean();
					
					codeCounter = codeCounter + 1;
					log.info("Code is {}", codeCounter);
					log.info("Inside 1st Loop");
					BigDecimal finalArvRateInLoop = new BigDecimal(0.00);
					
					BigDecimal discount_per = floorDao.findById(safarvDetailsDTO.getFloor_id()).get().getDiscount_per(); // get discount percentage
					floorYearWiseOwner.setUser_id(safEntryRequestDto.getUser_id());
					if (countYear > 0) { // check if property ARV calculation due from than 1 year
						String doe = null;
						List<OwnerTaxDTO> yearWiseFinalCalcArv = new ArrayList<>();
						for (int x = 0; x < countYear; x++) {
							PropertyARVDetailsBean propertyARVDetailsBean = new PropertyARVDetailsBean();


/**-------------------------------------------------------------------------------------------------------------------------------------------
* *******************************SETTING YEAR FOR  DOE FOR GETTINT ARV RATE AND DISCOUNT % ***************************************************
*-------------------------------------------------------------------------------------------------------------------------------------------------
*/
							if (convertedToIntFromYear <= 1998) {
								doe = "1997-04-01";

							} else if (convertedToIntFromYear >= 1998 && convertedToIntFromYear < 1999) {
								doe = "1997-04-01";
							} else if (convertedToIntFromYear >= 2000 && convertedToIntFromYear < 2001) {
								doe = "2000-04-01";
							} else if (convertedToIntFromYear >= 2001 && convertedToIntFromYear < 2002) {
								doe = "2001-04-01";
							} else if (convertedToIntFromYear >= 2002 && convertedToIntFromYear < 2005) {
								doe = "2002-04-01";
							} else if (convertedToIntFromYear >= 2005 && convertedToIntFromYear < 2006) {
								doe = "2005-04-01";
							} else if (convertedToIntFromYear >= 2006 && convertedToIntFromYear < 2016) {
								doe = "2006-04-01";
							}  else if (convertedToIntFromYear >= 2016 && convertedToIntFromYear < 2018) {
								doe = "2016-04-01";
							} else if (convertedToIntFromYear >= 2018 && convertedToIntFromYear <= 2024) {
								doe = "2018-04-01";
							}

							floorYearWiseOwner.setUses_type_id(safarvDetailsDTO.getUsage_type_id());
							usesType = safarvDetailsDTO.getUsage_type_id();
							if (safarvDetailsDTO.getUsage_type_id() == 4L) {
								usesType = 1L;
							}
// Setting road_type_id =2 legacy code
							
//							if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("OTHERS")) {
//								safEntryRequestDto.setRoad_type_id(2L);
//							}
							ArvRateBean arvRateResult = null;
							List<ArvRateBean> arvRateList = new ArrayList<>();
							
							Optional<List<VacantLandRateBean>> vacantRateResult = null;
							List<VacantLandRateBean> vacantRateList = null;

/**-------------------------------------------------------------------------------------------------------------------------------------------
* *******************************GETTING ARV RATE FOR [[ VACCANT ]] AND ALLOTING DISCOUNT OF 10%***************************************************
*-------------------------------------------------------------------------------------------------------------------------------------------------
*/
							if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("VACCANT")) {
								log.info("====== Inside VACCANT safardetailsDTO Line 328 =======  ");
								
									long road_type = safEntryRequestDto.getRoad_type_id();
								    log.info("ROAD_TYPE from SAFARV_DETAILS_SERVICE  ======> {}", road_type);
								   
//								    long zone_id = safEntryRequestDto.getZone_id();
								    long zone_id = safarvDetailsDTO.getZone_id();
								    log.info("ZONE_TYPE from SAFARV_DETAILS_SERVICE ======> {}", zone_id);
								   
								    String const_type = safarvDetailsDTO.getConstruction_type();
								    log.info("CONST_TYPE from SAFARV_DETAILS_SERVICE ======> {}", const_type);
								   
								    try {
								        // Add logging to check if the method is being invoked correctly
								    	long floor_id = safarvDetailsDTO.getFloor_id();
								    	FloorBean floor_name = floorDao.findById(floor_id);
								    	
								    	if(floor_name.getFloor_name().equals(safarvDetailsDTO.getFloor_name())) {
								    	
								    	
								    	
								    	
								    	String floor_Name_type = safarvDetailsDTO.getFloor_name();       
								    	 if(floor_Name_type.equals("FIRST")) {
										        arvRateResult = arvRateDao.findArvRateForIsComplex(zone_id, doe, const_type, road_type,FloorType.TOP_FLOOR);
										        log.info("arvRateResult [[[**TOP_FLOOR**]]]from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
									        }
								    	 if(floor_Name_type.equals("GROUND") || floor_Name_type.equals("BASEMENT") || floor_Name_type.equals("DECAR") ) {
								    		 arvRateResult = arvRateDao.findArvRateForIsComplex(zone_id, doe, const_type, road_type,FloorType.GROUND_FLOOR);
								    	     log.info("arvRateResult [[[**GROUND_FLOOR**]]]from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
								    	 }
								        log.info("arvRateResult from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
								    	}
								        
								        
								        
								        
								        
								        if (arvRateResult != null) {
								            arvRateList.add(arvRateResult);
								            log.info("arvRateList from SAFARV_DETAILS_SERVICE LINE 345======> {}", arvRateList);
								        } else {
								            log.error("arvRateResult is null. No data found for the provided parameters.");
								            // Handle the case where no data is found
								        }
								    } catch (Exception ex) {
								        log.error("Exception occurred while fetching ARV rate: {}", ex.getMessage());
								        // Handle the exception
								    }
								


							}
							
/**-------------------------------------------------------------------------------------------------------------------------------------------
* *******************************GETTING ARV RATE FOR [[ RCC]] AND ALLOTING DISCOUNT OF 10%***************************************************
*-------------------------------------------------------------------------------------------------------------------------------------------------
*/

							if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("RCC")) {
								log.info("====== Inside RCC safardetailsDTO Line 361 =======  ");
							
									 long road_type = safEntryRequestDto.getRoad_type_id();
									    log.info("ROAD_TYPE from SAFARV_DETAILS_SERVICE======> {}", road_type);
									   
//									    long zone_id = safEntryRequestDto.getZone_id();
									    long zone_id = safarvDetailsDTO.getZone_id();
									    log.info("ZONE_TYPE from SAFARV_DETAILS_SERVICE======> {}", zone_id);
									   
									    String const_type = safarvDetailsDTO.getConstruction_type();
									    log.info("CONST_TYPE from SAFARV_DETAILS_SERVICE======> {}", const_type);
									   
									    try {
									    	
									    	// 12/02 my change 
									    	long floor_id = safarvDetailsDTO.getFloor_id();
									    	FloorBean floor_name = floorDao.findById(floor_id);
									    	
									    	if(floor_name.getFloor_name().equals(safarvDetailsDTO.getFloor_name())) {
									    	
									    	
									    	
									    	
									    	String floor_Name_type = safarvDetailsDTO.getFloor_name();       
									    	 if(floor_Name_type.equals("FIRST")|| floor_Name_type.equals("SECOND") || floor_Name_type.equals("THIRD")|| floor_Name_type.equals("FOURTH") || floor_Name_type.equals("FIFTH") || floor_Name_type.equals("SIXTH") || floor_Name_type.equals("SEVENTH") || floor_Name_type.equals("EIGHTH") || floor_Name_type.equals("NINTH") || floor_Name_type.equals("TENTH")   ) {
											        arvRateResult = arvRateDao.findArvRateForIsComplex(zone_id, doe, const_type, road_type,FloorType.TOP_FLOOR);
											        log.info("arvRateResult [[[**TOP_FLOOR**]]]from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
										        }
									    	 if(floor_Name_type.equals("GROUND") || floor_Name_type.equals("BASEMENT") || floor_Name_type.equals("DECAR") ) {
									    		 arvRateResult = arvRateDao.findArvRateForIsComplex(zone_id, doe, const_type, road_type,FloorType.GROUND_FLOOR);
									    	     log.info("arvRateResult [[[**GROUND_FLOOR**]]]from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
									    	 }
									        log.info("arvRateResult from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
									    	}
									        
									        
									        
									        if (arvRateResult != null) {
									            arvRateList.add(arvRateResult);
									            log.info("arvRateList from SAFARV_DETAILS_SERVICE======> {}", arvRateList);
									        } else {
									            log.error("arvRateResult is null. No data found for the provided parameters.");
									            // Handle the case where no data is found
									        }
									    } catch (Exception ex) {
									        log.error("Exception occurred while fetching ARV rate: {}", ex.getMessage());
									        // Handle the exception
									    }


							}
							
/**-------------------------------------------------------------------------------------------------------------------------------------------
* *******************************GETTING ARV RATE FOR [[ ACC]]  AND ALLOTING DISCOUNT OF 10%***************************************************
*-------------------------------------------------------------------------------------------------------------------------------------------------
*/
							
							if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("ACC")) {
								log.info("====== Inside ACC safardetails Line 412 =======  ");
					
									 long road_type = safEntryRequestDto.getRoad_type_id();
									    log.info("ROAD_TYPE from SAFARV_DETAILS_SERVICE======> {}", road_type);
									   
//									    long zone_id = safEntryRequestDto.getZone_id();
									    long zone_id = safarvDetailsDTO.getZone_id();
									    log.info("ZONE_TYPE from SAFARV_DETAILS_SERVICE======> {}", zone_id);
									   
									    String const_type = safarvDetailsDTO.getConstruction_type();
									    log.info("CONST_TYPE from SAFARV_DETAILS_SERVICE======> {}", const_type);
									   
									    try {
									    	
									    	// 12/02 my change 
									    	long floor_id = safarvDetailsDTO.getFloor_id();
									    	FloorBean floor_name = floorDao.findById(floor_id);
									    	
									    	if(floor_name.getFloor_name().equals(safarvDetailsDTO.getFloor_name())) {
									    	
									    	
									    	
									    	String floor_Name_type = safarvDetailsDTO.getFloor_name();
									    	 if(floor_Name_type.equals("FIRST")|| floor_Name_type.equals("SECOND") || floor_Name_type.equals("THIRD")|| floor_Name_type.equals("FOURTH") || floor_Name_type.equals("FIFTH") || floor_Name_type.equals("SIXTH") || floor_Name_type.equals("SEVENTH") || floor_Name_type.equals("EIGHTH") || floor_Name_type.equals("NINTH") || floor_Name_type.equals("TENTH")   ) {
											        arvRateResult = arvRateDao.findArvRateForIsComplex(zone_id, doe, const_type, road_type,FloorType.TOP_FLOOR);
											        log.info("arvRateResult [[[**TOP_FLOOR**]]]from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
										        }
									    	 if(floor_Name_type.equals("GROUND") || floor_Name_type.equals("BASEMENT") || floor_Name_type.equals("DECAR") ) {
									    		 arvRateResult = arvRateDao.findArvRateForIsComplex(zone_id, doe, const_type, road_type,FloorType.GROUND_FLOOR);
									    	     log.info("arvRateResult [[[**GROUND_FLOOR**]]]from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
									    	 }
									        log.info("arvRateResult from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
									       
									    	}
									        if (arvRateResult != null) {
									            arvRateList.add(arvRateResult);
									            log.info("arvRateList from SAFARV_DETAILS_SERVICE======> {}", arvRateList);
									        } else {
									            log.error("arvRateResult is null. No data found for the provided parameters.");
									            // Handle the case where no data is found
									        }
									    } catch (Exception ex) {
									        log.error("Exception occurred while fetching ARV rate: {}", ex.getMessage());
									        // Handle the exception
									    }
								
							}
							
/**-------------------------------------------------------------------------------------------------------------------------------------------
* *******************************GETTING ARV RATE FOR [[ OTHERS]] AND ALLOTING DISCOUNT OF 10%***************************************************
*-------------------------------------------------------------------------------------------------------------------------------------------------
*/

							if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("OTHERS")) {
								log.info("====== Inside OTHERS safardetails Line 456 =======  ");
							
								//others is not working road_type_id sets the value 2 when
									 long road_type = safEntryRequestDto.getRoad_type_id();
									    log.info("ROAD_TYPE from SAFARV_DETAILS_SERVICE======> {}", road_type);
									   
//									    long zone_id = safEntryRequestDto.getZone_id();
									    long zone_id = safarvDetailsDTO.getZone_id();
									    log.info("ZONE_TYPE from SAFARV_DETAILS_SERVICE======> {}", zone_id);
									   
									    String const_type = safarvDetailsDTO.getConstruction_type();
									    log.info("CONST_TYPE from SAFARV_DETAILS_SERVICE======> {}", const_type);
									   
									    try {
									        // Add logging to check if the method is being invoked correctly
									       
									    	// 12/02 my change 
									    	long floor_id = safarvDetailsDTO.getFloor_id();
									    	FloorBean floor_name = floorDao.findById(floor_id);
									    	
									    	if(floor_name.getFloor_name().equals(safarvDetailsDTO.getFloor_name())) {
									    	
									    	
									    	
									    	String floor_Name_type = safarvDetailsDTO.getFloor_name();
									    	 if(floor_Name_type.equals("FIRST")|| floor_Name_type.equals("SECOND") || floor_Name_type.equals("THIRD")|| floor_Name_type.equals("FOURTH") || floor_Name_type.equals("FIFTH") || floor_Name_type.equals("SIXTH") || floor_Name_type.equals("SEVENTH") || floor_Name_type.equals("EIGHTH") || floor_Name_type.equals("NINTH") || floor_Name_type.equals("TENTH")   ) {
											        arvRateResult = arvRateDao.findArvRateForIsComplex(zone_id, doe, const_type, road_type,FloorType.TOP_FLOOR);
											        log.info("arvRateResult [[[**TOP_FLOOR**]]]from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
										        }
									    	 if(floor_Name_type.equals("GROUND") || floor_Name_type.equals("BASEMENT") || floor_Name_type.equals("DECAR") ) {
									    		 arvRateResult = arvRateDao.findArvRateForIsComplex(zone_id, doe, const_type, road_type,FloorType.GROUND_FLOOR);
									    	     log.info("arvRateResult [[[**GROUND_FLOOR**]]]from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
									    	 }
									        log.info("arvRateResult from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
									       
									    	}
									        if (arvRateResult != null) {
									            arvRateList.add(arvRateResult);
									            log.info("arvRateList from SAFARV_DETAILS_SERVICE======> {}", arvRateList);
									        } else {
									            log.error("arvRateResult is null. No data found for the provided parameters.");
									            // Handle the case where no data is found
									        }
									    } catch (Exception ex) {
									        log.error("Exception occurred while fetching ARV rate: {}", ex.getMessage());
									        // Handle the exception
									    }
								
								
							}
							
							

						
							
/**-------------------------------------------------------------------------------------------------------------------------------------------------
* *******************************GETTING ARV RATE FOR [[ COMMERCIAL]] AND ALLOTING DISCOUNT OF 10%***************************************************
*----------------------------------------------------------------------------------------------------------------------------------------------------
*/

							if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("COMMERCIAL_RCC")||
									safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("COMMERCIAL_ACC")||
									safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("COMMERCIAL_OTHERS")
									) {
								log.info("====== Inside COMMERCIAL safardetails Line 501 =======  ");
								
									 long road_type = safEntryRequestDto.getRoad_type_id();
									    log.info("ROAD_TYPE from SAFARV_DETAILS_SERVICE======> {}", road_type);
									   
//									    long zone_id = safEntryRequestDto.getZone_id();
									    long zone_id = safarvDetailsDTO.getZone_id();
									    log.info("ZONE_TYPE from SAFARV_DETAILS_SERVICE======> {}", zone_id);
									   
									    String const_type = safarvDetailsDTO.getConstruction_type();
									    log.info("CONST_TYPE from SAFARV_DETAILS_SERVICE======> {}", const_type);
									   
									    try {
									        // Add logging to check if the method is being invoked correctly
									    	long floor_id = safarvDetailsDTO.getFloor_id();
									    	FloorBean floor_name = floorDao.findById(floor_id);
									    	
									    	if(floor_name.getFloor_name().equals(safarvDetailsDTO.getFloor_name())) {
								
									    	String floor_Name_type = safarvDetailsDTO.getFloor_name();
									    	 if(floor_Name_type.equals("FIRST")|| floor_Name_type.equals("SECOND") || floor_Name_type.equals("THIRD")|| floor_Name_type.equals("FOURTH") || floor_Name_type.equals("FIFTH") || floor_Name_type.equals("SIXTH") || floor_Name_type.equals("SEVENTH") || floor_Name_type.equals("EIGHTH") || floor_Name_type.equals("NINTH") || floor_Name_type.equals("TENTH")   ) {
											        arvRateResult = arvRateDao.findArvRateForIsComplex(zone_id, doe, const_type, road_type,FloorType.TOP_FLOOR);
											        log.info("arvRateResult [[[**TOP_FLOOR**]]]from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
										        }
									    	 if(floor_Name_type.equals("GROUND") || floor_Name_type.equals("BASEMENT") || floor_Name_type.equals("DECAR") ) {
									    		 arvRateResult = arvRateDao.findArvRateForIsComplex(zone_id, doe, const_type, road_type,FloorType.GROUND_FLOOR);
									    	     log.info("arvRateResult [[[**GROUND_FLOOR**]]]from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
									    	 }
									        log.info("arvRateResult from SAFARV_DETAILS_SERVICE======> {}", arvRateResult);
									       
									    	}
									        
									        
									        
									        if (arvRateResult != null) {
									            arvRateList.add(arvRateResult);
									            log.info("arvRateList from SAFARV_DETAILS_SERVICE======> {}", arvRateList);
									        } else {
									            log.error("arvRateResult is null. No data found for the provided parameters.");
									            // Handle the case where no data is found
									        }
									    } catch (Exception ex) {
									        log.error("Exception occurred while fetching ARV rate: {}", ex.getMessage());
									        // Handle the exception
									    }
								

							}
							
							
							

							
							
							
							
							
							
							
							
							
							
							
							
/**----------------------------------------------------------------------------------------------------------------------------
* *******************************CALCULATION PART FOR [[ARV_RATES]]***************************************************
*----------------------------------------------------------------------------------------------------------------------------
*/						
							
							
							
							if (arvRateList != null || vacantRateList != null) {
								BigDecimal tempArv = new BigDecimal(0.00);
								BigDecimal grossArvInLoop = new BigDecimal(0.00);
								OwnerTaxDTO ownerTaxFinalCalc = new OwnerTaxDTO();

								// New Implmentation for the Vaccant , RCC, ACC, Commercial and Others ARV RATE

								
								
								if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("VACCANT")) {

									for (ArvRateBean rateBean : arvRateList) {
										setArvRate = rateBean.getBuilding_rate();
										setArvRateId = rateBean.getId();
										propertyARVDetailsBean.setArv_rate_mstr_id(setArvRateId); // Arv master id
//										finalArvRateInLoop = Calculations.finalArvRate(totalBuiltUpArea,rateBean.getBuilding_rate(), discount_per); // count final arvRate after
										finalArvRateInLoop = totalBuiltUpArea.multiply(rateBean.getBuilding_rate()) ;// count final arvRate after

										calculatedAllARV.add(finalArvRateInLoop);
										
										calculatedArvandBuiltType.put(safarvDetailsDTO.getConstruction_type(), finalArvRateInLoop.intValue());
										
										grossArv = grossArv.add(finalArvRateInLoop);

									}
								}
								
								
								
								
								
								
								if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("ACC")) {

									for (ArvRateBean rateBean : arvRateList) {
										setArvRate = rateBean.getBuilding_rate();
										setArvRateId = rateBean.getId();
										propertyARVDetailsBean.setArv_rate_mstr_id(setArvRateId); // Arv master id
										finalArvRateInLoop = Calculations.finalArvRate(totalBuiltUpArea,rateBean.getBuilding_rate(), discount_per); // count final arvRate after

										calculatedAllARV.add(finalArvRateInLoop);
										if(uses_type_map.containsKey(safarvDetailsDTO.getConstruction_type())) {
											
											int sumValue = calculatedAllARV.stream()
						                               .mapToInt(BigDecimal::intValue) // Map BigDecimal to int
						                               .sum(); // Compute sum
											log.info("old VAlue ---- Line 656 ----  =>  {}",sumValue);
										calculatedArvandBuiltType.put(safarvDetailsDTO.getConstruction_type(),sumValue);
										
										}
										else {
											calculatedArvandBuiltType.put(safarvDetailsDTO.getConstruction_type(), finalArvRateInLoop.intValue());

										}
										
										uses_type_map.put(safarvDetailsDTO.getConstruction_type(), safarvDetailsDTO.getUses_type_name());
										
										grossArv = grossArv.add(finalArvRateInLoop);

									}
								}

								
								if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("RCC")) {

									for (ArvRateBean rateBean : arvRateList) {
										setArvRate = rateBean.getBuilding_rate();
										setArvRateId = rateBean.getId();
										propertyARVDetailsBean.setArv_rate_mstr_id(setArvRateId); // Arv master id
										finalArvRateInLoop = Calculations.finalArvRate(totalBuiltUpArea,rateBean.getBuilding_rate(), discount_per); // count final arvRate after

										calculatedAllARV.add(finalArvRateInLoop);
										if(uses_type_map.containsKey(safarvDetailsDTO.getConstruction_type())) {
											
											int sumValue = calculatedAllARV.stream()
						                               .mapToInt(BigDecimal::intValue) // Map BigDecimal to int
						                               .sum(); // Compute sum
											log.info("old VAlue ---- Line 656 ----  =>  {}",sumValue);
										calculatedArvandBuiltType.put(safarvDetailsDTO.getConstruction_type(),sumValue);
										
										}
										else {
											calculatedArvandBuiltType.put(safarvDetailsDTO.getConstruction_type(), finalArvRateInLoop.intValue());

										}
										
										
										uses_type_map.put(safarvDetailsDTO.getConstruction_type(), safarvDetailsDTO.getUses_type_name());
										
										grossArv = grossArv.add(finalArvRateInLoop);

									}
								}
								
								
								if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("OTHERS")) {

									for (ArvRateBean rateBean : arvRateList) {
										setArvRate = rateBean.getBuilding_rate();
										setArvRateId = rateBean.getId();
										propertyARVDetailsBean.setArv_rate_mstr_id(setArvRateId); // Arv master id
										finalArvRateInLoop = Calculations.finalArvRate(totalBuiltUpArea,rateBean.getBuilding_rate(), discount_per); // count final arvRate after

										calculatedAllARV.add(finalArvRateInLoop);
										
										if(uses_type_map.containsKey(safarvDetailsDTO.getConstruction_type())) {
											
											int sumValue = calculatedAllARV.stream()
						                               .mapToInt(BigDecimal::intValue) // Map BigDecimal to int
						                               .sum(); // Compute sum
											log.info("old VAlue ---- Line 656 ----  =>  {}",sumValue);
										calculatedArvandBuiltType.put(safarvDetailsDTO.getConstruction_type(),sumValue);
										
										}
										else {
											calculatedArvandBuiltType.put(safarvDetailsDTO.getConstruction_type(), finalArvRateInLoop.intValue());

										}
										
										uses_type_map.put(safarvDetailsDTO.getConstruction_type(), safarvDetailsDTO.getUses_type_name());
										
										grossArv = grossArv.add(finalArvRateInLoop);

									}
								}
								// 
								if (safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("COMMERCIAL_RCC")||
										safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("COMMERCIAL_ACC")||
										safarvDetailsDTO.getConstruction_type().equalsIgnoreCase("COMMERCIAL_OTHERS")) {

									for (ArvRateBean rateBean : arvRateList) {
										setArvRate = rateBean.getBuilding_rate();
										setArvRateId = rateBean.getId();
										propertyARVDetailsBean.setArv_rate_mstr_id(setArvRateId); // Arv master id
										finalArvRateInLoop = Calculations.finalArvRate(totalBuiltUpArea,rateBean.getBuilding_rate(), discount_per); // count final arvRate after
										calculatedAllARV.add(finalArvRateInLoop);
										
										if(uses_type_map.containsKey(safarvDetailsDTO.getConstruction_type())) {
											
											int sumValue = calculatedAllARV.stream()
						                               .mapToInt(BigDecimal::intValue) // Map BigDecimal to int
						                               .sum(); // Compute sum
											log.info("old VAlue ---- Line 656 ----  =>  {}",sumValue);
										calculatedArvandBuiltType.put(safarvDetailsDTO.getConstruction_type(),sumValue);
										
										}
										else {
											calculatedArvandBuiltType.put(safarvDetailsDTO.getConstruction_type(), finalArvRateInLoop.intValue());

										}
										uses_type_map.put(safarvDetailsDTO.getConstruction_type(), safarvDetailsDTO.getUses_type_name());
										
										grossArv = grossArv.add(finalArvRateInLoop);

									}
								}

								Integer incremetedYear = convertedToIntFromYear + 1;
								String finalEffectYear = convertedToIntFromYear + "-" + incremetedYear;
								propertyARVDetailsBean.setProp_id(prop_id);
								propertyARVDetailsBean.setZone_id(safarvDetailsDTO.getZone_id());
								propertyARVDetailsBean.setUsage_type_id(safarvDetailsDTO.getUsage_type_id());
								propertyARVDetailsBean.setOccupancy_type(safarvDetailsDTO.getOccupancy_type());
								propertyARVDetailsBean.setFloor_name(safarvDetailsDTO.getFloor_name());
								propertyARVDetailsBean.setConstruction_type(safarvDetailsDTO.getConstruction_type());
								propertyARVDetailsBean.setFlr_dis_rate(discount_per); // floor discount

								propertyARVDetailsBean.setF_calc_rate(finalArvRateInLoop); // f_calc_rate
								propertyARVDetailsBean.setCalc_arv(finalArvRateInLoop.toString()); // set CalcArv after discount of 10%
								propertyARVDetailsBean.setBuilt_up_area(safarvDetailsDTO.getBuilt_up_area()); // set Builtup
								// construction from date, this field deleted as per approval of Mr Dilip from the  request DTO																				// Area
								propertyARVDetailsBean.setCompletion_date(finalComputeYear); 
								propertyARVDetailsBean.setComupto_date(finalComputeToYear); // construction to date
								propertyARVDetailsBean.setArv_rate(setArvRate); // Arv rate

								propertyARVDetailsBean.setArv(String.valueOf(grossArvInLoop)); // arv field calculation
								propertyARVDetailsBean.setCode("CODE" + codeCounter); // need to ask, previous value mention (CODE1, CODE2 etc)
								propertyARVDetailsBean.setTable_name("tbl_arvrate_mstr");
								propertyARVDetailsBean.setStatus(1); // set status to active
								propertyARVDetailsBean.setStampdate(Timestamp.valueOf(LocalDateTime.now())); 
								propertyARVDetailsBean.setEntry_fy_id(entry_fy_id); // Financial Year id
								propertyARVDetailsBean.setFy_year_date(safarvDetailsDTO.getFrom_date()); 
								propertyARVDetailsBean.setFy_end_date(toDate); // Fy end date
								propertyARVDetailsBean.setOld_prop_five_per_arv("N/A"); // need to ask
								propertyARVDetailsBean.setEffect_year(finalEffectYear); // set current entry financial year
								propertyARVDetailsDao.save(propertyARVDetailsBean);
								convertedToIntFromYear++;

								if (convertedToIntFromYear <= Integer.parseInt(entry_fy_name.substring(5, 9))) {
									ownerTaxFinalCalc.setCalc_arv(finalArvRateInLoop);
									ownerTaxFinalCalc.setYear(convertedToIntFromYear.toString());
									yearWiseFinalCalcArv.add(ownerTaxFinalCalc);
								}
							}
							 
							floorYearWiseOwner.setFloorWiseOwnerTax(yearWiseFinalCalcArv);
							effect_year = safarvDetailsDTO.getFrom_date();
							log.info("Year wise details {}", yearWiseFinalCalcArv);
							log.info("Built up area {} ", safarvDetailsDTO.getBuilt_up_area());

							safarvDetailsBean.setProp_id(prop_id);
							ownerTaxMasterBean.setArv(grossArv);
							ownerTaxMasterEntry.add(ownerTaxMasterBean);
							safarvDetailsBean.setZone_id(safarvDetailsDTO.getZone_id());
							safarvDetailsBean.setUsage_type_id(safarvDetailsDTO.getUsage_type_id());
							safarvDetailsBean.setOccupancy_type(safarvDetailsDTO.getOccupancy_type());
							safarvDetailsBean.setConstruction_type(safarvDetailsDTO.getConstruction_type());
							safarvDetailsBean.setFloor_name(safarvDetailsDTO.getFloor_name());
							totalBuiltUpArea = BigDecimal.valueOf(Integer.parseInt(safarvDetailsDTO.getBuilt_up_area())); // set build up
																										// area
							log.info("Total BuiltUP Area {}", totalBuiltUpArea);
							safarvDetailsBean.setFlr_dis_rate(finalArvRateAfterDiscount); // floor discount rate

					
							safarvDetailsBean.setF_calc_rate(finalArvRateInLoop); // f_calc_rate
							safarvDetailsBean.setCalc_arv(yearWiseFinalCalcArv.toString()); // set CalcArv
							ownerTaxMasterBean.setEffect_year(safarvDetailsDTO.getFrom_date()); // get from main Request DTO floor details from date FinancialYear
							safarvDetailsBean.setBuilt_up_area(safarvDetailsDTO.getBuilt_up_area()); // set Builtup Area
							safarvDetailsBean.setCompletion_date(finalComputeYear); // construction from date, this// field deleted as per approval of
							// New Implementation of zone_id
							safarvDetailsBean.setZone_id(safarvDetailsDTO.getZone_id());
							safarvDetailsBean.setComupto_date(finalComputeToYear); // construction to date
							safarvDetailsBean.setArv_rate(setArvRate); // Arv rate
							safarvDetailsBean.setArv_rate_mstr_id(setArvRateId); // Arv master id
							BigDecimal arvBeforeDiscount =setArvRate.multiply(totalBuiltUpArea);
							safarvDetailsBean.setArv(String.valueOf(arvBeforeDiscount)); // arv field calculation
							log.info("finalArvRateAfterDiscount before Passing to OwnerTax =======> {}", arvBeforeDiscount);
							safarvDetailsBean.setFlr_dis_rate(discount_per); // floor discount calculation and set into flr_dis_rate field
//
							safarvDetailsBean.setCode("CODE" + codeCounter); // need to ask, previous value mention(CODE1, CODE2 etc)
							safarvDetailsBean.setTable_name("tbl_arvrate_mstr");
							safarvDetailsBean.setStatus(1); // set status to active
							safarvDetailsBean.setStampdate(Timestamp.valueOf(LocalDateTime.now())); // current date and time
							safarvDetailsBean.setEntry_fy_id(entry_fy_id); // Financial Year id
							safarvDetailsBean.setFy_year_date(safarvDetailsDTO.getFrom_date()); // Fy start date
							safarvDetailsBean.setFy_end_date(toDate); // Fy end date
							safarvDetailsBean.setOld_prop_five_per_arv("N/A"); // need to ask
							safarvDetailsBean.setEffect_year(effect_year); // set current entry financial year

							try {

								SAFARVDetailsBean resultsOfSAFARV = sAFARVDetailsDao.save(safarvDetailsBean);
								floor_details.add(safarvDetailsDTO);
								safarvDetailsBeans.add(safarvDetailsBean);
								log.info("floor Details {}", floor_details);
								log.info("Data save into {}", resultsOfSAFARV);
								log.info("Owner Tax Master Array Data {}", ownerTaxMasterEntry);
							} catch (BadRequestException e) {
								throw new BadRequestException(e.getMessage());
							}
						}

						floorYearWiseOwner.setProp_id(prop_id);
						floorYearWiseOwner.setFrom_date(safarvDetailsDTO.getFrom_date());
						floorYearWiseOwner.setTo_date(safarvDetailsDTO.getTo_date());
						floorYearWiseOwner.setOccupancy_type(safarvDetailsDTO.getOccupancy_type());
						floorYearWiseOwner.setConstruction_type(safarvDetailsDTO.getConstruction_type());
						floorYearWiseOwner.setBuilt_up_area(safarvDetailsDTO.getBuilt_up_area());
						floorYearWiseOwner.setIs_complex(safEntryRequestDto.getIs_complex());
						floorYearWiseOwner.setIs_school(safEntryRequestDto.getIs_school());
						floorYearWiseOwner.setIs_isdp(safEntryRequestDto.getIs_isdp());
						floorYearWiseOwner.setIsMobileTower(safEntryRequestDto.getIsMobileTower());
						floorYearWiseOwner.setUser_id(safEntryRequestDto.getUser_id());
						floorWiseOwnerTaxDetails.add(floorYearWiseOwner);

					}
				}
				
						log.info("finalArvCalAfterDiscountToCal ======> {}", calculatedAllARV);
				// Initialize sum to zero

				ownerTaxMasterService.create(floorWiseOwnerTaxDetails, calculatedArvandBuiltType, uses_type_map, occup_type);

			} catch (Exception e) {
				log.info("Error message ====== {} ", e.getMessage());
				throw new BadRequestException(e.getMessage());
			}
		} else if (safEntryRequestDto == null) {
			// new Exception("Failed");
			throw new BadRequestException("Request is null");
		}

		return "Successful";

	}

}
