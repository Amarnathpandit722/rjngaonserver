package gov.municipal.suda.modules.property.model.master;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.hibernate.annotations.Formula;
import org.springframework.boot.autoconfigure.web.WebProperties;


import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_arv_range")
public class ArvRangeBean {

    @Id
    @Generated
    private Long id;
    private Long range_from;
    private Long range_to;
    private BigDecimal percentage;
    private String effect_date;
    private Long user_id;
    private LocalDateTime stampdate;
    private BigDecimal commercial_per;




}
