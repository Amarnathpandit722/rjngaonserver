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
@Table(name = "tbl_prop_reassessed_rec")
public class LegacyEntryTypeBean {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long ward_id;
    private String holding_no; // property_no
    private String status;

}
