package gov.municipal.suda.modules.property.controller.master;

import gov.municipal.suda.modules.property.dao.master.PropertyMasterDao;

import gov.municipal.suda.modules.property.dto.*;

import gov.municipal.suda.modules.property.dto.DueDTO;
import gov.municipal.suda.modules.property.dto.PaymentReceiptResponse;
import gov.municipal.suda.modules.property.dto.PropertyMasterDto;
import gov.municipal.suda.modules.property.dto.PropertyViewDTO;

import gov.municipal.suda.modules.property.model.master.*;

import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.SAFViewDTO;
import gov.municipal.suda.modules.property.service.master.PropertyMasterService;
import gov.municipal.suda.modules.property.service.transaction.*;
import gov.municipal.suda.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
public class PropertyMasterController {
    @Autowired
    private PropertyMasterService propertyMasterService;

    @Autowired
    private PropertyMasterDao propertyMasterDao;
    @Autowired
    DemandService demandService;
    @Autowired
    OwnerTaxMasterService ownerTaxMasterService;
    @Autowired
    OwnerDetailsEntryService ownerDetailsEntryService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentReceiptService paymentReceiptService;

    @PostMapping("/user/PropertyEntry")
    public ResponseEntity<Void> create(@Valid @RequestBody PropertyMasterDto dto) {
        PropertyMasterBean propertyMasterBean = propertyMasterService.propertyMasterEntry(ObjectMapperUtils.map(dto, PropertyMasterBean.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(propertyMasterBean.getProperty_no())
                .toUri();
        return ResponseEntity.created(location).build();

    }
//    @GetMapping({"/admin/generatePropertyNo/{ward_id}/{zone_id}"})
//    public String generatePropertyNo(@PathVariable("ward_id") Long ward_id,@PathVariable("zone_id") Long zone_id) throws SQLException {
//        final String propertNo= Generate.generatePropertyNo(ward_id,zone_id);
//        return propertNo;
//    }

@GetMapping({"/getDocumentNameDropdown"})
public ResponseEntity<List<PropertyDocMstrBean>> fetchAllDocumentName(){
    return ResponseEntity.ok().body(propertyMasterService.fetchAllDocumentName());
}

    @GetMapping({"/searchAllProperty"})
    public ResponseEntity<List<PropertyViewDTO>> getAllProperty(
            @RequestParam(name="ward_id",required=false) Long ward_id,
            @RequestParam(name="owner_name",required=false) String owner_name,
            @RequestParam(name="property_no",required=false) String property_no
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
    @GetMapping({"/getPropertyByPropNo"})
    public ResponseEntity<Optional<SAFViewDTO>> getPropertyByPropNo(
            @RequestParam("property_no") String property_no) {
        return ResponseEntity.ok(propertyMasterService.getPropertyByPropNo(property_no));
    }
    @GetMapping({"/user/getPropertyDetailsByPropId"})
    public ResponseEntity<List<PropertySearchDTO>> getPropertyDetailsByPropId(@RequestParam("prop_id") Long prop_id ) {
        return ResponseEntity.ok(propertyMasterService.getPropertyDetailsByPropId(prop_id));
    }
    @GetMapping({"/user/getPropertyOwnerDetailsByPropId"})
    public ResponseEntity<List<OwnerDetailsBean>> getPropertyOwnerDetailsByPropId(@RequestParam("prop_id") Long prop_id ) {
        return ResponseEntity.ok(ownerDetailsEntryService.getPropertyOwnerDetailsByPropId(prop_id));
    }
    @GetMapping({"/user/getDemandDetailsByPropId"})
    public ResponseEntity<List<DemandDetailsBean>> getDemandDetailsByPropId(@RequestParam("prop_id") Long prop_id ) {
        return ResponseEntity.ok(demandService.getDemandDetailsByPropId(prop_id));
    }
    @GetMapping({"/user/getPropertyOwnerTaxByPropId"})
    public ResponseEntity<List<OwnerTaxMasterBean>> getPropertyOwnerTaxByPropId(@RequestParam("prop_id") Long prop_id ) {
        return ResponseEntity.ok(ownerTaxMasterService.getPropertyOwnerTaxByPropId(prop_id));
    }
    @GetMapping({"/user/getPaymentDetailsByPropId"})
    public ResponseEntity<List<PaymentReceiptDTO>> getPaymentDetailsByPropId(@RequestParam("prop_id") Long prop_id ) {
        return ResponseEntity.ok(paymentService.getPaymentDetailsByPropId(prop_id));
    }
    @GetMapping({"/user/getPaymentReceiptByPropId"})
    public ResponseEntity<List<PaymentReceiptResponse>> getPaymentReceiptPropId(@RequestParam("property_no") String property_no,
                                                                                @RequestParam("frm_year") String frm_year,
                                                                                @RequestParam("upto_year") String upto_year,
                                                                                @RequestParam("tran_no") String tran_no,
                                                                                @RequestParam("payment_mode") String paymentMode
                                                                                ) {
        List<PaymentReceiptResponse> paymentReceipt = new ArrayList<>();

        paymentReceipt= paymentReceiptService.getPaymentReceipt(property_no,frm_year,upto_year,tran_no,paymentMode);
        return ResponseEntity.ok(paymentReceipt);
    }
    @GetMapping({"/user/getDueDetailsByPropId"})
    public ResponseEntity<List<DueDTO>> getDueDetailsByPropId(@RequestParam("prop_id") Long prop_id )throws Exception {
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
    @GetMapping({"/user/getPageablePropDetailsByPropId"})
    public ResponseEntity<Page<PropertySEarchViewDTO>> getPropDetailsByPropId(@RequestParam("ward_id") Long ward_id,
                                                                              @RequestParam("owner_name") String owner_name,
                                                                              @RequestParam("property_no") String property_no,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("id").descending());
        return ResponseEntity.ok(propertyMasterService.getPropDetailsByPropId(ward_id,owner_name,property_no,pageable));
    }
    @GetMapping({"/user/getPropertyOwnerDetailsByPropNo"})
    public ResponseEntity<List<OwnerDetailsBean>> getPropertyOwnerDetailsByPropNo(@RequestParam("property_no") String property_no ) {
        return ResponseEntity.ok(ownerDetailsEntryService.getPropertyOwnerDetailsByPropNo(property_no));
    }
    @PutMapping({"/admin/ownerUpdate"})
    public ResponseEntity<String> ownerUpdate(@RequestBody OwnerDetailsBean ownerDetailsBean) throws Exception {
        String results= String.valueOf(ownerDetailsEntryService.ownerUpdate(ownerDetailsBean));
        if (null==results) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in Controller");
        }
        return ResponseEntity.ok(results);
    }
    @GetMapping({"/user/getPaymentReceiptByTC"})
    public ResponseEntity<List<BulkPaymentReceipt>> getPaymentReceiptByTC(@RequestParam("user_id") String user_id,
                                                                                @RequestParam("frm_date") String frm_date,
                                                                                @RequestParam("to_date") String to_date) {
        List<BulkPaymentReceipt> paymentReceipt = new ArrayList<>();

        paymentReceipt= paymentReceiptService.getPaymentReceiptByTC(frm_date,to_date,user_id);
        return ResponseEntity.ok(paymentReceipt);
    }
}
