package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;


import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AreaDetailsDto {
    @NotNull
    private Long consumer_cat_mstr_id;
    @NotNull
    private Long consumer_range_mstr_id;
    @NotNull
    private Long consumer_rate_mstr_id;
    @NotNull
    private Long rate_chart_id;
    @NotNull
    private String financial_year;
    @NotNull
    private Long financial_year_id;
    @NotNull
    private Long month;
    private Long  ward_id;
    private Long created_byid;
    private BigDecimal monthlyAmount;
    private Integer noOfRoomTable;
//    private AreaDetailsDto(final String financial_year) {
//        if (StringUtils.isBlank(financial_year)) {
//            throw new IllegalArgumentException("Financial Year can't be blank/empty/null");
//        }
//
//        this.financial_year=financial_year;
//    }
}
