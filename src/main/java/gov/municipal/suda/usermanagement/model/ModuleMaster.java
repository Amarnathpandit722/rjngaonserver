package gov.municipal.suda.usermanagement.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_module_mstr")
public class ModuleMaster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String module_name;
    private String module_short_name;
    private String Description;

}
