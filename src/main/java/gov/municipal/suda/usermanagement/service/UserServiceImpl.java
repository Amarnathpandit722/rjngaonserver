package gov.municipal.suda.usermanagement.service;


import gov.municipal.suda.exception.RecordNotFoundException;
import gov.municipal.suda.usermanagement.dao.DesignationDao;
import gov.municipal.suda.usermanagement.dao.NewUserDao;
import gov.municipal.suda.usermanagement.dao.NewUserDetailsDao;
import gov.municipal.suda.usermanagement.dao.RoleDao;
import gov.municipal.suda.usermanagement.dao.UserDao;
import gov.municipal.suda.usermanagement.dao.UserDetailsDao;
import gov.municipal.suda.usermanagement.model.*;
import gov.municipal.suda.util.Generate;
import gov.municipal.suda.util.enumtype.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDetailsDao userDetailsDao;

    @Autowired
    private DesignationDao designationDao;
    @Autowired
    private EntityManager entityManager;
    

    @Value("${suda.user.name.prefix}")
    private String prefix;

    @Value("${suda.user.name.symbol1}")
    private String symbol1;

    @Value("${suda.user.name.symbol2}")
    private String symbol2;

    

//   @PostConstruct
//	
// public void initRolesAndUser() {
//	    
//		  Role superAdmin_ROle=new Role(); 
//		  superAdmin_ROle.setRoleName("SUPERADMIN");
//		  superAdmin_ROle.setRoleDescription("Super Admin"); 
//		  roleDao.save(superAdmin_ROle);
//		  
//	  Role adminRole=new Role();
//	  adminRole.setRoleName("ADMIN");
//	  adminRole.setRoleDescription("Admin Role");
//	  roleDao.save(adminRole);
//	 
// 
//	  Role pm=new Role(); 
//	  pm.setRoleName("PM");
//	  pm.setRoleDescription("Project Manager"); 
//	  roleDao.save(pm);
//	  
//	  Role apm=new Role(); 
//	  apm.setRoleName("APM");
//	  apm.setRoleDescription("Assitant Project Manager"); 
//	  roleDao.save(apm);
//	  
//	  
//	  Role co=new Role(); 
//	  co.setRoleName("CO");
//	  co.setRoleDescription("Circle Officer/Manager"); 
//	  roleDao.save(co);
//	  
//	  Role tl=new Role(); 
//	  tl.setRoleName("TL");
//	  tl.setRoleDescription("Team Leader"); 
//	  roleDao.save(tl);
//	  
//	  Role tc=new Role(); 
//	  tc.setRoleName("TC");
//	  tc.setRoleDescription("Tax Collector"); 
//	  roleDao.save(tc);
//	  
//	  Role boe=new Role(); 
//	  boe.setRoleName("BOE");
//	  boe.setRoleDescription("Back Office T/L"); 
//	  roleDao.save(boe);
//	  
//	  Role bo=new Role(); 
//	  bo.setRoleName("BO");
//	  bo.setRoleDescription("Back Office"); 
//	  roleDao.save(bo);
//	  
//	  Role jsk=new Role(); 
//	  jsk.setRoleName("JSK");
//	  jsk.setRoleDescription("Jan Suvida Kendra"); 
//	  roleDao.save(jsk);
//	  
//	  Role acc=new Role(); 
//	  acc.setRoleName("ACC");
//	  acc.setRoleDescription("Accounts"); 
//	  roleDao.save(acc);
//	   
//	   Role userrole=new Role(); 
//	   userrole.setRoleName("USER");
//	   userrole.setRoleDescription("User"); 
//		  roleDao.save(userrole);
//
//	  
//		//super Admin Data
//	  User superAdmin =new User(); 
//	  superAdmin.setId(1L);
//	  superAdmin.setUserName("sa");
//	  superAdmin.setUserPassword(getEncodedPassword("sa"));
//	  Set<Role>	  rolessuperAdmin=new HashSet<>(); 
//	  rolessuperAdmin.add(superAdmin_ROle);
//	  superAdmin.setRole(rolessuperAdmin); 
//	  superAdmin.setUser_id(111L);
//	  userDao.save(superAdmin);
//	  
//	  
////	  // Admin Data
//	  User admin =new User();
//	  admin.setId(1L);
//	  admin.setUserName("admin");
//	  admin.setUserPassword(getEncodedPassword("admin12345"));
//	  Set<Role>	  rolesAdmin=new HashSet<>(); 
//	  rolesAdmin.add(adminRole);
//	  admin.setRole(rolesAdmin); 
//	  admin.setUser_id(222L);
//	  userDao.save(admin);
//	  
//	  // Project Manager
//	  User projectManager =new User(); 
//	  projectManager.setId(3L);
//	  projectManager.setUserName("pm");
//	  projectManager.setUserPassword(getEncodedPassword("pm12345"));
//	  Set<Role>	  roleProjectManager=new HashSet<>(); 
//	  roleProjectManager.add(pm);
//	  projectManager.setRole(roleProjectManager); 
//	  projectManager.setUser_id(333L);
//	  userDao.save(projectManager);
//	  
//	  
////	  // Assitant Project Manager
//	  User assitantProjectManager =new User();
//	  assitantProjectManager.setId(4L);
//	  assitantProjectManager.setUserName("apm");
//	  assitantProjectManager.setUserPassword(getEncodedPassword("apm12345"));
//	  Set<Role>	  rolesAssistantProjectManager=new HashSet<>(); 
//	  rolesAssistantProjectManager.add(apm);
//	  assitantProjectManager.setRole(rolesAdmin); 
//	  assitantProjectManager.setUser_id(444L);
//	  userDao.save(assitantProjectManager);
//	  
//	  
	  // Assitant Project Manager
//		  User user =new User();
//		  user.setId(5L);
//		  user.setUserName("spslogin");
//		  user.setUserPassword(getEncodedPassword("sps12345"));
//		  Set<Role>	  rolessps=new HashSet<>(); 
//		  rolessps.add(userrole);
//		  user.setRole(rolessps); 
//		  user.setUser_id(555L);
//		  userDao.save(user);
		  
//	  
//	  	  
	 // }
	  
	 
    
    
    
    
    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    @Transactional
    public Integer changePassword(String password, Long id) {
        return userDao.changePassword(getEncodedPassword(password),id);
    }

    @Override
    public List<Object[]> getAllUser() {
        String jpql="select a.id,a.user_id,UPPER(b.name) as employee_name,a.user_name,c.description as designation,b.is_active from users a left join tbl_user_details b on b.user_id=a.user_id left join tbl_user_designation c on \n" +
                "UPPER(c.designation)=b.user_type where b.is_active='TRUE' order by employee_name";
        Query query = entityManager.createNativeQuery(jpql);
        log.info("query................"+jpql);
        List<Object[]> results = query.getResultList();
        return results;
    }

    @Override
    public String deactivateUser(String userId) {
        userDetailsDao.deactivateUser(Long.valueOf(userId));
        return "Deactivation Successfull";
    }

	@Override
	public User newUserRegister(UserRequest newUserRequest) {
		
		String creatorRole = newUserRequest.getCreatorRole();
		String userRoleId = newUserRequest.getUserRoleId();
		log.info("Creator Role Value ------ Line 193 ---- {}",creatorRole);
		log.info("User Role Value ------ Line 193 ---- {}",userRoleId);
//		Long sa= 100L;
//		Long admin=50L;
//		Long tl =20L;
//		Long tc = 10L;
		
		long creatorRoleValue = getRoleValue(creatorRole);
		long userRoleValue = getRoleValue(userRoleId);
		log.info("CreatorRole Value ----- Line 202------ {},------ UserRole Value ------   {}",creatorRoleValue,userRoleValue);
		if (creatorRoleValue <= userRoleValue) {
			
			log.info("userRole Id ---- {}",userRoleId);
			throw new RecordNotFoundException(messageSource.getMessage("Not Authorize to Add User", new Object[] {}, LocaleContextHolder.getLocale()));
		   
		} 
		
		
		log.info("newUserRequest {}",newUserRequest);
				if(null==newUserRequest.getDesignation() || null==newUserRequest.getUserName() || null==newUserRequest.getEmail() || null==newUserRequest.getUserPassword()|| null==newUserRequest.getEmployee_of()|| null==newUserRequest.getPhone()        
				
				) {
		         throw new RecordNotFoundException(messageSource.getMessage("no.record.found",
		                    new Object[] {}, LocaleContextHolder.getLocale()));
		        }
				if(creatorRole.equals(userRoleId)  ) {
					throw new RecordNotFoundException(messageSource.getMessage("Record Found", new Object[] {}, LocaleContextHolder.getLocale()));
				}
				
				
	
				String name = newUserRequest.getUserName();
		        Role role=roleDao.findRoleName(newUserRequest.getUserRoleId()).orElseThrow(()->
		                new RecordNotFoundException(messageSource.getMessage("role.not.found",
		                new Object[] {newUserRequest.getUserRoleId()}, LocaleContextHolder.getLocale())));

		        String designationShortName= String.valueOf(designationDao.findShortNameByDesignation(newUserRequest.getDesignation().toUpperCase()));

		        if (null == designationShortName || designationShortName.isEmpty()){
		        throw new RecordNotFoundException(messageSource.getMessage("no.record.found",
		                new Object[] {}, LocaleContextHolder.getLocale()));
		        }

	        Set<Role> roles=new HashSet<>();
		        roles.add(role);
	        Long newUserId=userDao.newUserID();
		        log.info("New user_id {} ,designationShortName : {}",newUserId,designationShortName);

		        final String newUserName =Generate.generateUserName(prefix,symbol1, newUserRequest.getUserName().toLowerCase().replaceAll("\\s", ""),
		                symbol2,designationShortName, String.valueOf(newUserId));

		        log.info("New user_name {} ",newUserName);
		        
		        User newuser = new User();
		        newuser.setUserName(newUserName);;

		        newuser.setRole(roles);
		        newuser.setUser_id(newUserId);
		        newuser.setUserRoleId(designationShortName);
		        newuser.setEmail(newUserRequest.getEmail());
		        newuser.setEmployee_of(newUserRequest.getEmployee_of());
		        newuser.setUserPassword(getEncodedPassword(newUserRequest.getUserPassword()));
		        newuser.setPhone(newUserRequest.getPhone());
		        newuser.setEmployee_photo(newUserRequest.getEmployee_photo());
		        newuser.setName(name);
		        
		        log.info("newUser {} "+ newuser);
		        
		        UserDetails newUserDetails = new UserDetails();
		        newUserDetails.setUser_id(newUserId);
		    
		        newUserDetails.setUser_type(newUserRequest.getDesignation());
		        newUserDetails.setName(newUserRequest.getUserName());
		        newUserDetails.setUser_name(newUserRequest.getUserName());
		        newUserDetails.setRole_name(newUserRequest.getDesignation());
		        newUserDetails.setEmail(newUserRequest.getEmail());
		        newUserDetails.setEmployee_of(newUserRequest.getEmployee_of());
		        newUserDetails.setPhone(newUserRequest.getPhone());
		        newUserDetails.setEmployee_photo(newUserRequest.getEmployee_photo());
		      
		        newUserDetails.setIs_active(Status.TRUE);
		        log.info("newUser {} "+ newUserDetails);
		        
		       userDetailsDao.save(newUserDetails);
		       
		      
		       		return userDao.save(newuser);
	}
	
	
	
	private long getRoleValue(String role) {
	    switch (role) {
	        case "SUPERADMIN":
	            return 100L;
	        case "ADMIN":
	            return 50L;
	        case "PM":
	            return 20L;
	        case "APM":
	            return 10L;
	        default:
	            return 0L; // Default value for unknown roles
	    }
	
	}
	
}
