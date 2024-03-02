package gov.municipal.suda.modules.watermgmt.model.master;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_extra_room_charge",schema = "watermgmt", catalog = "")
public class ExtraRoomChargeBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long prop_type_id;
    private BigDecimal amount;
    private String date_of_effect;
}
