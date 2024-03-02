package gov.municipal.suda.modules.watermgmt.dto.transaction;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerUpdateDto {
    private String consumerNo;
    private String oldConsumerNo;
    private String consumerName;
    private Long mobileNo;
    private String relation;
    private String guardianName;
    private String wardNo;
    private String propertyNo;
    private String propertyType;
    private Long propertyTypeId;
    private String propertyAddress;
    private String connectionType;
    private String meterNo;
    private Long initialMeterReading;
    private Long noOfConnection; //in database field name is no_table_room;
    private String dateOfConnection;
    private List<ConsumerUnitRateDetailsDto> consumerUnitRateDetails;
    private List<Long> demandId;
    private BigDecimal demandAmount;
    private BigDecimal penalty;
    private BigDecimal totalPayableAmount;
    private String maxEffectFrom;


}
