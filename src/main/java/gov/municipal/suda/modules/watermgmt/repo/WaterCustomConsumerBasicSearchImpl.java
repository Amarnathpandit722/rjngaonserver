package gov.municipal.suda.modules.watermgmt.repo;

import gov.municipal.suda.modules.watermgmt.model.master.ConsumerBasicDetailsBean;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class WaterCustomConsumerBasicSearchImpl implements WaterCustomConsumerBasicSearch{
    private EntityManager em;
    @Override
    public List<ConsumerBasicDetailsBean> findConsumerBasicDetailsByMobileAndConsumerName(String consumerName, Long mobileNo) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConsumerBasicDetailsBean> cq = cb.createQuery(ConsumerBasicDetailsBean.class);
        Root<ConsumerBasicDetailsBean> consumerBasicDetails = cq.from(ConsumerBasicDetailsBean.class);
        List<Predicate> predicates = new ArrayList<>();
        if(!consumerName.isEmpty()) {
            predicates.add(cb.like(consumerBasicDetails.get("name"), "%" + consumerName + "%"));
        }
        if(mobileNo!=null) {
            predicates.add(cb.equal(consumerBasicDetails.get("mobile_no"), mobileNo));
        }

        cq.select(consumerBasicDetails)
                .where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
