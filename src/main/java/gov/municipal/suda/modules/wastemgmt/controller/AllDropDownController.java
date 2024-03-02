package gov.municipal.suda.modules.wastemgmt.controller;

import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerCategoryDao;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerRangeDao;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerRateChartDao;
import gov.municipal.suda.modules.wastemgmt.dto.AllDropDownDto;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerCategoryMasterBean;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRangeMasterBean;

import gov.municipal.suda.modules.wastemgmt.service.master.AllDropDownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class AllDropDownController {
    @Autowired
    ConsumerRangeDao consumerRangeDao;
    @Autowired
    AllDropDownService allDropDownService;
    @GetMapping({"/UserCharge/AllDropDown"})
    public ResponseEntity<AllDropDownDto> fetchConsumerCategory(){
        return ResponseEntity.ok().body(allDropDownService.fetchAllRecords());
    }
}
