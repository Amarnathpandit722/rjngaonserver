package gov.municipal.suda.usermanagement.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import gov.municipal.suda.util.enumtype.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_new_user_details")

public class NewUserDetails implements Serializable{

		@Id
	    private Long user_id;
		//private String name;
		private String email;
		private String password;
		private String phone;
		private String employee_of;
	//	private String designation;
		 private String role_name; // role_id from role table
		private String employee_photo;
		 @Enumerated(EnumType.STRING)
		    private Status is_active;
		 private String user_name;
		    private String user_type; //designation  name
}

//@Id
//private Long user_id;
//private String user_name;
//private String user_type; //designation  name
//private String name;
//private String role_name; // role_id from role table
//@Enumerated(EnumType.STRING)
//private Status is_active;