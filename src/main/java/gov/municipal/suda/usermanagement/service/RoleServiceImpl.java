package gov.municipal.suda.usermanagement.service;

import gov.municipal.suda.usermanagement.dao.RoleDao;
import gov.municipal.suda.usermanagement.dao.UserDao;
import gov.municipal.suda.usermanagement.model.Role;
import gov.municipal.suda.util.UtilHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;

    public Role createNewRole(Role role) {
        return roleDao.save(role);
    }

    public void updateUserRole(String userId, String roleId) {
        System.out.println("user_id = " + userId);
        System.out.println("role_id = " + roleId);
        UtilHelper utilHelper=new UtilHelper();
        utilHelper.updateUserRole(userId,roleId);

//		User user= userDao.findById(userId).get();
//		Role role=roleDao.findById(roleId).get();
//		user.updateRole(role);
//		userDao.save(user);
//		return "success";
    }
    
    
    
    
    
    
    
}
