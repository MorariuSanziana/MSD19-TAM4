package org.app.service.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.persistence.FetchType.EAGER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@XmlRootElement(name="student")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class Student implements Comparable<Student>, Serializable{
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

	
	@XmlElement
	public Integer getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(Integer idStudent) {
		this.idStudent = idStudent;
	}

	
	@XmlElement
	public String getNumeStudent() {
		return numeStudent;
	}

	public void setNumeStudent(String numeStudent) {
		this.numeStudent = numeStudent;
	}

	
	@XmlElement
	public Integer getCNP() {
		return CNP;
	}

	public void setCNP(Integer cNP) {
		CNP = cNP;
	}
	
	
	@XmlElement
	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	
	@XmlElement
	public Integer getNrTelefon() {
		return nrTelefon;
	}

	public void setNrTelefon(Integer nrTelefon) {
		this.nrTelefon = nrTelefon;
	}

	
	@XmlElement
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

	public Student(Object idStudent2, String numeStudent2, Date date, Date date2, int nrTelefon2,
			Internship internship2) {
		// TODO Auto-generated constructor stub
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

	
//	@XmlElement
	public Internship getInternship() {
		return internship;
	}

	public void setInternship(Internship internship) {
		this.internship = internship;
	}

	
//	@XmlElement
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	
//	@XmlElement
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	
//	@XmlElement
	public String getIndicative() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setIndicative(String string) {
		// TODO Auto-generated method stub
		
	}

	public static List<Student> toDTOList1(List<Student> students) {
		// TODO Auto-generated method stub
		return null;
	}

	/* Rest Resource URL*/
//	public static String BASE_URL = "http://localhost:8080/ScrumREST/internships/";
	
	public static String BASE_URL = Internship.BASE_URL;
	@XmlElement(name = "link")
    public AtomLink getLink() throws Exception {
		String restUrl = BASE_URL 
				+ ((this.getInternship() != null) ? this.getInternship().getIdInternship() : "")
				+ "/students/" 
				+ this.getIdStudent();
        return new AtomLink(restUrl, "get-student");
    }	
	public void setLink(AtomLink link){}

	@Override
	public int compareTo(Student arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static List<Student> toDTOList(List<Student> students) {
		// TODO Auto-generated method stub
		return null;
	}

	/* DTO Logic*/
	/*public Student toDTO(){
		return new Student( idStudent, numeStudent, CNP, Email,  nrTelefon, Adresa,  internship.toDTO(), team.toDTO(), project.toDTO());
	}
	
	public static List<Student> toDTOList(List<Student> students){
		List<Student> studentDTOList = new ArrayList<>();
		for(Student s: students){
			studentDTOList.add(s.toDTO());
		}
		return studentDTOList;
	}	*/

}
