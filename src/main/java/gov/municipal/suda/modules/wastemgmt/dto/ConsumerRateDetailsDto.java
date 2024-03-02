package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsumerRateDetailsDto extends AreaDetailsDto{
    @NotNull
    private Long consumer_details_id;
}
