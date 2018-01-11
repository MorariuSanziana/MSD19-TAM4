package org.app.service.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="team")
@XmlAccessorType(XmlAccessType.NONE)
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
	
	 
	 @XmlElement
	 public Integer getIdEchipa() {
		return idEchipa;
	}
	public void setIdEchipa(Integer idEchipa) {
		this.idEchipa = idEchipa;
	}
	
	@XmlElement
	public String getNumeEchipa() {
		return numeEchipa;
	}
	public void setNumeEchipa(String numeEchipa) {
		this.numeEchipa = numeEchipa;
	}
	
	@XmlElement
	public Integer getNrMembri() {
		return nrMembri;
	}
	public void setNrMembri(Integer nrMembri) {
		this.nrMembri = nrMembri;
	}
	
	@XmlElementWrapper(name = "studenti")
    @XmlElement(name = "student")
	public List<Student> getStudenti() {
		return studenti;
	}
	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}
	
	@XmlElementWrapper(name = "membri")
    @XmlElement(name = "member")
	public List<Member> getMembri() {
		return membri;
	}
	public void setMembri(List<Member> membri) {
		this.membri = membri;
	}
	

	
	/* Rest Resource URL*/
	public static String BASE_URL = "http://localhost:8080/TAM4/resources/teams/";
	@XmlElement(name = "link")
    public AtomLink getLink() throws Exception {
		String restUrl = BASE_URL + this.getIdEchipa();
        return new AtomLink(restUrl, "get-team");
    }	
	
	public void setLink(AtomLink link){}
	
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
	 
	
	/* DTO Logic*/
/*	public Team toDTO(){
		Team teamDTO =new Team( idEchipa, numeEchipa,  nrMembri, null, null); 
		//teamDTO.setStudenti(null);
		//teamDTO.setMembri(null);

		return teamDTO;
	}
	
	public static Team toDTOAggregate(Team team){
		if (team == null)
			return null;
		Team teamDTO = team.toDTO();
		List<Student> studentsDTO = Student.toDTOList(team.getStudenti());
		teamDTO.setStudenti(studentsDTO);
		
		return teamDTO;
	}
	
	public static Team toDTOAggregate1(Team team){
		if (team == null)
			return null;
		Team teamDTO = team.toDTO();
		List<Member> membersDTO = Member.toDTOList(team.getMembri());
		teamDTO.setMembri(membersDTO);
		
		return teamDTO;
	}
	
	public static Team[] toDTOList(Collection<Team> teams){
		List<Team> teamDTOList = new ArrayList<>();
		for(Team t: teams){
			teamDTOList.add(t.toDTO());
		}
		return teamDTOList.toArray(new Team[0]);
	}	
	
	public static Collection<Team> toDTOs(Collection<Team> teams){
		List<Team> teamDTOList = new ArrayList<>();
		for(Team t: teams){
			teamDTOList.add(t.toDTO());
		}
		//return teamDTOList.toArray(new Team[0]);
		return teamDTOList;
	}	*/
	
}
