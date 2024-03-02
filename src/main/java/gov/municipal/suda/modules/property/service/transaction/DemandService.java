package gov.municipal.suda.modules.property.service.transaction;

import gov.municipal.suda.modules.property.dto.*;
import gov.municipal.suda.modules.property.model.master.OwnerTaxMasterBean;
import gov.municipal.suda.modules.property.model.transaction.DemandDetailsBean;

import java.util.List;

public interface DemandService {

    String createDemand(List<DemandRequestDto> demandRequest, Long user_id) throws  Exception;
    String createDemandDuringNewAssessment(List<NewDemandRequestDTO> newDemandRequestDTO) throws Exception;
    String createDemandDuringReAssessment(List<NewDemandRequestDTO> newDemandRequestDTO) throws Exception;

    List<DemandDetailsBean> getDemandDetailsByPropId(Long propId);

    List<Object[]> getDueDetailsByPropId(Long propId)throws Exception;
}
