package gov.municipal.suda.modules.watermgmt.model.transaction;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_wtr_consumer_payment_details",schema = "watermgmt", catalog = "")
public class ConsumerPaymentDetailsBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long consumer_dets_id;
    private Long demand_id;
    private Long ward_id;
    private Long transaction_id;
    private String from_date;
    private String upto_date;
    private BigDecimal penalty;
    private BigDecimal monthly_amount;
    private Long fy_id;
    private String fy_year;
    private String payment_type;
    private String entry_date;
    private String entry_time;
    private Long user_id;
    private Long old_ward_id;

//    @Transient
//    private  String sorted_date;

//    @Transient
//    @JsonGetter("sortedDate")
//    public String getSorted_date() {
//        return sorted_date;
//    }
    //    @PostLoad
//    public void populateField() {
//        sorted_date = new BeanHolder().bean.getClass().;
//    }
//    @Configurable
//    private static class BeanHolder {
//        @Autowired
//        private Bean bean;
//    }
//    @PostLoad
//    public void setSorted_date(String sorted_date) {
//        this.sorted_date = sorted_date;
//    }




}
