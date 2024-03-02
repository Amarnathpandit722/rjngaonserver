package gov.municipal.suda.modules.property.model.master;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_bankname_mstr")
public class BankNameBean {
    @Id
    @GeneratedValue
    private Long id;
    private String bank_name;
}
