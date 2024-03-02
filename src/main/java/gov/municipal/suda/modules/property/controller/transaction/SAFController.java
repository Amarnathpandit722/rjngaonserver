package gov.municipal.suda.modules.property.controller.transaction;

import gov.municipal.suda.message.ResponseMessage;
import gov.municipal.suda.modules.property.dto.SAFEntryRequestDto;
import gov.municipal.suda.modules.property.model.master.OwnerTaxMasterBean;
import gov.municipal.suda.modules.property.model.master.PropertyMasterBean;
import gov.municipal.suda.modules.property.model.master.SAFAllDropDownBean;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;
import gov.municipal.suda.modules.property.service.master.PropertyMasterService;
import gov.municipal.suda.modules.property.service.master.SAFAllDropDownServiceImpl;
import gov.municipal.suda.modules.property.service.master.SAFServiceImpl;
import gov.municipal.suda.modules.property.service.transaction.DemandService;
import gov.municipal.suda.modules.property.service.transaction.OwnerTaxMasterService;
import gov.municipal.suda.modules.property.service.transaction.SAFEntryServiceImpl;
import gov.municipal.suda.modules.property.service.transaction.SAFUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@Slf4j
public class SAFController {

    @Autowired
    private SAFServiceImpl safService;
    @Autowired
    SAFEntryServiceImpl safEntryService;
    @Autowired
    private SAFAllDropDownServiceImpl safAllDropDownService;
    @Autowired
    SAFUpdateService sAFUpdateService;
    @Autowired
    DemandService demandService;
    @Autowired
    OwnerTaxMasterService ownerTaxMasterService;
    @Autowired
    PropertyMasterService propertyService;
    
    // New Imlplementation of Property Generation 
     @PostMapping("/user/generatePropertyNo")
   public ResponseEntity<Long> generateProperty_NO(@RequestParam("ward_id")long ward_id, @RequestParam("zone_id")long zone_id){
    	 
    	 Long generatedNo= propertyService.generateProperty_No(ward_id, zone_id);
    	 return ResponseEntity.ok().body(generatedNo);
    	 
     }
    
    
    
    
    
    

    @PostMapping({"/user/SAFEntry"})
    public ResponseEntity<String> safEntry(@Valid @RequestBody  SAFEntryRequestDto safEntryRequestDto,@RequestHeader(value = "isMobileReq", required = false) String isMobileReq) throws Exception {
        log.info("SafEntryRequestDTo {} ",safEntryRequestDto)  ;
        if(isMobileReq!=null) {
            safEntryRequestDto.setIsMobileRequest(isMobileReq);
        }
        String results= String.valueOf(safEntryService.create(safEntryRequestDto));
        if (null==results) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in SAF Controller");
        }
        return ResponseEntity.ok(results);
    }
    @GetMapping({"/user/PropertyByPage"})
    public ResponseEntity<List<PropertyMasterBean>> getAllPropertyByPage(
               @RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "3") Integer size
    )
    {
            Pageable paging = PageRequest.of(page,size);
            return ResponseEntity.ok(safService.findAllByPage(paging));
    }

    @PostMapping({"/user/PropertyTransactionMaster"})
    public ResponseEntity<PropertyMasterBean> create(@RequestBody @Valid PropertyMasterBean propertyMasterBean) {

        return ResponseEntity.ok().build();
    }
    @GetMapping({"/SAFAllDropDownList"})
    public ResponseEntity<Optional<SAFAllDropDownBean>> getAllDropDownList() {
        return ResponseEntity.ok(safAllDropDownService.findAllDropDown());
    }
    @PutMapping({"/user/SAFUpdate/{id}"})
    public ResponseEntity<String> SAFUpdate(@Valid @RequestBody  SAFEntryRequestDto safEntryRequestDto, @PathVariable Long id
            ,@RequestHeader(value = "isMobileReq", required = false) String isMobileReq) throws Exception {

        if(isMobileReq!=null) {
            safEntryRequestDto.setIsMobileRequest(isMobileReq);
        }
        String results= String.valueOf(sAFUpdateService.SAFUpdate(safEntryRequestDto,id));


        if (null==results) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in SAF Controller");
        }
        return ResponseEntity.ok(results);
    }
    @PostMapping("/user/SAFDocumentUpload")
    public ResponseEntity<ResponseMessage> SAFDocumentUpload(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("prop_id") Long prop_id ,
                                                    @RequestParam("doc_mstr_id") Long doc_mstr_id ,
                                                    @RequestParam("user_id") Long user_id,
                                                    @RequestParam("fy_id") Long fy_id) throws Exception {
            safEntryService.SAFDocumentUpload(file,prop_id,doc_mstr_id,user_id,fy_id);
            String message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.ok(new ResponseMessage(message));
    }

    @RequestMapping(value="/user/SAFDocumentUpload",method=RequestMethod.OPTIONS)
    public ResponseEntity getCorsHandling() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Allow", "GET, POST, PUT");
        httpHeaders.add("Access-Control-Allow-Origin", "*");
        httpHeaders.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        httpHeaders.add("Access-Control-Allow-Headers", "Authorization, Content-Type");
        return new ResponseEntity(httpHeaders,HttpStatus.OK);
    }

}
