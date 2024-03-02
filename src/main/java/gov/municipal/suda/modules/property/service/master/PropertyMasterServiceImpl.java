package gov.municipal.suda.modules.property.service.master;

import gov.municipal.suda.exception.BadRequestException;
import gov.municipal.suda.exception.RecordNotFoundException;
import gov.municipal.suda.modules.property.dao.master.*;
import gov.municipal.suda.modules.property.dao.transaction.PropertySearchViewDao;
import gov.municipal.suda.modules.property.dao.transaction.SAFARVDetailsDao;
import gov.municipal.suda.modules.property.dto.PropertySEarchViewDTO;
import gov.municipal.suda.modules.property.dto.PropertySearchDTO;
import gov.municipal.suda.modules.property.model.master.*;
import gov.municipal.suda.modules.property.model.transaction.SAFARVDetailsBean;
import gov.municipal.suda.modules.property.model.transaction.SAFViewDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
@Slf4j
public class PropertyMasterServiceImpl implements PropertyMasterService{

    @Autowired
    private PropertyMasterDao propertyMasterDao;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private WardDao wardDao;
    @Autowired
    private RoadTypeDao roadTypeDao;
    @Autowired
    private PropertyTypeDao propertyTypeDao;

    @Autowired
    private FinYearDao finYearDao;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PropertyDocMstrDao propertyDocMstrDao;
   
    @Autowired
    OwnerDetailsDao ownerDetailsDao;
    @Autowired
    SAFARVDetailsDao safarvDetailsDao;
    @Autowired
    UsesTypeDao usesTypeDao;
    @Autowired
    PropertySearchViewDao propertySearchViewDao;
    @Override
    public PropertyMasterBean propertyMasterEntry(PropertyMasterBean propertyMasterBean) {

       roadTypeDao.findById(propertyMasterBean.getRoad_type_id()).orElseThrow(()->
                new RecordNotFoundException(messageSource.getMessage("road.type.not.found",
                      new Object[] {propertyMasterBean.getRoad_type_id()}, LocaleContextHolder.getLocale()))
                      // new RecordNotFoundException("Exception")
                        );

        wardDao.findWardName(propertyMasterBean.getWard_id().toString()).orElseThrow(()->
                new RecordNotFoundException(messageSource.getMessage("ward.not.found",
                        new Object[] {propertyMasterBean.getWard_id()}, LocaleContextHolder.getLocale())));

        propertyTypeDao.findById(propertyMasterBean.getProperty_type_id()).orElseThrow(()->
                new RecordNotFoundException(messageSource.getMessage("prop.type.not.found",
                        new Object[] {propertyMasterBean.getProperty_type_id()}, LocaleContextHolder.getLocale())));

        finYearDao.findById(propertyMasterBean.getEntry_fy_id()).orElseThrow(()->
                new RecordNotFoundException(messageSource.getMessage("fin.year.not.found",
                       new Object[] {propertyMasterBean.getEntry_fy_id()}, LocaleContextHolder.getLocale())));

        //Long newPropertyNo=propertyMasterDao.newPropertyNo();
        String newPropertyNo="N210200001";
        //log.info("Ne property No generated ",newPropertyNo);
        propertyMasterBean.setProperty_no(newPropertyNo);

        return propertyMasterDao.save(propertyMasterBean);
    }

    @Override
    public
    List<Object[]> getAllProperty(Long ward_id, String owner_name, String property_no) {
        String jpql = "";

        if(ward_id==null && owner_name.equals("") && property_no.equals("")) {
            throw new BadRequestException("All inputs must not be null");
        }
        else if(property_no.equals("") && owner_name.equals("") && ward_id!=null ){
            jpql="SELECT distinct p.id,r.ward_name,p.property_no,p.application_no,p.entry_type, q.owner_name,q.owner_address,p.entry_fy_id,p.ward_id,s.fy_name FROM tbl_property_mstr p INNER JOIN tbl_prop_owner_details q on p.id=q.prop_id LEFT JOIN tbl_ward_mstr r on r.id=p.ward_id left join tbl_fy s on s.id=p.entry_fy_id where p.ward_id="+ward_id+" and p.status=1 ";
        }
        else if(ward_id==null && owner_name.equals("") && !property_no.equals("")){
            jpql="SELECT distinct p.id,r.ward_name,p.property_no,p.application_no,p.entry_type, q.owner_name,q.owner_address,p.entry_fy_id,p.ward_id,s.fy_name FROM tbl_property_mstr p INNER JOIN tbl_prop_owner_details q on p.id=q.prop_id LEFT JOIN tbl_ward_mstr r on r.id=p.ward_id left join tbl_fy s on s.id=p.entry_fy_id where p.property_no='"+property_no+"' and p.status=1";
        }
        else if(ward_id==null  && property_no.equals("") && !owner_name.equals("")) {
            jpql="SELECT distinct p.id,r.ward_name,p.property_no,p.application_no,p.entry_type, q.owner_name,q.owner_address,p.entry_fy_id,p.ward_id,s.fy_name FROM tbl_property_mstr p INNER JOIN tbl_prop_owner_details q on p.id=q.prop_id LEFT JOIN tbl_ward_mstr r on r.id=p.ward_id left join tbl_fy s on s.id=p.entry_fy_id where q.owner_name LIKE '%"+owner_name+"%' and p.status=1";
        }
        else if(owner_name.equals("") && ward_id!=null  && !property_no.equals("")){
            jpql="SELECT distinct p.id,r.ward_name,p.property_no,p.application_no,p.entry_type, q.owner_name,q.owner_address,p.entry_fy_id,p.ward_id,s.fy_name FROM tbl_property_mstr p INNER JOIN tbl_prop_owner_details q on p.id=q.prop_id LEFT JOIN tbl_ward_mstr r on r.id=p.ward_id left join tbl_fy s on s.id=p.entry_fy_id where p.ward_id="+ward_id+" and p.property_no='"+property_no+"' and p.status=1";
        }
        else if(property_no.equals("") && !owner_name.equals("") && ward_id!=null ){
            jpql="SELECT distinct p.id,r.ward_name,p.property_no,p.application_no,p.entry_type, q.owner_name,q.owner_address,p.entry_fy_id,p.ward_id,s.fy_name FROM tbl_property_mstr p INNER JOIN tbl_prop_owner_details q on p.id=q.prop_id LEFT JOIN tbl_ward_mstr r on r.id=p.ward_id left join tbl_fy s on s.id=p.entry_fy_id where p.ward_id="+ward_id+" and q.owner_name LIKE '%"+owner_name+"%' and p.status=1";
        }
        else if(ward_id==null && !owner_name.equals("") && !property_no.equals("")){
            jpql="SELECT distinct p.id,r.ward_name,p.property_no,p.application_no,p.entry_type, q.owner_name,q.owner_address,p.entry_fy_id,p.ward_id,s.fy_name FROM tbl_property_mstr p INNER JOIN tbl_prop_owner_details q on p.id=q.prop_id LEFT JOIN tbl_ward_mstr r on r.id=p.ward_id left join tbl_fy s on s.id=p.entry_fy_id where p.property_no='"+property_no+"' and q.owner_name LIKE '%"+owner_name+"%' and p.status=1";
        }
        else if(ward_id!=null  && !owner_name.equals("") && !property_no.equals("")){
            jpql="SELECT distinct p.id,r.ward_name,p.property_no,p.application_no,p.entry_type, q.owner_name,q.owner_address,p.entry_fy_id,p.ward_id,s.fy_name FROM tbl_property_mstr p INNER JOIN tbl_prop_owner_details q on p.id=q.prop_id LEFT JOIN tbl_ward_mstr r on r.id=p.ward_id left join tbl_fy s on s.id=p.entry_fy_id where p.ward_id="+ward_id+" and p.property_no='"+property_no+"' and q.owner_name LIKE '%"+owner_name+"%' and p.status=1";
        }
        //String jpql = "SELECT p.ward_id,p.property_no,p.application_no,p.entry_type, q.owner_name,q.owner_address FROM PropertyMasterBean p JOIN gov.municipal.suda.modules.property.model.master.OwnerDetailsBean q on p.id=q.prop_id where p.ward_id="+ward_id+" and (p.property_no='"+property_no+"' OR p.application_no='"+application_no+"' OR q.owner_name LIKE '%"+owner_name+"%')";
        //Query query = entityManager.createQuery(jpql);
        Query query = entityManager.createNativeQuery(jpql);
        log.info("query................"+jpql);
        List<Object[]> results = query.getResultList();
        return results;
    }

    @Override
    public List<PropertyDocMstrBean> fetchAllDocumentName() {
        return propertyDocMstrDao.findAll();
    }

    @Override
    public Optional<SAFViewDTO> getPropertyByPropNo(String property_no) {
        SAFViewDTO sAFViewDTO=new SAFViewDTO();
        Long prop_id = propertyMasterDao.findIdByPropNo(property_no);
        sAFViewDTO.setPropertyMasterBean(propertyMasterDao.fetchAllPropById(prop_id));
        sAFViewDTO.setOwnerDetailsBean(ownerDetailsDao.findOwnerDetails(prop_id).stream().collect(Collectors.toList()));
        //List<SAFARVDetailsBean> floorDetails=safarvDetailsDao.findSAFARVDetails(prop_id);

        sAFViewDTO.setSafarvDetailsBean(safarvDetailsDao.findSAFARVDetails(prop_id).stream().collect(Collectors.toList()));

        return Optional.of(sAFViewDTO);
    }

    @Override
    public List<PropertySearchDTO> getPropertyDetailsByPropId(Long propId) {
        List<PropertySearchDTO> propertySearchDTOS= new ArrayList<>();
        PropertySearchDTO propertySearchDTO = new PropertySearchDTO();
        List<PropertyMasterBean> propertyMasterBeans=propertyMasterDao.fetchAllPropById(propId);
        String ward_name=wardDao.findWardNameById(propertyMasterBeans.get(0).getWard_id());
        String property_type=propertyTypeDao.findPropertTypeNameById(propertyMasterBeans.get(0).getProperty_type_id());
        //String fy_name=finYearDao.findYearNameById(propertyMasterBeans.get(0).getEntry_fy_id());
        String uses_type_name=usesTypeDao.findUsesTypeNameById(propertyMasterBeans.get(0).getUsage_type_id());
        List<SAFARVDetailsBean> safarvDetailsBeans=safarvDetailsDao.findSAFARVDetails(propId);
        Optional<SAFARVDetailsBean> latestObject = safarvDetailsBeans.stream()
                .sorted(Comparator.comparing(SAFARVDetailsBean::getEffect_year).reversed()) // sort by stampdate in descending order
                .findFirst();

        propertySearchDTO.setWard_id(ward_name);
        propertySearchDTO.setProperty_no(propertyMasterBeans.get(0).getProperty_no());
        propertySearchDTO.setProperty_type_name(property_type);
        propertySearchDTO.setTotalbuilbup_area(propertyMasterBeans.get(0).getTotalbuilbup_area());
        propertySearchDTO.setProperty_address(propertyMasterBeans.get(0).getProperty_address());
        propertySearchDTO.setFy_name(latestObject.get().getEffect_year());
        propertySearchDTO.setPlot_no(propertyMasterBeans.get(0).getPlot_no());
        propertySearchDTO.setEntry_type(propertyMasterBeans.get(0).getEntry_type());
        propertySearchDTO.setUses_type_name(uses_type_name);
        propertySearchDTO.setMohalla(propertyMasterBeans.get(0).getMohalla());
        propertySearchDTO.setBuilding_name(propertyMasterBeans.get(0).getBuilding_name());
        propertySearchDTO.setStampdate(propertyMasterBeans.get(0).getStampdate());
        propertySearchDTO.setKhata_no(propertyMasterBeans.get(0).getKhata_no());
        propertySearchDTO.setCity(propertyMasterBeans.get(0).getCity());
        propertySearchDTO.setDistrict(propertyMasterBeans.get(0).getDistrict());
        propertySearchDTO.setPincode(propertyMasterBeans.get(0).getPincode());
        propertySearchDTOS.add(propertySearchDTO);
        return propertySearchDTOS;
    }

    @Override
    public Page<PropertySEarchViewDTO> getPropDetailsByPropId(Long ward_id, String owner_name, String property_no, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PropertySEarchViewDTO> query = builder.createQuery(PropertySEarchViewDTO.class);
        Root<PropertySEarchViewDTO> root = query.from(PropertySEarchViewDTO.class);

        if(ward_id != null) {
            query.where(builder.and(builder.equal(root.get("ward_id"), ward_id)));
        }
        else if(ward_id != null && owner_name != null) {
            query.where(builder.equal(root.get("ward_id"), ward_id),
                    builder.equal(root.get("owner_name"), owner_name));
        }
        else if(ward_id != null && property_no != null) {
            query.where(builder.equal(root.get("ward_id"), ward_id),
                    builder.equal(root.get("property_no"), property_no));
        }
        else {
            query.select(root);
        }

        TypedQuery<PropertySEarchViewDTO> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<PropertySEarchViewDTO> entities = typedQuery.getResultList();

        Long count = countEntitiesByParameters(ward_id, owner_name,property_no);

        return new PageImpl<>(entities, pageable, count);
        //return  propertySearchViewDao.findAll(pageable);
    }
    private Long countEntitiesByParameters(Long ward_id,String owner_name, String property_no) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<PropertySEarchViewDTO> root = query.from(PropertySEarchViewDTO.class);

        if(ward_id != null) {
            query.select(builder.count(root)).where(builder.and(builder.equal(root.get("ward_id"), ward_id)));
        }
        else if(ward_id != null && owner_name != null) {
            query.select(builder.count(root)).where(builder.equal(root.get("ward_id"), ward_id),
                    builder.and(builder.equal(root.get("owner_name"), owner_name)));
        }
        else if(ward_id != null && property_no != null) {
            query.select(builder.count(root)).where(builder.equal(root.get("ward_id"), ward_id),
                    builder.and(builder.equal(root.get("property_no"), property_no)));
        }
        else {
            query.select(builder.count(root));
        }

        return entityManager.createQuery(query).getSingleResult();
    }

	@Override
	public Long generateProperty_No(long ward_id, long zone_id) {
		// TODO Auto-generated method stub
		long generatedPropertyNo= 0L;
		long dummyId = propertyMasterDao.generatePropertyNo();
		
			String formattedWardId = String.format("%02d",zone_id );
			 String generatedPropertyNoString =formattedWardId + String.format("%02d%06d", ward_id, dummyId);
			 generatedPropertyNo= Long.parseLong(generatedPropertyNoString)		;
			
		
		
		
		
		return generatedPropertyNo;
	}

}
