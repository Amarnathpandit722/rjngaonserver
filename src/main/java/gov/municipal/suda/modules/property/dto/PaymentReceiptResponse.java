package gov.municipal.suda.modules.property.dto;

import gov.municipal.suda.modules.property.model.transaction.ARVDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.SAFARVDetailsBean;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReceiptResponse {


    private String receipt_no;
    private String department_section;
    private String account_description;
    private String ward_no;
    private String date;
    private String property_no;
    private Long propertyTypeId;
    private String owner_name;
    private String address;
    private String mobile_no;
    private BigDecimal total_builtup_area;
    private List<PayReceiptFloorDTO> floor_details;
    private List<PaymentReceiptDetailsDTO> payment_details;
    private BigDecimal payable_amount;
    private String mode_of_payment;
    private String bank_name;
    private String branch_location;
    private String effect_year;
    private BigDecimal penal_charge;
    private BigDecimal form_fee;
    private BigDecimal penalty_amount;
    private BigDecimal adjustment_amount;
    private BigDecimal diference_amount;
    private BigDecimal total;
    private BigDecimal rain_water_harvesting;
    private BigDecimal receivable_amount;
    private String online_no;
    private String drawn_on;
    private String uses_type_name;
    private String check_no;
    private String check_dt;
    private String card_holder_name;
    private String card_type;
    private Long check_status;

}
