package gov.municipal.suda.modules.property.dto;

import lombok.*;
import org.hibernate.annotations.Formula;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ArvRangeDTO {
   // private String range_from;
    //private String range_to;
    //private String effect_date;
    //@Formula("concat(range_from :: varchar,' TO ' :: varchar ,range_to::varchar, ' => ' :: varchar,effect_date :: varchar)")
    private Long id;
    private String arvRange;

}
