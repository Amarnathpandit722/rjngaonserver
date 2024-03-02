package gov.municipal.suda.usermanagement.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String userName;
	private String userPassword;
	@Transient
	private String userRoleId;
	private Long user_id;
	private String name;
 		private String email;
		private String phone;
		private String employee_of;
	    private String employee_photo;
	
	
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID",insertable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	private Set<Role> role =new HashSet<>();


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		User user = (User) o;
		return userName != null && Objects.equals(userName, user.userName);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	public void updateRole(Role role){
		this.role.add(role);
	}
}
