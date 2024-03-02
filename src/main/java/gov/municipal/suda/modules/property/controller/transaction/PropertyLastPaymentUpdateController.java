package gov.municipal.suda.modules.property.controller.transaction;

import gov.municipal.suda.modules.property.dto.LastPaymentResponseDTO;
import gov.municipal.suda.modules.property.dto.PropertyLastPaymentDetailsViewDTO;
import gov.municipal.suda.modules.property.dto.PropertyLastPaymentUpdateRequestDTO;
import gov.municipal.suda.modules.property.service.transaction.LastPaymentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;

@RestController
@CrossOrigin
public class PropertyLastPaymentUpdateController {
    @Autowired
    LastPaymentRecordService lastPaymentRecordService;
    @GetMapping({"/user/property/lastPaymentView"})
    public ResponseEntity<LastPaymentResponseDTO> lastPaymentUpdateView(@RequestParam("propertyNo") String propertyNo ) {
        return ResponseEntity.ok(lastPaymentRecordService.lastUpdatePaymentView(propertyNo));
    }
    @GetMapping({"/user/property/lastPaymentDetails"})
    public ResponseEntity<PropertyLastPaymentDetailsViewDTO> lastPaymentDetailsView(@RequestParam("propertyNo") String propertyNo,@RequestParam("upToYear") String upToYear ) {
        return ResponseEntity.ok(lastPaymentRecordService.lastPaymentDetailsView(propertyNo,upToYear));
    }

    // Last Payment Update is Not Access by the TC by the recomendation of Suraj sir
    
    @PostMapping({"/user/property/lastPaymentUpdate"})
    public ResponseEntity<String> PropertyLastPaymentUpdate(@RequestPart("file") MultipartFile file,
                                                            @RequestParam("propertyNo") String propertyNo,
                                                            @RequestParam("receiptNo") String receiptNo,
                                                            @RequestParam("receiptDate") String receiptDate,
                                                            @RequestParam("bookNo") String bookNo,
                                                            @RequestParam("userId") Long userId,
                                                            @RequestParam("fromYear") String fromYear,
                                                            @RequestParam("upToYear") String upToYear,
                                                            @RequestParam("totalAmount") BigDecimal totalAmount,
                                                            @RequestParam("fineAmount") BigDecimal fineAmount ) throws IOException, ParseException {
        lastPaymentRecordService.PropertyLastPaymentUpdate(file,propertyNo,receiptNo,receiptDate,bookNo,userId,fromYear,upToYear,totalAmount,fineAmount);
        return ResponseEntity.ok("Success");
    }
}
