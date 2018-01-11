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

import org.app.service.entities.Member;
import org.app.service.entities.Project;


/*
 * SCRUM: 		src/main/webapp/WEB-INF/jboss-web.xml
 * data	: 		org.app.service.rest.ApplicationConfig
 * projects: 	@Path
 */
@Path("projects") /* http://localhost:8080/TAM4/resources/projects */
@Stateless @LocalBean
public class ProjectDataServiceEJB implements ProjectDataService {
	
private static Logger logger = Logger.getLogger(ProjectDataServiceEJB.class.getName());
	
	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public ProjectDataServiceEJB() {
	}
	// Init after constructor
	@PostConstruct
	public void init(){
		logger.info("POSTCONSTRUCT-INIT : " + this.em);
	}		
	
	/* CRUD operations implementation */
	// CREATE or UPDATE
	@PUT @Path("/{ID}") 	/* SCRUM/data/members/{ID} 	REST-resource: Member-entity */	
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
	public Project addProject(Project projectToAdd){
		em.persist(projectToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(projectToAdd);
		return projectToAdd;
	}
	
	// READ
	@GET @Path("/{ID}")		/* SCRUM/data/members 		REST-resource: Member-entity */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
		public Project getProjectByID(@PathParam("ID")Integer idProiect) {
		logger.info("**** DEBUG REST getProjectByID(): id = " + idProiect);
		return em.find(Project.class, idProiect);
		}	
		@Override
		@GET 					/* SCRUM/data/members 		REST-resource: Members-collection */
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
		public Collection<Project> getProjects(){
			List<Project> projects = em.createQuery("SELECT p FROM Project p", Project.class)
					.getResultList();
			logger.info("**** DEBUG REST projects.size()= " + projects.size());
			return projects;
		}
		@POST 					/* MSD-S4/data/projects 		REST-resource: projects-collection*/
		@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
		public Collection<Project> addIntoCollection(Project project) {
			// save aggregate
			this.addProject(project);
			logger.info("**** DEBUG REST save aggregate POST");
			// return updated collection
			return this.toCollection();
		}
			
		@POST @Path("/new/{id}")
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
		@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
		// Aggregate factory method
		public Project createNewProject(@PathParam("id")Integer id){
			logger.info("**** DEBUG REST createNewProject POST");
			// create project aggregate
			Project project = new Project(1, "Dezvoltare Aplicatii Multistrat", new Date());
			this.addProject(project);
			return project;
		}
		
					
			
		// REMOVE
		@Override
		@DELETE 					/* SCRUM/data/members		REST-resource: Member-entity */
		@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
				public String removeProject(Project projectToDelete){
			projectToDelete = em.merge(projectToDelete);
					em.remove(projectToDelete);
					em.flush();
					return "True";
				}

		@DELETE @Path("/{id}") 	/* MSD-S4/data/projects/{id} 	REST-resource: project-entity*/	
		@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
		public void remove(@PathParam("id")Integer id) {
			logger.info("DEBUG: called REMOVE - ById() for projects >>>>>>>>>>>>>> simplified ! + id");
			Project project = this.getProjectByID(id);
			this.removeProject(project);
		}
		
		@Override
		@DELETE 				/* MSD-S4/data/projects 		REST-resource: projects-collection*/
		@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
		public Collection<Project> removeFromCollection(Project project) {
			logger.info("DEBUG: called REMOVE - project: " + project);
			this.removeProject(project);
			return null;
		}
				// Custom READ: custom query
				@Override
				@GET @Path("/{Nume}")		/* SCRUM/data/members 		REST-resource: Member-entity */
				@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })		
				public Project getProjectByNume(@PathParam("Nume")String numeProiect) {
					return em.createQuery("SELECT p FROM Project p WHERE p.Nume = :Nume", Project.class)
							.setParameter("Nume", numeProiect)
							.getSingleResult();
				}	
				@GET @Path("/projectdata")
				@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
				public Response getProjectData() throws Exception{
					Project dto = new Project(1, "Dezvoltare Aplicatii Multistrat", new Date());
					JAXBContext jaxbContext = JAXBContext.newInstance(Member.class);
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
					return "MemberServiceEJB is ON... ";
				}
				@Override
				public Collection<Project> toCollection() {
					// TODO Auto-generated method stub
					return null;
				}
				
				
	

}
