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
import org.app.service.entities.Student;

/*
 * SCRUM: 		src/main/webapp/WEB-INF/jboss-web.xml
 * data	: 		org.app.service.rest.ApplicationConfig
 * features: 	@Path
 */
@Path("students") /* http://localhost:8080/TAM4/resources/students */
@Stateless @LocalBean
public class StudentDataServiceEJB implements StudentDataService {
	private static Logger logger = Logger.getLogger(StudentDataServiceEJB.class.getName());

	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public StudentDataServiceEJB() {
	}
	// Init after constructor
	@PostConstruct
	public void init(){
		logger.info("POSTCONSTRUCT-INIT : " + this.em);
	}		

	/* CRUD operations implementation */
	// CREATE or UPDATE
	@PUT @Path("/{ID}") 	/* SCRUM/data/students/{ID} 	REST-resource: Student-entity */	
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
	public Student addStudent(Student studentToAdd){
		em.persist(studentToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(studentToAdd);
		return studentToAdd;
	}
	

	// READ
	@GET @Path("/{ID}")		/* SCRUM/data/students 		REST-resource: Student-entity */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
		public Student getStudentByID(@PathParam("ID")Integer idStudent) {
		logger.info("**** DEBUG REST getStudentByID(): id = " + idStudent);
			return em.find(Student.class, idStudent);
		}
	
	@GET 					/* SCRUM/data/students 		REST-resource: Students-collection */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
		public Collection<Student> getStudents(){
			List<Student> students = em.createQuery("SELECT s FROM Student s", Student.class)
					.getResultList();
			logger.info("**** DEBUG REST students.size()= " + students.size());
		return students;
		}
	@POST 					/* MSD-S4/data/projects 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	public Collection<Student> addIntoCollection(Student student) {
		// save aggregate
		this.addStudent(student);
		logger.info("**** DEBUG REST save aggregate POST");
		// return updated collection
		return this.toCollection();
	}
		
	@POST @Path("/new/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	// Aggregate factory method
	public Student createNewStudent(@PathParam("id")Integer id){
		logger.info("**** DEBUG REST createNewMember POST");
		// create project aggregate
		Student student = new Student(null, "Popa Adrian", 195052624, "popa.adrian@gmail.com",0752524, "Iasi");
		this.addStudent(student);
		return student;
	}
	
		// REMOVE
	@DELETE 					/* SCRUM/data/students		REST-resource: Student-entity */
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
		public String removeStudent(Student studentToDelete){
					studentToDelete = em.merge(studentToDelete);
					em.remove(studentToDelete);
					em.flush();
			return "True";
				}
	@DELETE @Path("/{id}") 	/* MSD-S4/data/projects/{id} 	REST-resource: project-entity*/	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public void remove(@PathParam("id")Integer id) {
		logger.info("DEBUG: called REMOVE - ById() for projects >>>>>>>>>>>>>> simplified ! + id");
		Student student = this.getStudentByID(id);
		this.removeStudent(student);
	}
	
	@Override
	@DELETE 				/* MSD-S4/data/projects 		REST-resource: projects-collection*/
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
	public Collection<Student> removeFromCollection(Student student) {
		logger.info("DEBUG: called REMOVE - project: " + student);
		this.removeStudent(student);
		return null;
	}
				
		// Custom READ: custom query
	@GET @Path("/{Name}")		/* SCRUM/data/students 		REST-resource: Student-entity */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
	@Override
		public Student getStudentByName(@PathParam("Name")String numeStudent) {
				return em.createQuery("SELECT s FROM Student s WHERE s.Name = :Name", Student.class)
							.setParameter("Name", numeStudent)
							.getSingleResult();
				}	
	
	@GET @Path("/projectdata")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStudentData() throws Exception{
		Student dto = new Student(null, "Popa Adrian", 195052624, "popa.adrian@gmail.com",0752524, "Iasi");
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
			return "StudentServiceEJB is ON... ";
				}
				
		
				@Override
				public Collection<Student> toCollection() {
					// TODO Auto-generated method stub
					return null;
				}
				

}
