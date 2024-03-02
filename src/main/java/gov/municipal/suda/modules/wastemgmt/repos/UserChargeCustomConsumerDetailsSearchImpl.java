package gov.municipal.suda.modules.wastemgmt.repos;

import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerDetailsBean;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerMasterBean;
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
public class UserChargeCustomConsumerDetailsSearchImpl implements UserChargeCustomConsumerDetailsSearch{
    private EntityManager em;

    @Override
    public List<ConsumerDetailsBean> fetchCustomConsumerDetailsSearch(String consumer_no, String consumer_name, Long mobile_no) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConsumerDetailsBean> cq = cb.createQuery(ConsumerDetailsBean.class);
        Root<ConsumerDetailsBean> consumerDetails = cq.from(ConsumerDetailsBean.class);
        List<Predicate> predicates = new ArrayList<>();
        if(!consumer_no.isEmpty()) {
            predicates.add(cb.equal(consumerDetails.get("consumer_no"),consumer_no));
        }
        if(!consumer_name.isEmpty()) {
            predicates.add(cb.like(consumerDetails.get("consumer_name"),"%"+ consumer_name+"%"));
        }
        if(mobile_no !=null) {
            predicates.add(cb.equal(consumerDetails.get("mobile_no"),mobile_no));
        }

        cq.select(consumerDetails)
                .where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
