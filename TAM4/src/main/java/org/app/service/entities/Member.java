package org.app.service.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="member")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
public class Member implements Comparable<Member>, Serializable {
    @Id @GeneratedValue
	private Integer idMembru;
	private String numeMembru;
	private String Email;
	private String Rol;
	
	@ManyToOne
	private Team team;
	
	@XmlElement
	public Integer getIdMembru() {
		return idMembru;
	}
	public void setIdMembru(Integer idMembru) {
		this.idMembru = idMembru;
	}
	
	@XmlElement
	public String getNumeMembru() {
		return numeMembru;
	}
	public void setNumeMembru(String numeMembru) {
		this.numeMembru = numeMembru;
	}
	
	@XmlElement
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	
	@XmlElement
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
	@Override
	public int compareTo(Member arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/* Rest Resource URL*/
//	public static String BASE_URL = "http://localhost:8080/TAM4/resources/teams/";
	
	public static String BASE_URL = Team.BASE_URL;
    @XmlElement(name = "link")
    public AtomLink getLink() throws Exception {
		String restUrl = BASE_URL 
				+ ((this.getTeam() != null) ? ((Team) this.getTeam()).getIdEchipa() : "")
				+ "/members/" 
				+ this.getIdMembru();
        return new AtomLink(restUrl, "get-member");
    }	
	private Object getTeam() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setLink(AtomLink link){}

	/* DTO Logic*/
	/*public Member toDTO(){
		return new Member idMembru, numeMembru, Email, Rol, team.toDTO());
	}
	
	public static List<Member> toDTOList(List<Member> members){
		List<Member> memberDTOList = new ArrayList<>();
		for(Member m: members){
			memberDTOList.add(m.toDTO());
		}
		return memberDTOList;
	}	*/
}
