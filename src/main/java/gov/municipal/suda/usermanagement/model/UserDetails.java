package gov.municipal.suda.usermanagement.model;

import gov.municipal.suda.util.enumtype.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_user_details")
public class UserDetails implements Serializable {

    @Id
    private Long user_id;
    private String user_name;
    private String user_type; //designation  name
    private String name;
    private String role_name; // role_id from role table
    @Enumerated(EnumType.STRING)
    private Status is_active;
    
    private String email;
    private String employee_photo;
	private String phone;
	private String employee_of;
    
    
    
}
