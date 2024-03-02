package gov.municipal.suda.modules.wastemgmt.repos;

import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerMasterBean;
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
public class UserChargeCustomConsumerMasterSearchImpl implements UserChargeCustomConsumerMasterSearch{
    private EntityManager em;
    @Override
    public List<ConsumerMasterBean> fetchCustomConsumerMasterSearch(String ward_id, String holding_no) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ConsumerMasterBean> cq = cb.createQuery(ConsumerMasterBean.class);
        Root<ConsumerMasterBean> consumerMaster = cq.from(ConsumerMasterBean.class);
        List<Predicate> predicates = new ArrayList<>();
        if(!ward_id.isEmpty()) {
            predicates.add(cb.equal(consumerMaster.get("ward_id"),ward_id));
        }
        if(!holding_no.isEmpty()) {
            predicates.add(cb.equal(consumerMaster.get("holding_no"), holding_no));
        }

        cq.select(consumerMaster)
                .where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
