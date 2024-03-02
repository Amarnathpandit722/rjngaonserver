package gov.municipal.suda.modules.property.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_teamcollection_view")
public class TeamWiseCollectionDTO {
    @Id
    private Long id;
    private String collector_name;
    private String user_type;
    private BigInteger property_count;
    private BigDecimal total_collection;
    private Timestamp stampdate; // not possible to grouping
    private BigInteger user_id;
    private String payment_mode; // not possible to grouping
    private BigDecimal CashTotal;
    private BigDecimal CardTotal;
    private BigDecimal ChequeTotal;
    private BigDecimal DDTotal;
    private BigDecimal RTGSTotal;
    private BigDecimal NEFTTotal;
}
