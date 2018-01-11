package org.app.service.ejb;

import java.io.ByteArrayOutputStream;

import java.util.Collection;

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





/*
 * SCRUM: 		src/main/webapp/WEB-INF/jboss-web.xml
 * data	: 		org.app.service.rest.ApplicationConfig
 * members: 	@Path
 */
@Path("members") /* http://localhost:8080/TAM4/resources/members */
@Stateless @LocalBean
public class MemberDataServiceEJB implements MemberDataService {
	private static Logger logger = Logger.getLogger(MemberDataServiceEJB.class.getName());
	
	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public MemberDataServiceEJB() {
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
	public Member addMember(Member memberToAdd){
		em.persist(memberToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(memberToAdd);
		return memberToAdd;
	}
	
	// READ
	@GET @Path("/{ID}")		/* SCRUM/data/members 		REST-resource: Member-entity */
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
		public Member getMemberByID(@PathParam("ID")Integer idMembru) {
		logger.info("**** DEBUG REST getMemberByID(): id = " + idMembru);
		return em.find(Member.class, idMembru);
		}	
		@Override
		@GET 					/* SCRUM/data/members 		REST-resource: Members-collection */
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
		public Collection<Member> getMembers(){
			List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
					.getResultList();
			logger.info("**** DEBUG REST members.size()= " + members.size());
			return members;
		}
		@POST 					/* MSD-S4/data/projects 		REST-resource: projects-collection*/
		@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
		public Collection<Member> addIntoCollection(Member member) {
			// save aggregate
			this.addMember(member);
			logger.info("**** DEBUG REST save aggregate POST");
			// return updated collection
			return this.toCollection();
		}
			
		@POST @Path("/new/{id}")
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
		@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
		// Aggregate factory method
		public Member createNewMember(@PathParam("id")Integer id){
			logger.info("**** DEBUG REST createNewMember POST");
			// create project aggregate
			Member member = new Member(null, "Popescu Constantin", "popescu.constantin@gmail.com", "Analyst");
			this.addMember(member);
			return member;
		}
		// REMOVE
		@Override
		@DELETE 					/* SCRUM/data/members		REST-resource: Member-entity */
		@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
				public String removeMember(Member memberToDelete){
					memberToDelete = em.merge(memberToDelete);
					em.remove(memberToDelete);
					em.flush();
					return "True";
				}
					
		
				@DELETE @Path("/{id}") 	/* MSD-S4/data/projects/{id} 	REST-resource: project-entity*/	
				@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
				public void remove(@PathParam("id")Integer id) {
					logger.info("DEBUG: called REMOVE - ById() for projects >>>>>>>>>>>>>> simplified ! + id");
					Member member = this.getMemberByID(id);
					this.removeMember(member);
				}
				
				@Override
				@DELETE 				/* MSD-S4/data/projects 		REST-resource: projects-collection*/
				@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })	
				@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
				@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) // autonomous transaction
				public Collection<Member> removeFromCollection(Member member) {
					logger.info("DEBUG: called REMOVE - project: " + member);
					this.removeMember(member);
					return null;
				}
				// Custom READ: custom query
				@Override
				@GET @Path("/{name}")		/* SCRUM/data/members 		REST-resource: Member-entity */
				@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })		
				public Member getMemberByNume(@PathParam("name")String numeMembru) {
					return em.createQuery("SELECT m FROM Member m WHERE m.Name = :Name", Member.class)
							.setParameter("Name", numeMembru)
							.getSingleResult();
				}	
				@GET @Path("/projectdata")
				@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
				public Response getMemberData() throws Exception{
					Member dto = new Member(null, "Popescu Constantin", "popescu.constantin@gmail.com", "Analyst");
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
				public Collection<Member> toCollection() {
					// TODO Auto-generated method stub
					return null;
				}
			
				
				
			
}
