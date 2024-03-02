package gov.municipal.suda.modules.property.model.transaction;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_composite_tax")
public class CompositeTax {

	 	@Id
	    @Generated
	    private Long id;
	 private Integer status;
	 private String year;
	 private String CalulationRate;
	 private String calulationType;
	 
	 
	 
}
