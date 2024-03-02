package gov.municipal.suda.modules.watermgmt.controller;

import gov.municipal.suda.modules.wastemgmt.dto.WaterRangeDTO;
import gov.municipal.suda.modules.watermgmt.dao.master.ExtraRoomChargeDao;
import gov.municipal.suda.modules.watermgmt.dao.master.WaterRangeMasterDao;
import gov.municipal.suda.modules.watermgmt.dao.master.WaterRateMasterDao;
import gov.municipal.suda.modules.watermgmt.dto.transaction.*;
import gov.municipal.suda.modules.watermgmt.dto.response.ConsumerEntrySuccessResponse;
import gov.municipal.suda.modules.watermgmt.dto.response.ConsumerListResponse;
import gov.municipal.suda.modules.watermgmt.dto.response.ConsumerPaymentDetailsResponse;
import gov.municipal.suda.modules.watermgmt.model.master.ExtraRoomChargeBean;
import gov.municipal.suda.modules.watermgmt.model.master.WaterRateMasterBean;
import gov.municipal.suda.modules.watermgmt.service.transaction.WaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@Slf4j
public class WaterController {
    @Autowired
    WaterService waterService;
    @Autowired
    WaterRateMasterDao rate;
    @Autowired
    WaterRangeMasterDao range;
    @Autowired
    ExtraRoomChargeDao extraCharge;


   /*@RequestMapping(value="/user/Water/AddWaterEntry",method=RequestMethod.OPTIONS)
    public ResponseEntity getCorsHandling() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Allow", "GET, POST, PUT");
        httpHeaders.add("Access-Control-Allow-Origin", "*");
        httpHeaders.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        httpHeaders.add("Access-Control-Allow-Headers", "Authorization, Content-Type");
        return new ResponseEntity(httpHeaders,HttpStatus.OK);
    }*/
    @PostMapping({"/user/Water/AddWaterEntry"})
    public ResponseEntity<ConsumerEntrySuccessResponse> addWaterEntry(@RequestBody ConsumerEntryDto consumerEntryDto) {
        ConsumerEntrySuccessResponse result=waterService.addEntry(consumerEntryDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/user/Water/GetReceipt/{consumerNo}/{modeOfPayment}"})
    public ResponseEntity<WaterPaymentReceiptDto> getReceiptNo(@PathVariable String consumerNo,
                                                               @PathVariable String modeOfPayment) {
        WaterPaymentReceiptDto result=waterService.getReceiptDuringNewConsumerEntry(consumerNo,modeOfPayment);
        return ResponseEntity.ok(result);
    }
    @GetMapping({"/user/Water/SearchConsumer"})
    public ResponseEntity<List<ConsumerListResponse>> getReceiptNo(@RequestParam(name="wardId",required=false) Long wardId,
                                                                   @RequestParam(name="propertyNo",required=false)String propertyNo,
                                                                   @RequestParam(name="consumerNo",required=false)String consumerNo,
                                                                   @RequestParam(name="mobileNo",required = false)Long mobileNo,
                                                                   @RequestParam(name="consumerName",required = false) String consumerName) {
        List<ConsumerListResponse> result=waterService.getConsumerList(wardId,propertyNo,consumerNo,mobileNo,consumerName);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/user/Water/SearchView/{consumerNo}"})
    public ResponseEntity<ConsumerUpdateDto> SearchView(@PathVariable String consumerNo) {
        ConsumerUpdateDto result=waterService.view(consumerNo);
        return ResponseEntity.ok(result);
    }
    @GetMapping({"/user/Water/ViewDemandAmount/{fromDate}/{toDate}/{arrearAmount}/{propertyType}/{noOfRooms}/{connectionType}"})
    public ResponseEntity<String> ViewDemandAmount(@PathVariable String fromDate,
                                                   @PathVariable  String toDate, @PathVariable  BigDecimal arrearAmount,
                                                   @PathVariable  String propertyType, @PathVariable  Long noOfRooms,
                                                   @PathVariable  String connectionType) {
        String result=waterService.viewDemandAmount(fromDate,toDate,arrearAmount,propertyType,noOfRooms,connectionType);
        return ResponseEntity.ok(result);
    }

    @PostMapping({"/user/Water/DemandGenerate"})
    public ResponseEntity<Void> singleDemandGenerate(@RequestBody GenerateDemandDto generateDemandDto) {
        waterService.singleDemandGenerate(generateDemandDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping({"/user/Water/getConsumerPayment/{consumerNo}"})
    public ResponseEntity<List<ConsumerPaymentDetailsResponse>> getConsumerPaymentDetails(@PathVariable String consumerNo) {
        List<ConsumerPaymentDetailsResponse> result=waterService.getConsumerPaymentDetails(consumerNo);
        return ResponseEntity.ok(result);
    }

    //WaterSinglePaymentRequestDto

    @PostMapping({"/user/Water/WaterPayment"})
    public ResponseEntity<Void> makeWaterPayment(@RequestBody WaterSinglePaymentRequestDto waterPayment) {
        waterService.waterPayment(waterPayment);
        return ResponseEntity.ok().build();
    }

    @GetMapping({"/user/Water/collectionReport/{dateFrom}/{dateUpTo}/{wardId}/{userId}/{paymentMode}"})
    public ResponseEntity<CollectionReportDto> getCollectionReport(@PathVariable String dateFrom,
                                                                         @PathVariable String dateUpTo,
                                                                         @PathVariable Optional<String> wardId,
                                                                         @PathVariable Optional<String> userId,
                                                                         @PathVariable String paymentMode) {
        String ward_id= wardId.orElse(null);
        String user_id=userId.orElse(null);
      CollectionReportDto result=waterService.collectionReport(dateFrom,dateUpTo,ward_id,user_id,paymentMode);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"/Water/AllRate"})
    public ResponseEntity<List<WaterRateMasterBean>> getAllRate() {
        return ResponseEntity.ok(rate.findAll());
    }

    @GetMapping({"/Water/AllRange"})
    public ResponseEntity<List<WaterRangeDTO>> getAllRange(){
        return ResponseEntity.ok(range.findAllRange());
    }

    @GetMapping({"/Water/ExtraCharge"})
    public ResponseEntity<List<ExtraRoomChargeBean>> getAllExtraCharge() {
        return ResponseEntity.ok(extraCharge.findAll());
    }

	/*
	 * @PostMapping({"/user/water/empDocUpload"}) public ResponseEntity<String>
	 * lastPaymentUpdate(@RequestPart("file") MultipartFile
	 * file,@RequestParam("consumerNo") String consumerNo) throws IOException {
	 * waterService.IsNagarNigamEmployeeDocumentUpload(file,consumerNo); return
	 * ResponseEntity.ok("Success"); }
	 */
    
    
    
    
    
    
    
}
