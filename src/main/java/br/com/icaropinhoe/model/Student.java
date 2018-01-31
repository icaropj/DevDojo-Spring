package br.com.icaropinhoe.model;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Student extends AbstractEntity{
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	@Email
	private String email;

	public Student() {}
	public Student(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public Student(long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Student [name=");
		builder.append(name);
		builder.append(", email=");
		builder.append(email);
		builder.append("]");
		return builder.toString();
	}
	
}
