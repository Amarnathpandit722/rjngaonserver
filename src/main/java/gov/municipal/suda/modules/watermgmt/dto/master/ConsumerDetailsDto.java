package gov.municipal.suda.modules.watermgmt.dto.master;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerDetailsDto {
            private String oldConsumerNo;
            private String newConsumerNo; //need to autogenerate
            private String holdingNo; //aka propertyNo
            private Long propertyTypeId;
            private String propertyAddress;
            private Long noOfConnection;
            private Long noOfRoom;
            private Long noOfTables;
            private String isNigamEmp;
}
