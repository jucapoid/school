package com.xhamster.models;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Column;

@Entity
public class Utilizador {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long u_id;
	
	@Column(name= "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	private String role;

	@OneToMany(mappedBy = "utilizador")
	private List<Encomenda> encomenda;

	protected Utilizador() {}

	public Utilizador(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = "USER";
	}

	@Override
	public String toString() {
		return String.format(
				"Utilizador[u_id=%d, username='%s', password='%s', email='%s', role='%s']",
				u_id, username, password, email, role);
	}

	public Long getId() {
		return u_id;
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	public String getEmail() {
		return email;
	}
}
