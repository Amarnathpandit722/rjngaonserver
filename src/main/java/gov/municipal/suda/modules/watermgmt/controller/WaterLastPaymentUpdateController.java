package gov.municipal.suda.modules.watermgmt.controller;

import gov.municipal.suda.modules.watermgmt.dto.response.WaterLastPaymentUpdateMonthDropDownDto;
import gov.municipal.suda.modules.watermgmt.dto.transaction.WaterLastPaymentViewResponseMainDto;
import gov.municipal.suda.modules.watermgmt.service.transaction.WaterLastPaymentUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class WaterLastPaymentUpdateController {
    @Autowired
    WaterLastPaymentUpdateService waterLastPaymentUpdateService;
    @PostMapping({"/user/water/lastPaymentUpdate"})
    public ResponseEntity<String> lastPaymentUpdate(@RequestPart("file") MultipartFile file,
                                                    @RequestParam("consumer_details_id") Long consumer_details_id,
                                                    @RequestParam("receipt_no") String receipt_no,
                                                    @RequestParam("receipt_date") String receipt_date,
                                                    @RequestParam("book_no") String book_no,
                                                    @RequestParam("frm_month") String frm_month,
                                                    @RequestParam("upTo_month") String upTo_month,
                                                    @RequestParam("total_amount") BigDecimal total_amount,
                                                    @RequestParam("user_id") Long user_id,
                                                    @RequestParam("fromDate") String fromDate,
                                                    @RequestParam("upToDate")  String upToDate
                                                    ) throws IOException, ParseException {
        waterLastPaymentUpdateService.WaterLastPaymentUpdate(file,consumer_details_id,receipt_no,receipt_date,book_no,frm_month,upTo_month,total_amount,user_id,fromDate,upToDate);
        return ResponseEntity.ok("Success");
    }

    @GetMapping({"/user/water/lastPaymentUpdateView"})
    public ResponseEntity<WaterLastPaymentViewResponseMainDto> lastPaymentUpdateView(@RequestParam("consumerNo") String consumerNo,
                                                                                     @RequestParam("demandFrom") String demandFrom, @RequestParam("demandUpTo") String demandUpTo){
        WaterLastPaymentViewResponseMainDto results= waterLastPaymentUpdateService.LastPaymentUpdateView(consumerNo,demandFrom, demandUpTo);
        return ResponseEntity.ok(results);
    }

    @GetMapping({"/water/lastPaymentUpdateMonthDropDown"})
    public ResponseEntity<List<WaterLastPaymentUpdateMonthDropDownDto>> lastPaymentUpdateDropDown(@RequestParam("consumerNo") String consumerNo){
        List<WaterLastPaymentUpdateMonthDropDownDto> results= waterLastPaymentUpdateService.WaterLastPaymentUpdateDropDown(consumerNo);
        return ResponseEntity.ok(results);
    }
}
