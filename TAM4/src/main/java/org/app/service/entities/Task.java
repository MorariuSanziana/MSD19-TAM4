package org.app.service.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="task")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class Task implements Serializable{
    
	@Id @GeneratedValue
	private Integer idTask;
	private String denumireTask;
	private String Categorie;
	private String Status;
	@Temporal(TemporalType.DATE)
	private Date dataIncepere;
	private Integer estimareDurata;
	private Integer timpRamas;
	private String descriereTask;
	@ManyToOne
	private Student student;
	@ManyToOne
	private Member member;
	
	
	@XmlElement
	public Integer getIdTask() {
		return idTask;
	}
	
	public void setIdTask(Integer idTask) {
		this.idTask = idTask;
	}
	
	@XmlElement
	public String getDenumireTask() {
		return denumireTask;
	}
	public void setDenumireTask(String denumireTask) {
		this.denumireTask = denumireTask;
	}
	
	@XmlElement
	public String getCategorie() {
		return Categorie;
	}
	public void setCategorie(String categorie) {
		Categorie = categorie;
	}
	
	@XmlElement
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
	@XmlElement
	public Date getDataIncepere() {
		return dataIncepere;
	}
	public void setDataIncepere(Date dataIncepere) {
		this.dataIncepere = dataIncepere;
	}
	
	@XmlElement
	public Integer getEstimareDurata() {
		return estimareDurata;
	}
	public void setEstimareDurata(Integer estimareDurata) {
		this.estimareDurata = estimareDurata;
	}
	
	@XmlElement
	public Integer getTimpRamas() {
		return timpRamas;
	}
	public void setTimpRamas(Integer timpRamas) {
		this.timpRamas = timpRamas;
	}
	
	@XmlElement
	public String getDescriereTask() {
		return descriereTask;
	}
	public void setDescriereTask(String descriereTask) {
		this.descriereTask = descriereTask;
	}

//	@XmlElement
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
//	@XmlElement
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	
	/* Rest Resource URL*/
	public static String BASE_URL = "http://localhost:8080/TAM4/resources/tasks/";
	@XmlElement(name = "link")
    public AtomLink getLink() throws Exception {
		String restUrl = BASE_URL + this.getIdTask();
        return new AtomLink(restUrl, "get-task");
    }	
	
	public void setLink(AtomLink link){}
	public Task(Integer idTask, String denumireTask, String categorie, String status, Date dataIncepere,
			Integer estimareDurata, Integer timpRamas, String descriereTask) {
		super();
		this.idTask = idTask;
		this.denumireTask = denumireTask;
		Categorie = categorie;
		Status = status;
		this.dataIncepere = dataIncepere;
		this.estimareDurata = estimareDurata;
		this.timpRamas = timpRamas;
		this.descriereTask = descriereTask;
	}
	public Task() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTask == null) ? 0 : idTask.hashCode());
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
		Task other = (Task) obj;
		if (idTask == null) {
			if (other.idTask != null)
				return false;
		} else if (!idTask.equals(other.idTask))
			return false;
		return true;
	}

	
}
