package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CollectionService {
	
	
    Map<String, BigDecimal> getCollectionReport();
    
    void createCollection(Long prop_id,String effect_year, Long user_id,Long getMaxValue)throws Exception;

    List<CounterCollectionReportDTO> getCollectionByWardTcMode(String dateFrom, String dateTo, String wardId, String user_id,String payment_mode) throws Exception,ParseException;

    CollectionViewByPayModeDTO getCollectionByPayMode(String dateFrom, String dateTo, String wardId,String userId);

    List<WardWiseDemandViewDTO> getDemandReportByWard(String date_from,String date_to,String wardId);

    List<TeamWiseCollectionDTO> getCollectionByTeamWise(String dateFrom, String dateTo, String userId);

    List<CollectionByPayModeDTO> getCollectionByPayMode1(String dateFrom, String dateTo, BigInteger wardId);

    List<CollectionBouncedChequeDDResponseDto> bounceReport(String dateFrom, String dateTo, Long ward_no, Long user_id);

    List<Object> getAllModule(String date_from, String date_to);
    
    List<DailyAssessmentReportDTO> getSummaryData(String date_from, String date_to);
}
