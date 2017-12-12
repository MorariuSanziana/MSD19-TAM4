package org.app.service.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Basic;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.TemporalType.TIME;
import static javax.persistence.TemporalType.DATE;

@Entity
public class Internship implements Serializable{

	private static final String mapped = null;

	private static final TemporalType DATE = null;
	
	@Id @GeneratedValue
	private Integer IdInternship;
	private String denumireInternship;
	@Basic(fetch = EAGER)
	@Temporal(TemporalType.DATE)
	private Date dataIncepere;	
	@Temporal(TemporalType.DATE)
	private Date dataFinalizare;
	public static String getMapped() {
		return mapped;
	}
	public static TemporalType getDate() {
		return DATE;
	}
	private Integer nrLocuri;
	@OneToMany (mappedBy ="internship")
	private List<Student> studenti = new ArrayList<Student>();
	public Integer getIdInternship() {
		return IdInternship;
	}
	public void setIdInternship(Integer idInternship) {
		IdInternship = idInternship;
	}
	public String getDenumireInternship() {
		return denumireInternship;
	}
	public void setDenumireInternship(String denumireInternship) {
		this.denumireInternship = denumireInternship;
	}
	public Date getDataIncepere() {
		return dataIncepere;
	}
	public void setDataIncepere(Date dataIncepere) {
		this.dataIncepere = dataIncepere;
	}
	public Date getDataFinalizare() {
		return dataFinalizare;
	}
	public void setDataFinalizare(Date dataFinalizare) {
		this.dataFinalizare = dataFinalizare;
	}
	public Integer getNrLocuri() {
		return nrLocuri;
	}
	public void setNrLocuri(Integer nrLocuri) {
		this.nrLocuri = nrLocuri;
	}
	public List<Student> getStudenti() {
		return studenti;
	}
	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}
	public Internship(Integer idInternship, String denumireInternship, Date dataIncepere, Date dataFinalizare,
			Integer nrLocuri, List<Student> studenti) {
		super();
		IdInternship = idInternship;
		this.denumireInternship = denumireInternship;
		this.dataIncepere = dataIncepere;
		this.dataFinalizare = dataFinalizare;
		this.nrLocuri = nrLocuri;
		this.studenti = studenti;
	}
	public Internship() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IdInternship == null) ? 0 : IdInternship.hashCode());
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
		Internship other = (Internship) obj;
		if (IdInternship == null) {
			if (other.IdInternship != null)
				return false;
		} else if (!IdInternship.equals(other.IdInternship))
			return false;
		return true;
	}
	public List<Student> getStudents() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
