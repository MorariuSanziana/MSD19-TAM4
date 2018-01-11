package org.app.service.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.Basic;
import static javax.persistence.FetchType.EAGER;


@XmlRootElement(name="internship")
@XmlAccessorType(XmlAccessType.NONE)
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
	
	@XmlElement
	public Integer getIdInternship() {
		return IdInternship;
	}
	public void setIdInternship(Integer idInternship) {
		IdInternship = idInternship;
	}
	
	@XmlElement
	public String getDenumireInternship() {
		return denumireInternship;
	}
	public void setDenumireInternship(String denumireInternship) {
		this.denumireInternship = denumireInternship;
	}
	
	@XmlElement
	public Date getDataIncepere() {
		return dataIncepere;
	}
	public void setDataIncepere(Date dataIncepere) {
		this.dataIncepere = dataIncepere;
	}
	
	@XmlElement
	public Date getDataFinalizare() {
		return dataFinalizare;
	}
	public void setDataFinalizare(Date dataFinalizare) {
		this.dataFinalizare = dataFinalizare;
	}
	
	@XmlElement
	public Integer getNrLocuri() {
		return nrLocuri;
	}
	public void setNrLocuri(Integer nrLocuri) {
		this.nrLocuri = nrLocuri;
	}
	
	@XmlElementWrapper(name = "studenti")
    @XmlElement(name = "student")
	public List<Student> getStudenti() {
		return studenti;
	}
	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}
	

	/* Rest Resource URL*/
	public static String BASE_URL = "http://localhost:8080/TAM4/resources/internships/";
	@XmlElement(name = "link")
    public AtomLink getLink() throws Exception {
		String restUrl = BASE_URL + this.getIdInternship();
        return new AtomLink(restUrl, "get-internship");
    }	
	
	public void setLink(AtomLink link){}
	
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
	
	
	/* DTO Logic*/
	public Internship toDTO(){
		Internship internshipDTO =new Internship(IdInternship, denumireInternship, dataIncepere, dataFinalizare, nrLocuri, null); 
	internshipDTO.setStudenti(null);
		return internshipDTO;
	}
	
	public static Internship toDTOAggregate(Internship internship){
		if (internship == null)
			return null;
		Internship internshipDTO = internship.toDTO();
		List<Student> studentsDTO = Student.toDTOList(internship.getStudents());
		internshipDTO.setStudenti(studentsDTO);
		
		return internshipDTO;
	}
	
	
		
	
	public static Internship[] toDTOList(Collection<Internship> internships){
		List<Internship> internshipDTOList = new ArrayList<>();
		for(Internship i: internships){
			internshipDTOList.add(i.toDTO());
		}
		return internshipDTOList.toArray(new Internship[0]);
	}
	public static Collection<Internship> toDTOs(Collection<Internship> internships){
		List<Internship> internshipDTOList = new ArrayList<>();
		for(Internship i: internships){
			internshipDTOList.add(i.toDTO());
		}
		//return internshipDTOList.toArray(new Internship[0]);
		return internshipDTOList;
	}	
	
}
