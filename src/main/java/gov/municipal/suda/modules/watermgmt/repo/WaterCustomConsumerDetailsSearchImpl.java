package gov.municipal.suda.modules.watermgmt.repo;

import gov.municipal.suda.modules.watermgmt.model.master.WaterConsumerDetailsBean;
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
public class WaterCustomConsumerDetailsSearchImpl implements  WaterCustomConsumerDetailsSearch{
   private EntityManager em;

   @Override
    public List<WaterConsumerDetailsBean> findWaterConsumerDetailsBy(Long wardId, String consumerNo, String propertyNo) {
       CriteriaBuilder cb = em.getCriteriaBuilder();
       CriteriaQuery<WaterConsumerDetailsBean> cq = cb.createQuery(WaterConsumerDetailsBean.class);
       Root<WaterConsumerDetailsBean> consumerDetails = cq.from(WaterConsumerDetailsBean.class);
       List<Predicate> predicates = new ArrayList<Predicate>();
       if(wardId!=null) {
           predicates.add((Predicate) cb.equal(consumerDetails.get("ward_id"), wardId));
       }
       if(!consumerNo.isEmpty()) {
           predicates.add(cb.equal(consumerDetails.get("consumer_no"), consumerNo));
       }
       if(!propertyNo.isEmpty()) {
           predicates.add(cb.equal(consumerDetails.get("holding_no"), propertyNo));
       }

       cq.select(consumerDetails)
               .where(predicates.toArray(new Predicate[0]));

       return em.createQuery(cq).getResultList();
    }
}
