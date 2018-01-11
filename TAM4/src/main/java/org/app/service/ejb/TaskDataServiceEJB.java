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
import org.app.service.entities.Task;

/*
 * SCRUM: 		src/main/webapp/WEB-INF/jboss-web.xml
 * data	: 		org.app.service.rest.ApplicationConfig
 * tasks: 	@Path
 */
@Path("tasks") /* http://localhost:8080/TAM4/resources/tasks */
@Stateless @LocalBean
public class TaskDataServiceEJB implements TaskDataService {

	private static Logger logger = Logger.getLogger(TaskDataServiceEJB.class.getName());
	
	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public TaskDataServiceEJB() {
	}
	// Init after constructor
	@PostConstruct
	public void init(){
		logger.info("POSTCONSTRUCT-INIT : " + this.em);
	}	
	
	/* CRUD operations implementation */
	// CREATE or UPDATE
	@PUT @Path("/{id}") 	/* SCRUM/data/tasks/{id} 	REST-resource: Task-entity */	
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
	public Task addTask(Task taskToAdd){
		em.persist(taskToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(taskToAdd);
		return taskToAdd;
	}
	
	// READ
	@GET @Path("/{id}")		/* SCRUM/data/tasks 		REST-resource: Task-entity */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
		public Task getTaskByID(@PathParam("id")Integer idTask) {
		logger.info("**** DEBUG REST getTaskByID(): id = " + idTask);
			return em.find(Task.class, idTask);
		}
	@GET 					/* SCRUM/data/tasks 		REST-resource: Tasks-collection */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
		public Collection<Task> getTasks(){
			List<Task> tasks = em.createQuery("SELECT t FROM Task t", Task.class)
					.getResultList();
			logger.info("**** DEBUG REST tasks.size()= " + tasks.size());
			return tasks;
		}
	
	@POST 					/* MSD-S4/data/projects 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	public Collection<Task> addIntoCollection(Task task) {
		// save aggregate
		this.addTask(task);
		logger.info("**** DEBUG REST save aggregate POST");
		// return updated collection
		return this.toCollection();
	}
		
	@POST @Path("/new/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	// Aggregate factory method
	public Task createNewTask(@PathParam("id")Integer id){
		logger.info("**** DEBUG REST createNewProject POST");
		// create project aggregate
		Task task = new Task(null, "Implementare clase Java", "Implementare","In progres", new Date(), 2 , 1, "descriere");
		this.addTask(task);
		return task;
	}
		
		// REMOVE
	// REMOVE
		@DELETE 					/* SCRUM/data/internships		REST-resource: Internship-entity */
		@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		@Override
			public String removeTask(Task taskToDelete){
			taskToDelete = em.merge(taskToDelete);
				em.remove(taskToDelete);
				em.flush();
				return "True";
			}
		@DELETE @Path("/{id}") 	/* MSD-S4/data/projects/{id} 	REST-resource: project-entity*/	
		@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
		public void remove(@PathParam("id")Integer id) {
			logger.info("DEBUG: called REMOVE - ById() for projects >>>>>>>>>>>>>> simplified ! + id");
			Task task = this.getTaskByID(id);
			this.removeTask(task);
		}
		
		@Override
		@DELETE 				/* MSD-S4/data/projects 		REST-resource: projects-collection*/
		@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
		public Collection<Task> removeFromCollection(Task task) {
			logger.info("DEBUG: called REMOVE - project: " + task);
			this.removeTask(task);
			return null;
		}
 
		// Custom READ: custom query
	@GET @Path("/{Denumire}")		/* SCRUM/data/tasks 		REST-resource: Task-entity */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
		public Task getTaskByDenumire(@PathParam("Denumire")String denumireTask) {
			return em.createQuery("SELECT t FROM Task t WHERE t.Denumire = :Denumire", Task.class)
							.setParameter("Denumire", denumireTask)
							.getSingleResult();
				}	


				// Others
				@Override
				public String getMessage() {
					return "TaskServiceEJB is ON... ";
				}
			
				@GET @Path("/projectdata")
				@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
				public Response getTaskData() throws Exception{
					Task dto = new Task(null, "Implementare clase Java", "Implementare","In progres", new Date(), 2 , 1, "descriere");
					JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);
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
				
				@Override
				public Collection<Task> toCollection() {
					// TODO Auto-generated method stub
					return null;
				}	
}
