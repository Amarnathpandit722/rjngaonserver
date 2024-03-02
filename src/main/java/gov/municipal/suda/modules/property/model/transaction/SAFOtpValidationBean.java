package gov.municipal.suda.modules.property.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Table(name="tbl_saf_otp_validation")
public class SAFOtpValidationBean implements Serializable {
    @Id
    private Long id;
    private Long prop_id;
    private int otp;
    private Long mobile_no;
    private String message;
    private LocalDateTime stampdate;
}
