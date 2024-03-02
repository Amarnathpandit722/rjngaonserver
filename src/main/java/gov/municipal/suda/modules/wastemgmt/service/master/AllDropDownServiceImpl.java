package gov.municipal.suda.modules.wastemgmt.service.master;

import gov.municipal.suda.modules.property.dao.master.FinYearDao;
import gov.municipal.suda.modules.property.dao.master.WardDao;
import gov.municipal.suda.modules.property.model.master.FinYearBean;
import gov.municipal.suda.modules.property.model.master.WardBean;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerCategoryDao;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerRangeDao;
import gov.municipal.suda.modules.wastemgmt.dao.master.ConsumerRateChartDao;
import gov.municipal.suda.modules.wastemgmt.dto.AllDropDownDto;
import gov.municipal.suda.modules.wastemgmt.dto.CategoryRangeRateDto;
import gov.municipal.suda.modules.wastemgmt.dto.RangeAndRateDto;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerCategoryMasterBean;
import gov.municipal.suda.modules.wastemgmt.model.master.ConsumerRangeMasterBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AllDropDownServiceImpl implements AllDropDownService{
    @Autowired
    FinYearDao finYearDao;
    @Autowired
    WardDao wardDao;
    @Autowired
    ConsumerCategoryDao consumerCategoryDao;
    @Autowired
    ConsumerRangeDao consumerRangeDao;

    @Autowired
    ConsumerRateChartDao consumerRateChartDao;

    @Override
    public AllDropDownDto fetchAllRecords() {
        AllDropDownDto response = new AllDropDownDto();
        List<ConsumerCategoryMasterBean> category=consumerCategoryDao.findAll();
        List<ConsumerRangeMasterBean> rangeResults=consumerRangeDao.findAll();
        Set<Long> consumerRangeMasterId=rangeResults.stream().map(v->v.getId()).collect(Collectors.toSet());
        Set<RangeAndRateDto> rangeAndRateList= consumerRateChartDao.findRateByRangeId(consumerRangeMasterId);
        List<CategoryRangeRateDto> categoryList = new ArrayList<>();
        for(ConsumerCategoryMasterBean cat : category) {
            CategoryRangeRateDto categoryRangeRateDto= new CategoryRangeRateDto();
            categoryRangeRateDto.setId(cat.getId());
            categoryRangeRateDto.setCategory_name(cat.getCategory_name());
            categoryRangeRateDto.setConsumerRangeType(rangeAndRateList.stream().filter(v->v.getConsumer_cat_mstr_id()==cat.getId()).filter(distinctByKey(fields-> Arrays.asList(fields.getId(),fields.getDateOfEffect()))).collect(Collectors.toSet()));
            categoryList.add(categoryRangeRateDto);
        }
        List<WardBean> ward = wardDao.findAll();
        response.setWards(ward);
        List<FinYearBean> finYear=finYearDao.findAll();
        response.setDateOfEffect(finYear);
        response.setCategories(categoryList);


        return response;
    }

    private static  <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
