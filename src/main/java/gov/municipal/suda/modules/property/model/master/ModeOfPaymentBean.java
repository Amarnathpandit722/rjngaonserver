package gov.municipal.suda.modules.property.model.master;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_mode_payment")
public class ModeOfPaymentBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String mode_of_payment;
    private Integer status;
}
