package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.modules.property.dao.master.LastPaymentRecordDao;
import gov.municipal.suda.modules.property.dao.transaction.CashTransactionDao;
import gov.municipal.suda.modules.property.dao.transaction.CashTransactionDetailDao;
import gov.municipal.suda.modules.property.dao.transaction.CollectionDao;
import gov.municipal.suda.modules.property.dao.transaction.DemandDao;
import gov.municipal.suda.modules.property.dto.PaymentReceiptDTO;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.PropertyCollectionMstrBean;
import gov.municipal.suda.modules.property.model.transaction.PropertyTransactionBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    CollectionDao collectionDao;
    @Autowired
    private DemandDao demandDao;
    @Autowired
    private CashTransactionDao cashTransactionDao;
    @Autowired
    private CashTransactionDetailDao cashTransactionDetailDao;
    @Autowired
    EntityManager entityManager;
    @Override
    public List<PaymentReceiptDTO> getPaymentDetailsByPropId(Long propId) {
        List<PaymentReceiptDTO> paymentReceiptDTOS = new ArrayList<>();
        try {
            List<PropertyTransactionBean> transactionBeans = cashTransactionDao.fetchTransactionById(propId);
            if (!transactionBeans.isEmpty()) {
                String fromYear=null;
                String upToYear=null;
                for (PropertyTransactionBean payment : transactionBeans) {
                    Long cheque_status = 0L;
                    if (!payment.getPayment_mode().equalsIgnoreCase("Cash")) {
                        String StrChequeStatus = cashTransactionDetailDao.findChkStatusByTrnId(payment.getId());
                        if (StrChequeStatus == null || StrChequeStatus.equals("")) {
                            StrChequeStatus = "0";
                        }
                        cheque_status = Long.valueOf(StrChequeStatus);
                    }
                    List<PropertyCollectionMstrBean> collectionList=collectionDao.findCollectionByPropIdAndTransactionId(propId,payment.getId());
                    Set<Long> demandIds = collectionList.stream().map(v->v.getDemand_id()).collect(Collectors.toSet());
                    List<DemandDetailsBean> paidDemandList= demandDao.findPaidDemandByPropIdAndDemandIds(propId,demandIds);
                    if(paidDemandList.size()==1) {
                      fromYear=paidDemandList.get(0).getEffect_year();
                      upToYear=paidDemandList.get(0).getEffect_year();
                    }
                    else if(paidDemandList.size()>1) {
                        fromYear=paidDemandList.get(0).getEffect_year();
                        upToYear=paidDemandList.get(paidDemandList.size()-1).getEffect_year();
                    }
                    PaymentReceiptDTO paymentReceipt = new PaymentReceiptDTO();
                    paymentReceipt.setBook_no(null); // need to ask what is the book no
                    paymentReceipt.setEntry_date(String.valueOf(payment.getTransaction_date()).substring(0,10));
                    paymentReceipt.setFine_amount(payment.getPenalty());
                    paymentReceipt.setFrm_year(fromYear);
                    paymentReceipt.setId(payment.getId());
                    paymentReceipt.setProp_id(payment.getProp_id());
                    paymentReceipt.setReceipt_date(String.valueOf(payment.getTransaction_date()));
                    paymentReceipt.setReceipt_no(payment.getTransaction_no());
                    paymentReceipt.setRecpt_filename(null);
                    paymentReceipt.setTime(String.valueOf(payment.getStampdate()));
                    paymentReceipt.setTot_amount(payment.getPayable_amt());
                    paymentReceipt.setUpto_year(upToYear);
                    paymentReceipt.setUser_id(payment.getUser_id());
                    paymentReceipt.setTransaction_no(payment.getTransaction_no());
                    paymentReceipt.setPayment_mode(payment.getPayment_mode());
                    paymentReceipt.setCheck_status(cheque_status);
                    paymentReceiptDTOS.add(paymentReceipt);
                }
            }
        }
        catch (IndexOutOfBoundsException  ex){
            System.out.println("An IndexOutOfBoundsException occurred: " + ex.getMessage());
        }
        return paymentReceiptDTOS;
    }

    @Override
    public List<Object[]> getPaymentReceiptPropId(Long propId, String frmYear, String uptoYear) {
        String jpql="select b.property_no,b.ward_id,d.uses_type_name,b.totalbuilbup_area,b.entry_date,\n" +
                "c.owner_name,c.owner_address,c.mobile_no,a.receipt_no,a.frm_year,a.upto_year,a.tot_amount from\n" +
                "tbl_prop_last_payment_record a left join tbl_property_mstr b on b.id=a.prop_id left join \n" +
                "tbl_prop_owner_details c on c.prop_id=a.prop_id left join tbl_uses_type d on b.usage_type_id=d.id  where a.prop_id="+propId+" and a.frm_year='"+frmYear+"' and a.upto_year='"+uptoYear+"'";
        Query query = entityManager.createNativeQuery(jpql);
        log.info("query................"+jpql);
        List<Object[]> results = query.getResultList();
        return results;
    }
}
