package gov.municipal.suda.modules.property.model.master;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReciptDto {
    private String uses_type_name; //property_master
    private BigInteger ward_id; //property_master
    private String property_no; //property_master
    private BigDecimal totalbuilbup_area; //property_master
    private Timestamp entry_date; //property_master
    private String owner_name; // owner_details
    private String owner_address; // owner_details
    private BigInteger mobile_no; // owner_details
    private String receipt_no; //last_payment_record
    private String frm_year; //last_payment_record
    private String upto_year; //last_payment_record
    private BigDecimal tot_amount; //last_payment_record

}
