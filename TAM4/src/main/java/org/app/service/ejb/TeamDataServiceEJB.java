package org.app.service.ejb;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.app.service.entities.Internship;
import org.app.service.entities.Member;
import org.app.service.entities.Project;
import org.app.service.entities.Team;
/*
 * SCRUM: 		src/main/webapp/WEB-INF/jboss-web.xml
 * data	: 		org.app.service.rest.ApplicationConfig
 * teams: 	@Path
 */
@Path("teams") /* http://localhost:8080/TAM4/resources/teams */
@Stateless @LocalBean
public class TeamDataServiceEJB implements TeamDataService {
	private static Logger logger = Logger.getLogger(TeamDataServiceEJB.class.getName());
	
	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public TeamDataServiceEJB() {
	}
	// Init after constructor
	@PostConstruct
	public void init(){
		logger.info("POSTCONSTRUCT-INIT : " + this.em);
	}	
	
	/* CRUD operations implementation */
	// CREATE or UPDATE
	@PUT @Path("/{id}") 	/* SCRUM/data/teams/{id} 	REST-resource: Team-entity */	
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
	public Team addTeam(Team teamToAdd){
		em.persist(teamToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(teamToAdd);
		return teamToAdd;
	}
	
	// READ
	@GET @Path("/{ID}")		/* SCRUM/data/teams 		REST-resource: Team-entity */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
		@Override
		public Team getTeamByID(@PathParam("ID")Integer idEchipa) {
		logger.info("**** DEBUG REST getTeamByID(): id = " + idEchipa);
			return em.find(Team.class, idEchipa);
		}
	@GET 					/* SCRUM/data/teams 		REST-resource: Teams-collection */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
		public Collection<Team> getTeams(){
			List<Team> teams = em.createQuery("SELECT t FROM Team t  JOIN FETCH t.membri m ", Team.class)
					.getResultList();
			logger.info("**** DEBUG REST teams.size()= " + teams.size());
			return teams;
		}
	@POST 					/* MSD-S4/data/projects 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	public Collection<Team> addIntoCollection(Team team) {
		// save aggregate
		this.addTeam(team);
		logger.info("**** DEBUG REST save aggregate POST");
		// return updated collection
		return this.toCollection();
	}
	
	
	@POST @Path("/new/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	// Aggregate factory method
	public Team createNewTeam(@PathParam("id")Integer id){
		logger.info("**** DEBUG REST createNewProject POST");
		// create project aggregate
		Team team = new Team(null, "Echipa1", 4, null, null);
		List<Member> membersTeam = new ArrayList<>();
		Integer memberCount = 3;
		for (int i=0; i<=memberCount-1; i++){
			membersTeam.add(new Member(null, "Popescu Constantin"  + (100+i), "popescu.constantin@gmail.com" + (100+i), "Analyst"));
		}
		team.setMembri(membersTeam);		
		// save project aggregate
		this.addTeam(team);
		// return project aggregate to service client
		return team;
	}
	
		
	// REMOVE
	@DELETE 					/* SCRUM/data/teams		REST-resource: Team-entity */
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
		public String removeTeam(Team teamToDelete){
					teamToDelete = em.merge(teamToDelete);
					em.remove(teamToDelete);
					em.flush();
					return "True";
				}
	// Custom READ: custom query
	@GET @Path("/{nume}")		/* SCRUM/data/teams 		REST-resource: Team-entity */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
		public Team getTeamByNume(@PathParam("nume")String numeEchipa) {
				return em.createQuery("SELECT t FROM Team t WHERE t.Nume = :Nume", Team.class)
							.setParameter("Nume", numeEchipa)
							.getSingleResult();
				}	
	@GET @Path("/projectdata")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getTeamData() throws Exception{
		Team dto = new Team(null, "Echipa1", 4, null, null);
		JAXBContext jaxbContext = JAXBContext.newInstance(Team.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		marshaller.marshal(dto, os);
		String aString = new String(os.toByteArray(),"UTF-8");
		
		Response response = Response
				.status(Status.OK)
				.type(MediaType.TEXT_PLAIN)
				.entity(aString)
				.build();
		
		return response;
	}
	// Others
	@Override
		public String getMessage() {
			return "TeamServiceEJB is ON... ";
				}
	@Override
	public Collection<Team> toCollection() {
		// TODO Auto-generated method stub
		return null;
	}


}
