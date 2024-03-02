package gov.municipal.suda.usermanagement.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Role {

	@Id
	private String roleName;
	private String roleDescription;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Role role = (Role) o;
		return roleName != null && Objects.equals(roleName, role.roleName);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
