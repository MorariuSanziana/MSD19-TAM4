package org.app.service.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Member implements Serializable {
    @Id @GeneratedValue
	private Integer idMembru;
	private String numeMembru;
	private String Email;
	private String Rol;
	
	@ManyToOne
	private Team team;
	public Integer getIdMembru() {
		return idMembru;
	}
	public void setIdMembru(Integer idMembru) {
		this.idMembru = idMembru;
	}
	public String getNumeMembru() {
		return numeMembru;
	}
	public void setNumeMembru(String numeMembru) {
		this.numeMembru = numeMembru;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getRol() {
		return Rol;
	}
	public void setRol(String rol) {
		Rol = rol;
	}
	public Member(Integer idMembru, String numeMembru, String email, String rol) {
		super();
		this.idMembru = idMembru;
		this.numeMembru = numeMembru;
		Email = email;
		Rol = rol;
	}
	public Member() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMembru == null) ? 0 : idMembru.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (idMembru == null) {
			if (other.idMembru != null)
				return false;
		} else if (!idMembru.equals(other.idMembru))
			return false;
		return true;
	}
	
	
}
