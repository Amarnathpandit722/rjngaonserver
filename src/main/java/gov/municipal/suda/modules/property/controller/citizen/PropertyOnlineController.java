package gov.municipal.suda.modules.property.controller.citizen;

import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.master.OwnerDetailsBean;
import gov.municipal.suda.modules.property.model.master.OwnerTaxMasterBean;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.SAFViewDTO;
import gov.municipal.suda.modules.property.service.master.PropertyMasterService;
import gov.municipal.suda.modules.property.service.transaction.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@Slf4j
public class PropertyOnlineController {
    @Autowired
    SAFUpdateService sAFUpdateService;
    @Autowired
    PropertyMasterService propertyMasterService;
    @Autowired
    OwnerDetailsEntryService ownerDetailsEntryService;
    @Autowired
    DemandService demandService;
    @Autowired
    OwnerTaxMasterService ownerTaxMasterService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentReceiptService paymentReceiptService;
    @GetMapping({"/onlineSearchAllProperty"})
    public ResponseEntity<List<PropertyViewDTO>> onlineSearchAllProperty(
            @RequestParam("ward_id") Long ward_id,
            @RequestParam("owner_name") String owner_name,
            @RequestParam("property_no") String property_no
    ){
        List<Object[]> results=propertyMasterService.getAllProperty(ward_id,owner_name,property_no);
        List<PropertyViewDTO> properties= new ArrayList<>();
        for (Object[] result : results) {
            PropertyViewDTO property = new PropertyViewDTO();
            property.setId((BigInteger) result[0]);
            property.setWard_name((String) result[1]);
            property.setProperty_no((String) result[2]);
            property.setApplication_no((String) result[3]);
            property.setEntry_type((String) result[4]);
            property.setOwner_name((String) result[5]);
            property.setOwner_address((String) result[6]);
            property.setEntry_fy_id((BigInteger) result[7]);
            property.setWard_id((BigInteger) result[8]);
            property.setFy_name((String) result[9]);
            properties.add(property);
        }
        return ResponseEntity.ok(properties);
    }
    @GetMapping({"/onlineGetPropertyByPropNo"})
    public ResponseEntity<Optional<SAFViewDTO>> onlineGetPropertyByPropNo(
            @RequestParam("property_no") String property_no) {
        return ResponseEntity.ok(propertyMasterService.getPropertyByPropNo(property_no));
    }
    @PutMapping({"/onlineReAssessment/{id}"})
    public ResponseEntity<String> onlineReAssessment(@Valid @RequestBody SAFEntryRequestDto safEntryRequestDto, @PathVariable Long id) throws Exception {
        String results= String.valueOf(sAFUpdateService.SAFUpdate(safEntryRequestDto,id));
        if (null==results) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in SAF Controller");
        }
        return ResponseEntity.ok(results);
    }
    @GetMapping({"/onlineGetPropertyDetailsByPropId"})
    public ResponseEntity<List<PropertySearchDTO>> onlineGetPropertyDetailsByPropId(@RequestParam("prop_id") Long prop_id ) {
        return ResponseEntity.ok(propertyMasterService.getPropertyDetailsByPropId(prop_id));
    }
    @GetMapping({"/onlineGetPropertyOwnerDetailsByPropId"})
    public ResponseEntity<List<OwnerDetailsBean>> onlineGetPropertyOwnerDetailsByPropId(@RequestParam("prop_id") Long prop_id ) {
        return ResponseEntity.ok(ownerDetailsEntryService.getPropertyOwnerDetailsByPropId(prop_id));
    }
    @GetMapping({"/onlineGetDemandDetailsByPropId"})
    public ResponseEntity<List<DemandDetailsBean>> onlineGetDemandDetailsByPropId(@RequestParam("prop_id") Long prop_id ) {
        return ResponseEntity.ok(demandService.getDemandDetailsByPropId(prop_id));
    }
    @GetMapping({"/onlineGetPropertyOwnerTaxByPropId"})
    public ResponseEntity<List<OwnerTaxMasterBean>> onlineGetPropertyOwnerTaxByPropId(@RequestParam("prop_id") Long prop_id ) {
        return ResponseEntity.ok(ownerTaxMasterService.getPropertyOwnerTaxByPropId(prop_id));
    }
    @GetMapping({"/onlineGetPaymentDetailsByPropId"})
    public ResponseEntity<List<PaymentReceiptDTO>> onlineGetPaymentDetailsByPropId(@RequestParam("prop_id") Long prop_id ) {
        return ResponseEntity.ok(paymentService.getPaymentDetailsByPropId(prop_id));
    }
    @GetMapping({"/onlineGetPaymentReceiptByPropId"})
    public ResponseEntity<List<PaymentReceiptResponse>> onlineGetPaymentReceiptByPropId(@RequestParam("property_no") String property_no,
                                                                                @RequestParam("frm_year") String frm_year,
                                                                                @RequestParam("upto_year") String upto_year,
                                                                                @RequestParam("tran_no") String tran_no,
                                                                                @RequestParam("payment_mode") String paymentMode) {
        List<PaymentReceiptResponse> paymentReceipt = new ArrayList<>();

        paymentReceipt= paymentReceiptService.getPaymentReceipt(property_no,frm_year,upto_year,tran_no,paymentMode);
        return ResponseEntity.ok(paymentReceipt);
    }
    @GetMapping({"/onlineGetDueDetailsByPropId"})
    public ResponseEntity<List<DueDTO>> onlineGetDueDetailsByPropId(@RequestParam("prop_id") Long prop_id )throws Exception {
        List<Object[]> results=demandService.getDueDetailsByPropId(prop_id);
        List<DueDTO> receiptes= new ArrayList<>();
        for (Object[] result : results) {
            DueDTO demandReceipt = new DueDTO();
            demandReceipt.setTotal_amount((BigDecimal) result[0]);
            demandReceipt.setPenalty((BigDecimal) result[1]);
            demandReceipt.setPenal_charge((BigDecimal) result[2]);
            receiptes.add(demandReceipt);
        }
        return ResponseEntity.ok(receiptes);
    }
}
