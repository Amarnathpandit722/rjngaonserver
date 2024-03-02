package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAllDropdownBean {
    private List<ModeOfPaymentBean> modeOfPaymentBeans;
    private List<BankNameBean> bankNameBeans;
    private List<ChequeDDBounceReasonBean> chequeDDBounceReasonBeans;
}
