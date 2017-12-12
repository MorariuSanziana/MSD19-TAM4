package org.app.service.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Team implements Serializable{
	@Id @GeneratedValue
	 private Integer idEchipa;
	 private String numeEchipa;
	 private Integer nrMembri;
	 @OneToMany (mappedBy="team")
	 private List<Student> studenti = new ArrayList<Student>();
	 @OneToMany (mappedBy= "team")
	 private List<Member> membri = new ArrayList<Member>();
	 @ManyToOne
		private Project project;
	public Integer getIdEchipa() {
		return idEchipa;
	}
	public void setIdEchipa(Integer idEchipa) {
		this.idEchipa = idEchipa;
	}
	public String getNumeEchipa() {
		return numeEchipa;
	}
	public void setNumeEchipa(String numeEchipa) {
		this.numeEchipa = numeEchipa;
	}
	public Integer getNrMembri() {
		return nrMembri;
	}
	public void setNrMembri(Integer nrMembri) {
		this.nrMembri = nrMembri;
	}
	public List<Student> getStudenti() {
		return studenti;
	}
	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}
	public List<Member> getMembri() {
		return membri;
	}
	public void setMembri(List<Member> membri) {
		this.membri = membri;
	}
	public Team(Integer idEchipa, String numeEchipa, Integer nrMembri, List<Student> studenti, List<Member> membri) {
		super();
		this.idEchipa = idEchipa;
		this.numeEchipa = numeEchipa;
		this.nrMembri = nrMembri;
		this.studenti = studenti;
		this.membri = membri;
	}
	public Team() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEchipa == null) ? 0 : idEchipa.hashCode());
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
		Team other = (Team) obj;
		if (idEchipa == null) {
			if (other.idEchipa != null)
				return false;
		} else if (!idEchipa.equals(other.idEchipa))
			return false;
		return true;
	}
	 
}
