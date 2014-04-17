package entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the passwords database table.
 * 
 */
@Entity
@Table(name="passwords")
@NamedQuery(name="Password.findAll", query="SELECT p FROM Password p")
public class Password implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	private String password;

	public Password() {
	}
	
	public Password(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}