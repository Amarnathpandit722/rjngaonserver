package gov.municipal.suda.modules.property.model.transaction;

import gov.municipal.suda.modules.property.model.master.OwnerDetailsBean;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Cacheable
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class SAFEntryPK implements Serializable {
    // this class use for composite key joining
    @Getter
    @Setter
    @Column(name="prop_id")
    private Long property_id;

    @Getter
    @Setter
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE})
    @JoinColumn(name="id", referencedColumnName = "prop_id")
    private OwnerDetailsBean ownerDetails;

}
