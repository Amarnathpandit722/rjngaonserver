package gov.municipal.suda.usermanagement.service;

import gov.municipal.suda.usermanagement.model.NewUser;
import gov.municipal.suda.usermanagement.model.User;
import gov.municipal.suda.usermanagement.model.UserRequest;

import java.util.List;


public interface UserService {


	//public User registerNewUser(UserRequest userRequest) ;

	//public void initRolesAndUser();

	public String getEncodedPassword(String password);

	public Integer changePassword(String password, Long id);

	List<Object[]> getAllUser();

    String deactivateUser(String userId);
    
    public User newUserRegister(UserRequest newUserRequest) ;
    
    
    
}