package gov.municipal.suda.modules.wastemgmt.service.transaction;

import gov.municipal.suda.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class ConsumerCollectionServiceImpl implements ConsumerCollectionService{
    @Override
    @Transactional(rollbackOn = BadRequestException.class)
    public void createConsumerCollection(Long id, String effectYear, Long userId, Long getMaxValue) {

    }
}
