package org.app.service.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
public class Project implements Serializable {
    private static final Date Date = null;
	@Id @GeneratedValue
	private Integer idProiect;
	private String numeProiect;
	@Temporal(TemporalType.DATE)
	private Date dataIncepere;
	public Integer getIdProiect() {
		return idProiect;
	}
	public void setIdProiect(Integer idProiect) {
		this.idProiect = idProiect;
	}
	public String getNumeProiect() {
		return numeProiect;
	}
	public void setNumeProiect(String numeProiect) {
		this.numeProiect = numeProiect;
	}
	public 	Date getDataIncepere() {
		return dataIncepere;
	}
	public void setDataIncepere(Date dataIncepere) {
		this.dataIncepere = dataIncepere;
	}
	public Project(Integer idProiect, String numeProiect, Date date2) {
		super();
		//this.idProiect = idProiect;
		this.numeProiect = numeProiect;
		this.dataIncepere = Date;
	}
	public Project() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idProiect == null) ? 0 : idProiect.hashCode());
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
		Project other = (Project) obj;
		if (idProiect == null) {
			if (other.idProiect != null)
				return false;
		} else if (!idProiect.equals(other.idProiect))
			return false;
		return true;
	}
	
}
