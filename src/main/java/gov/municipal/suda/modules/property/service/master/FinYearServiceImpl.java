package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.modules.property.dao.master.FinYearDao;
import gov.municipal.suda.modules.property.dto.PropertyViewDTO;
import gov.municipal.suda.modules.property.model.master.FinYearBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FinYearServiceImpl implements FinYearService{
    @Autowired
    private FinYearDao finYearDao;
    @Autowired
    private EntityManager entityManager;
    @Override
    public Optional<List<FinYearBean>> findAllFinYear() {
        return Optional.of(finYearDao.findAll());
    }

    @Override
    public List<FinYearBean> getFinancialYear() {
        String jpql = "select f.id,r.effect_date from tbl_ratechange_effect r  left join tbl_fy f on r.effect_year=f.fy_name where r.status=1";
        Query query = entityManager.createNativeQuery(jpql);
        log.info("query................"+jpql);
        List<Object[]> results = query.getResultList();
        List<FinYearBean> years= new ArrayList<>();
        for (Object[] result : results) {
            FinYearBean finYearBean=new FinYearBean();
            finYearBean.setYr_id((BigInteger) result[0]);
            finYearBean.setEffective_date((String) result[1]);
            years.add(finYearBean);
        }
        return years;
    }
}
