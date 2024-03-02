package gov.municipal.suda.modules.property.controller.master;

import gov.municipal.suda.modules.property.dao.master.BankNameDao;
import gov.municipal.suda.modules.property.dao.master.BounceReasonDao;
import gov.municipal.suda.modules.property.dao.master.ModeOfPaymentDao;
import gov.municipal.suda.modules.property.model.master.ModeOfPaymentBean;
import gov.municipal.suda.modules.property.model.master.PaymentAllDropdownBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class PaymentController {
    @Autowired
    ModeOfPaymentDao modeOfPaymentDao;
    @Autowired
    BounceReasonDao bounceReasonDao;
    @Autowired
    BankNameDao bankNameDao;
    @GetMapping({"/PaymentAllDropdown"})
    public ResponseEntity<List<PaymentAllDropdownBean>> paymentAllDropdown() {
        PaymentAllDropdownBean paymentAllDropdownBean=new PaymentAllDropdownBean();
        paymentAllDropdownBean.setModeOfPaymentBeans(modeOfPaymentDao.findAll().stream().filter(v->v.getStatus()==1).collect(Collectors.toList()));
        paymentAllDropdownBean.setBankNameBeans(bankNameDao.findAll());
        paymentAllDropdownBean.setChequeDDBounceReasonBeans(bounceReasonDao.findAll());
        return ResponseEntity.ok(Collections.singletonList(paymentAllDropdownBean));
    }
}
