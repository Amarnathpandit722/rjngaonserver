package gov.municipal.suda.modules.wastemgmt.controller;

import gov.municipal.suda.modules.property.dto.CashPaymentRequestDto;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;
import gov.municipal.suda.modules.property.model.master.OwnerDetailsBean;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerDetailsDao;
import gov.municipal.suda.modules.wastemgmt.dto.*;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerCategoryMasterBean;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRangeMasterBean;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRateChartBean;
import gov.municipal.suda.modules.wastemgmt.model.transaction.ConsumerDemandBean;
import gov.municipal.suda.modules.wastemgmt.service.master.ConsumerMasterService;
import gov.municipal.suda.modules.wastemgmt.service.master.ConsumerRateDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class ConsumerMasterController {
    @Autowired
    private ConsumerMasterService consumerMasterService;
    @Autowired
    ConsumerDetailsDao consumerDetailsDao;

    @Autowired
    ConsumerRateDetailService rateDetailService;

    @GetMapping({"/fetchConsumerCategory"})
    public ResponseEntity<List<ConsumerCategoryMasterBean>> fetchConsumerCategory(){
        return ResponseEntity.ok().body(consumerMasterService.fetchAllConsumerCategory());
    }
    @GetMapping({"/fetchConsumerRange"})
    public ResponseEntity<List<ConsumerRangeMasterBean>> fetchConsumerRange(){
        return ResponseEntity.ok().body(consumerMasterService.fetchConsumerRange());
    }
    @GetMapping({"/fetchAllConsumer"})
    public ResponseEntity<List<ConsumerViewDTO>> fetchAllConsumer(
            @RequestParam(name="ward_id",required = false) String ward_id,
            @RequestParam(name="holding_no",required = false) String holding_no,
            @RequestParam(name="consumer_no",required = false) String consumer_no,
            @RequestParam(name="consumer_name",required = false) String consumer_name,
            @RequestParam(name="mobile_no",required = false) Long mobile_no
    ){
        return ResponseEntity.ok().body(consumerMasterService.fetchAllConsumer(ward_id,holding_no,consumer_no,consumer_name,mobile_no));
    }
    @GetMapping({"/fetchMonthlyAmount"})
    public ResponseEntity<Optional<ConsumerRateChartBean>> fetchMonthlyAmount(
            @RequestParam("consumer_range_mstr_id") String consumer_range_mstr_id
//            @RequestParam("effect_year") String effect_year,
//            @RequestParam("effect_month") String effect_month
    ){
        return ResponseEntity.ok().body(consumerMasterService.fetchMonthlyAmount(consumer_range_mstr_id));
    }

    @PostMapping({"/createConsumer"})
    public ResponseEntity<String> createConsumer(@Valid @RequestBody ConsumerEntryDTO consumerEntryDTO) throws SQLException {
        String results= String.valueOf(consumerMasterService.createConsumer(consumerEntryDTO));
        if (null==results) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in Consumer Controller");
        }
        return ResponseEntity.ok(results);
    }
    @GetMapping({"/fetchConsumerDetailByConsumerNo"})
    public ResponseEntity<List<ConsumerDetailsViewDTO>> fetchConsumerByConsumerNo(
            @RequestParam("consumer_no") String consumer_no
    ){
        return ResponseEntity.ok().body(consumerMasterService.fetchConsumerByConsumerNo(consumer_no));
    }
    @GetMapping({"/fetchAreaDetailByConsumerNo"})
    public ResponseEntity<List<AreaDetailsViewDTO>> fetchAreaDetailByConsumerNo(
            @RequestParam("consumer_no") String consumer_no
    ){
        return ResponseEntity.ok().body(consumerMasterService.fetchAreaDetailByConsumerNo(consumer_no));
    }
    @GetMapping({"/fetchMonthlyRateByConsumerNo"})
    public ResponseEntity<List<MonthlyRateViewDTO>> fetchMonthlyRateByConsumerNo(
            @RequestParam("consumer_no") String consumer_no
    ){
        return ResponseEntity.ok().body(consumerMasterService.fetchMonthlyRateByConsumerNo(consumer_no));
    }
    @GetMapping({"/fetchDueDetailsByConsumerNo"})
    public ResponseEntity<List<ConsumerDueDTO>> fetchDeuDetailsByConsumerNo(
            @RequestParam("consumer_no") String consumer_no
    ){
        return ResponseEntity.ok().body(consumerMasterService.fetchDeuDetailsByConsumerNo(consumer_no));
     }
    @GetMapping({"/getPaymentReceiptByTrnNo"})
    public ResponseEntity<List<PaymentReceiptViewDTO>> getPaymentReceiptByTrnNo(
            @RequestParam("transaction_no") String transaction_no
    ){
        return ResponseEntity.ok().body(consumerMasterService.getPaymentReceiptByTrnNo(transaction_no));
    }
    @GetMapping({"/getDemandByConsumerNo"})
    public ResponseEntity<List<ConsumerDemandViewDTO>> getDemandByConsumerNo(
            @RequestParam("consumer_no") String consumer_no
    ){
        return ResponseEntity.ok().body(consumerMasterService.getDemandByConsumerNo(consumer_no));
    }
    @GetMapping({"/fetchPaymentDetailsByConsumerNo"})
    public ResponseEntity<List<PaymentViewDTO>> fetchPaymentDetailsByConsumerNo(
            @RequestParam("consumer_no") String consumer_no
    ){
        return ResponseEntity.ok().body(consumerMasterService.fetchPaymentDetailsByConsumerNo(consumer_no));
    }
    @PostMapping({"/consumerPayment"})
    public ResponseEntity<String> consumerPayment(@RequestBody ConsumerPaymentReqDTO consumerPaymentReqDTO) throws Exception {
        String returnResponse=consumerMasterService.consumerPayment(consumerPaymentReqDTO);
        return ResponseEntity.ok(returnResponse);
    }
    @RequestMapping(value="/userChargeCounterReport",method=RequestMethod.OPTIONS)
    public ResponseEntity getCorsHandling() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Allow", "GET, POST, PUT");
        httpHeaders.add("Access-Control-Allow-Origin", "*");
        httpHeaders.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        httpHeaders.add("Access-Control-Allow-Headers", "Authorization, Content-Type");
        return new ResponseEntity(httpHeaders,HttpStatus.OK);
    }
    @GetMapping({"/userChargeCounterReport"})
    public ResponseEntity<List<WasteCounterReportDTO>> userChargeCounterReport(
            @RequestParam("date_from") String date_from,
            @RequestParam("date_to") String date_to,
            @RequestParam("ward_id") String ward_id,
            @RequestParam("user_id") String user_id,
            @RequestParam("payment_mode") String payment_mode

    ){
        return ResponseEntity.ok().body(consumerMasterService.userChargeCounterReport(date_from,date_to,ward_id,user_id,payment_mode));
    }
    @GetMapping({"usesCharge/fetchConsumerByConsumerNo"})
    public ResponseEntity<List<ConsumerDetailsViewDTO>> fetchByConsumerNo(
            @RequestParam("consumer_no") String consumer_no
    ){
        Long id=consumerDetailsDao.findConsumMstrIdByNo(consumer_no);
        return ResponseEntity.ok().body(consumerMasterService.fetchConsumerByConsumerNo(consumer_no));
    }
    @PutMapping({"/consumerDetailsUpdate"})
    public ResponseEntity<String> consumerDetailsUpdate(@RequestBody ConsumerDetUpdateDTO consumerDetUpdateDTO) throws Exception {
        String results= String.valueOf(consumerMasterService.consumerDetailsUpdate(consumerDetUpdateDTO));
        if (null==results) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in Controller");
        }
        return ResponseEntity.ok(results);
    }
    @PatchMapping({"/userCharges/consumerRateDetailsUpdateById"})
    public ResponseEntity<?> consumerRateDetailsUpdate(
            @RequestParam("rateDetailsId") Long rateDetailsId,
            @RequestParam("consumerNo") String consumerNo,
            @RequestParam("chartId") Long previousChartId,
            @RequestBody AreaDetailsDto areaDetailsDto) throws Exception {
        rateDetailService.consumerRateDetailsUpdate(consumerNo,rateDetailsId,previousChartId,areaDetailsDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping({"/userCharges/CategoryEntry"})
    public ResponseEntity<?> consumerCategoryEntryInRateDetailsByConsumerDetailsId(@RequestBody ConsumerRateDetailsDto consumerRateDetailsDto)  {
        rateDetailService.consumerCategoryEntryInRateDetailsByConsumerDetailsId(consumerRateDetailsDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
