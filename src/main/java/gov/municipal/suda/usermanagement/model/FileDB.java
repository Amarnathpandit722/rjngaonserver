package gov.municipal.suda.usermanagement.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString

@Entity
@Table(name = "filedb")
public class FileDB {
	@Id
	  @GeneratedValue(generator = "uuid")
	  @GenericGenerator(name = "uuid", strategy = "uuid2")
	  private String id;

	  private String name;

	  private String type;

	  @Lob
	  private byte[] data;

	  public FileDB() {
	  }

	  public FileDB(String name, String type, byte[] data) {
	    this.name = name;
	    this.type = type;
	    this.data = data;
	  }


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		FileDB fileDB = (FileDB) o;
		return id != null && Objects.equals(id, fileDB.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
