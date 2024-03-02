package gov.municipal.suda.modules.watermgmt.model.master;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_water_mode_of_payment",schema = "watermgmt", catalog = "")
public class WaterModeOfPaymentBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mode_of_payment;
    private Integer status;
}
