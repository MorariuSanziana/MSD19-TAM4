package org.app.service.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="project")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class Project implements Serializable {
    private static final Date Date = null;
	@Id 
	//@GeneratedValue
	private Integer idProiect;
	private String numeProiect;
	@Temporal(TemporalType.DATE)
	private Date dataIncepere;
	
	@XmlElement
	public Integer getIdProiect() {
		return idProiect;
	}
	public void setIdProiect(Integer idProiect) {
		this.idProiect = idProiect;
	}
	
	@XmlElement
	public String getNumeProiect() {
		return numeProiect;
	}
	public void setNumeProiect(String numeProiect) {
		this.numeProiect = numeProiect;
	}
	
	@XmlElement
	public 	Date getDataIncepere() {
		return dataIncepere;
	}
	public void setDataIncepere(Date dataIncepere) {
		this.dataIncepere = dataIncepere;
	}
	
	
	/* Rest Resource URL*/
	public static String BASE_URL = "http://localhost:8080/TAM4/resources/projects/";
	@XmlElement(name = "link")
    public AtomLink getLink() throws Exception {
		String restUrl = BASE_URL + this.getIdProiect();
        return new AtomLink(restUrl, "get-project");
    }	
	
	public void setLink(AtomLink link){}
	
	public Project(Integer idProiect, String numeProiect, Date date2) {
		super();
		//this.idProiect = idProiect;
		this.numeProiect = numeProiect;
		this.dataIncepere = Date;
	}
	@Override
	public String toString() {
		return "Project [idProiect=" + idProiect + ", numeProiect=" + numeProiect + ", dataIncepere=" + dataIncepere + "]";
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
	
	
	/* DTO Logic*/
	/*public Project toDTO(){
		Project projectDTO =new Project(idProiect, numeProiect,dataIncepere); 
		return projectDTO;
	}
	
	public static Project toDTOAggregate(Project project){
		if (project == null)
			return null;
		Project projectDTO = project.toDTO();
		List<Student> studentsDTO = Student.toDTOList(project.getStudents());
		projectDTO.setStudents(studentsDTO);
		return projectDTO;
	}
	
	private void setStudents(List<Student> studentsDTO) {
		// TODO Auto-generated method stub
		
	}
	private List<Student> getStudents() {
		// TODO Auto-generated method stub
		return null;
	}
	public static Project[] toDTOList(Collection<Project> projects){
		List<Project> projectDTOList = new ArrayList<>();
		for(Project p: projects){
			projectDTOList.add(p.toDTO());
		}
		return projectDTOList.toArray(new Project[0]);
	}	
	
	public static Collection<Project> toDTOs(Collection<Project> projects){
		List<Project> projectDTOList = new ArrayList<>();
		for(Project p: projects){
			projectDTOList.add(p.toDTO());
		}
		//return projectDTOList.toArray(new Project[0]);
		return projectDTOList;
	}	*/
}
