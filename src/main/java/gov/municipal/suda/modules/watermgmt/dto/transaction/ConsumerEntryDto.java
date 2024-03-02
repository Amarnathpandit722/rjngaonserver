package gov.municipal.suda.modules.watermgmt.dto.transaction;

import gov.municipal.suda.modules.watermgmt.dto.master.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerEntryDto {
 private ConsumerDetailsDto consumerDetails;
 private ConsumerBasicDetailsDto consumerBasicDetails;
 private ConsumerConnectionDetailsDto consumerConnectionDetails;
 private DemandDetailsWaterDto demandDetailsWater;
 private Long rateId;
 private BigDecimal rate;
 private BigDecimal noOfRoomsTableConnection;
 private BigDecimal extraRoomCharge;
 private Long userId;
 private Long wardId;

}
