package gov.municipal.suda.usermanagement.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "new_user")
public class NewUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private String userName;
	private String userPassword;
	private String email;
	private String phone;
	private String employee_of;
	private String designation;
	private String employee_photo;
	private Long user_id;
	@Transient
	private String userRoleId;
	
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinTable(name = "NEW_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID",insertable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	private Set<Role> role =new HashSet<>();
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewUser other = (NewUser) obj;
		return Objects.equals(userName, other.userName);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	public void updateRole(Role role){
		this.role.add(role);
	}
	
	
//	private Long id;
//	private String userName;
//	private String userPassword;
//	@Transient
//	private String userRoleId;
//	private Long user_id;
	
	
	
	
	
	
	
}
