package org.app.service.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;

@Entity
public class Student implements Serializable{
	@Id
	@GeneratedValue
	private Integer idStudent;
	private String numeStudent;
	private Integer CNP;
	private String Email;
	private Integer nrTelefon;
	private String Adresa;
	@ManyToOne(fetch = EAGER)
	private Internship internship;
	@ManyToOne(fetch = EAGER)
	private Team team;
	@ManyToOne(fetch = EAGER)
	private Project project;

	public Integer getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(Integer idStudent) {
		this.idStudent = idStudent;
	}

	public String getNumeStudent() {
		return numeStudent;
	}

	public void setNumeStudent(String numeStudent) {
		this.numeStudent = numeStudent;
	}

	public Integer getCNP() {
		return CNP;
	}

	public void setCNP(Integer cNP) {
		CNP = cNP;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public Integer getNrTelefon() {
		return nrTelefon;
	}

	public void setNrTelefon(Integer nrTelefon) {
		this.nrTelefon = nrTelefon;
	}

	public String getAdresa() {
		return Adresa;
	}

	public void setAdresa(String adresa) {
		Adresa = adresa;
	}

	public Student(Integer idStudent, String numeStudent, Integer cNP, String email, Integer nrTelefon, String adresa) {
		super();
		this.idStudent = idStudent;
		this.numeStudent = numeStudent;
		CNP = cNP;
		Email = email;
		this.nrTelefon = nrTelefon;
		Adresa = adresa;
	}

	public Student() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idStudent == null) ? 0 : idStudent.hashCode());
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
		Student other = (Student) obj;
		if (idStudent == null) {
			if (other.idStudent != null)
				return false;
		} else if (!idStudent.equals(other.idStudent))
			return false;
		return true;
	}

	public Internship getInternship() {
		return internship;
	}

	public void setInternship(Internship internship) {
		this.internship = internship;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getIndicative() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setIndicative(String string) {
		// TODO Auto-generated method stub
		
	}

	

}
