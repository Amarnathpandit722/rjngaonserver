package gov.municipal.suda.modules.wastemgmt.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;



@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class RangeAndRateDto {
    private Long id;
    private Long consumer_cat_mstr_id;
    private String range_name;
    private Long rateId;
    private BigDecimal rate;

    private String dateOfEffect;

    public String getDateOfEffect() {
        return dateOfEffect.substring(0,10);
    }

    public void setDateOfEffect(String dateOfEffect) {
        this.dateOfEffect = dateOfEffect;
    }
}
