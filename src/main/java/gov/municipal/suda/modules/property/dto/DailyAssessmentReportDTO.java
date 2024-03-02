package gov.municipal.suda.modules.property.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@RequiredArgsConstructor

public class DailyAssessmentReportDTO {
	private String wardid;
	private String assessmentType;
	private String proertyNo;
	private String PropertyAddress;
	private String entryDate;
	private String ownerName;
	private Long UserId;
	private BigDecimal total_amount_sum;
	private BigDecimal common_wtr_tax_sum;
	private BigDecimal education_cess_sum;

	private String user_name;
	
	
	
	
	
	
	
	
	
}
