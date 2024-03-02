package gov.municipal.suda.usermanagement.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {
	private String creatorUserName;
	private String creatorRole;
    private String userName;
    private String userPassword;
    private String userRoleId;
    private String designation;
    
    	private String email;
		private String phone;
		private String employee_of;
	    private String employee_photo;
    
}
