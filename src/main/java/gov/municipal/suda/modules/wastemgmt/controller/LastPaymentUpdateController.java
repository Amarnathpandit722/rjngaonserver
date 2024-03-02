package gov.municipal.suda.modules.wastemgmt.controller;

import gov.municipal.suda.util.common.LastPaymentUpdateMonthDropDownDto;
import gov.municipal.suda.modules.wastemgmt.dto.UsesChargeLastPaymentUpdateDto;
import gov.municipal.suda.modules.wastemgmt.dto.WasteLastPaymentUpdateDto;
import gov.municipal.suda.modules.wastemgmt.service.transaction.LastPaymentUpdateService;
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
public class LastPaymentUpdateController {
    @Autowired
    LastPaymentUpdateService lastPaymentUpdateService;
    @PostMapping({"/user/userCharge/lastPaymentUpdate"})
    public ResponseEntity<String> lastPaymentUpdate(@RequestPart("file") MultipartFile file,
                                                    @RequestParam("consumer_details_id") Long consumerMasterId, // parameter name should be consumerMasterId
                                                    @RequestParam("receipt_no") String receipt_no,
                                                    @RequestParam("receipt_date") String receipt_date,
                                                    @RequestParam("book_no") String book_no,
                                                    @RequestParam("frm_month") String frm_month,
                                                    @RequestParam("upto_month") String upto_month,
                                                    @RequestParam("tot_amount") BigDecimal tot_amount,
                                                    @RequestParam("user_id")  Long user_id,
                                                    @RequestParam("fromDate") String fromDate,
                                                    @RequestParam("upToDate") String upToDate) throws IOException, ParseException {
        lastPaymentUpdateService.LastPaymentUpdate(file,consumerMasterId,receipt_no,receipt_date,book_no,frm_month,upto_month,tot_amount,user_id,fromDate,upToDate);
        return ResponseEntity.ok("Success");
    }

    @GetMapping({"/user/userCharge/lastPaymentUpdateView"})
    public ResponseEntity<List<UsesChargeLastPaymentUpdateDto>> lastPaymentUpdateView(@RequestParam("consumerNo") String consumerNo, @RequestParam("demandUpto") String demandUpto) {
        List<UsesChargeLastPaymentUpdateDto> results=lastPaymentUpdateService.LastPaymentUpdateView(consumerNo,demandUpto);
        return ResponseEntity.ok(results);
    }

    @GetMapping({"/userCharge/lastPaymentUpdateMonthDropDown"})
    public ResponseEntity<List<LastPaymentUpdateMonthDropDownDto>> lastPaymentUpdateMonthDropDown(@RequestParam("consumerNo") String consumerNo) {
        List<LastPaymentUpdateMonthDropDownDto> results=lastPaymentUpdateService.LastPaymentUpdateMonthDropDown(consumerNo);
        return ResponseEntity.ok(results);
    }
}
