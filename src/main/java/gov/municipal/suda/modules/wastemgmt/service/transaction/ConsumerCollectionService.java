package gov.municipal.suda.modules.wastemgmt.service.transaction;

public interface ConsumerCollectionService {
    void createConsumerCollection(Long id, String effectYear, Long userId, Long getMaxValue);
}
