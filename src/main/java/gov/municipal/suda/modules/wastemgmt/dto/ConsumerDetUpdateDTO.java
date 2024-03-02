package gov.municipal.suda.modules.wastemgmt.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerDetUpdateDTO {
    private String consumer_no;
    private String consumer_name;
    private Long mobile_no;
    private String address;
    private String name;
}
