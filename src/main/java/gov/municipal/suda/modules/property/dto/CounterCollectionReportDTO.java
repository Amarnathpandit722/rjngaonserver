package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedStoredProcedureQuery(name = "CounterCollectionReport",
        procedureName = "public.get_counter_collection_report",
        resultClasses = CounterCollectionReportDTO.class,
        parameters = {
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, type = void.class),
        @StoredProcedureParameter(mode = ParameterMode.IN,  type = Timestamp.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, type = Timestamp.class),
        @StoredProcedureParameter(mode = ParameterMode.IN,  type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN,  type = Long.class)

        })

public class CounterCollectionReportDTO {
    @Id
    private Long ward_id;
    private String ward_name;
    private String property_no;
    private String owner_name;
    private String mobile_no;
    private String transaction_no;
    private String transaction_date;
    private String payment_mode;
    private String bank_name;
    private String branch_name;
    private String cheque_dd_aprcode;
    private BigDecimal property_tax;
    private BigDecimal aproperty_tax;
    private BigDecimal composite_tax;
    private BigDecimal acomposite_tax;
    private BigDecimal education_cess;
    private BigDecimal aeducation_cess;
    private BigDecimal penalty;
    private BigDecimal  penal_charge;
    private BigDecimal rain_harvest_charge;
    private BigDecimal form_fee;
    private BigDecimal discount;
    private BigDecimal tot_amount;
    private String tax_collector;
    private Long user_id;
    private String stampdate;
    private String for_year;
}
