package gov.municipal.suda.modules.property.controller.transaction;

import gov.municipal.suda.modules.property.dao.transaction.BounceChequeDDDao;
import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.transaction.CounterCollectionBouncedChequeDDView;
import gov.municipal.suda.modules.property.service.transaction.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
public class CollectionController {

    @Autowired
    private CollectionService collectionService;
    @Autowired
    BounceChequeDDDao bounceChequeDDDao;
    @GetMapping({"/user/getAllCollection"})
    public Map<String, BigDecimal> getCollectionReport(){
        return collectionService.getCollectionReport();
    }
    @GetMapping({"/user/getCounterCollectionReport"})
    public ResponseEntity<List<CounterCollectionReportDTO>> getCollectionByWardTcMode(
            @RequestParam("date_from") String date_from,
            @RequestParam("date_to") String date_to,
            @RequestParam("ward_id") Optional<String> ward_id,
            @RequestParam("user_id") Optional<String> user_id,
            @RequestParam("payment_mode") Optional<String> payment_mode
    ) throws Exception {
        String wardId=ward_id.orElse("");
        String paymentMode=payment_mode.orElse("");
        String userId=user_id.orElse("");
        return ResponseEntity.ok(collectionService.getCollectionByWardTcMode(date_from,date_to,wardId,userId,paymentMode));
     }
    @GetMapping({"/user/getCollectionByPayMode"})
    public ResponseEntity<CollectionViewByPayModeDTO> getCollectionByPayMode(
            @RequestParam("date_from") String date_from,
            @RequestParam("date_to") String date_to,
            @RequestParam("ward_id") String ward_id,
            @RequestParam("user_id") String user_id
    ){
        return ResponseEntity.ok(collectionService.getCollectionByPayMode(date_from,date_to,ward_id,user_id));
     }
    @GetMapping({"/user/getCollectionByPayMode1"})
    public ResponseEntity<List<CollectionByPayModeDTO>> getCollectionByPayMode1(
            @RequestParam("date_from") String date_from,
            @RequestParam("date_to") String date_to,
            @RequestParam("ward_id") BigInteger ward_id
    ){
        return ResponseEntity.ok(collectionService.getCollectionByPayMode1(date_from,date_to,ward_id));
    }
    
    // Have to implement Access us to  TL ADMIN and SUPERADMIN but not By TC
    
    @GetMapping({"/user/getDemandReportByWard"})
    public ResponseEntity<List<WardWiseDemandViewDTO>> getDemandReportByWard(
            @RequestParam("date_from") String date_from,
            @RequestParam("date_to") String date_to,
            @RequestParam("ward_id") String ward_id
    ){
        return ResponseEntity.ok(collectionService.getDemandReportByWard(date_from,date_to,ward_id));
     }
    
    @GetMapping({"/user/getCollectionByTeamWise"})
    @PreAuthorize("hasAnyRole('ADMIN', 'TL', 'SUPERADMIN')")
    public ResponseEntity<List<TeamWiseCollectionDTO>> getCollectionByTeamWise(
            @RequestParam("date_from") String date_from,
            @RequestParam("date_to") String date_to,
            @RequestParam("user_id") String user_id
    ){
        return ResponseEntity.ok(collectionService.getCollectionByTeamWise(date_from,date_to,user_id));
    }

    @GetMapping({"/user/get_bounce_collection"})
    public ResponseEntity<List<CollectionBouncedChequeDDResponseDto>> getBounceCollection(
            @RequestParam("date_from") String date_from,
            @RequestParam("date_to") String date_to,
            @RequestParam("ward_id") Optional<String> ward_id,
            @RequestParam("user_id") Optional<String> user_id) {

       String wardId=ward_id.orElse("");
       String userId=user_id.orElse("");
       Long wardNo=0L;
       Long userNo=-1L;
       if(!wardId.equalsIgnoreCase("All")) {
         wardNo=Long.parseLong(wardId);
       }
       if(!userId.equalsIgnoreCase("All")) {
           userNo=Long.parseLong(userId);
       }

      return ResponseEntity.ok(collectionService.bounceReport(date_from,date_to,wardNo,userNo));
    }
    
    @GetMapping({"/user/all_module_arr_curent_total"})
    public ResponseEntity<?> getAllModule(
    		@RequestParam("date_from") String date_from,
            @RequestParam("date_to") String date_to)
    
    {
    
    	return ResponseEntity.ok(collectionService.getAllModule(date_from,date_to));
    	
    }
    @GetMapping({"/user/allSummaryData"})
    public ResponseEntity<?> getSummaryDataofUser(
    		@RequestParam("date_from") String date_from,
            @RequestParam("date_to") String date_to)
    
    {
    
    	return ResponseEntity.ok(collectionService.getSummaryData(date_from,date_to));
    	
    }
    

}
