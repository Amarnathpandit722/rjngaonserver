package gov.municipal.suda.modules.property.dto;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@NamedStoredProcedureQuery(
//        name = "BulkReportByTC",
//        procedureName = "public.get_bulk_receipt_report",
//        resultClasses = PaymentReceiptResponse.class,
//        parameters = {
//                @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, type = void.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN,  type = String.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN,  type = Long.class),
//                @StoredProcedureParameter(mode = ParameterMode.IN,  type = String.class)
//
//        })
//
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class BulkPaymentReceipt {
    //@Id
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
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<PayReceiptFloorDTO> floor_details;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private PaymentReceiptDetailsDTO previous_year;
    @Column(columnDefinition = "jsonb")
    private PaymentReceiptDetailsDTO current_year;
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
    //private String online_no;
    //private String drawn_on;
    private String uses_type_name;
    private String check_no;
    private String check_dt;
    private String card_holder_name;
    private String card_type;
    private Long check_status;

}
