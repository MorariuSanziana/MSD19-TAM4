package org.app.service.rest.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.StudentDataService;
import org.app.service.entities.Student;
import org.app.service.rest.ApplicationConfig;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(Arquillian.class) 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestStudentDataServiceRESTArq {

	//http://localhost:8080/TAM4/resources/students
	
	private static Logger logger = Logger.getLogger(TestStudentDataServiceRESTArq.class.getName());

//	 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
	private static String serviceURL = "http://localhost:8080/TAM4/resources/students";	
//	private static String serviceURL = "http://localhost:8080/TAM4/data/students";	
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "TAM4.war")
	                .addPackage(Student.class.getPackage())
	                .addPackage(StudentDataService.class.getPackage())
	                .addPackage(EntityRepository.class.getPackage())
	                .addPackage(ApplicationConfig.class.getPackage())
	                .addAsResource("META-INF/persistence.xml")
	                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); // all mode by default
	}	
	
//	@Test
	public void test1_GetMessage() {
		String resourceURL = serviceURL + "/test";
		logger.info("DEBUG: Junit TESTING: test1_GetMessage ...");
		String response = ClientBuilder.newClient().target(resourceURL)
				.request().get()
				.readEntity(String.class);
		assertNotNull("Data Service failed!", response);
		logger.info("DEBUG: EJB Response ..." + response);
	}

//	@Test
	public void test4_GetStudents() {
		logger.info("DEBUG: Junit TESTING: test4_GetStudents ...");
		Collection<Student> students = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Student>>(){});
		assertTrue("Fail to read Students!", students.size() > 0);
		students.stream().forEach(System.out::println);
	}

//	@Test
	public void test3_AddStudent() {
		// addIntoCollection
		logger.info("DEBUG: Junit TESTING: test3_AddStudent ...");
		Client client = ClientBuilder.newClient();
		Collection<Student> students;
		Integer studentsToAdd = 3;
		Student student;
		for (int i=1; i <= studentsToAdd; i++){
			student = new Student(null, "Popa Adrian", 195052624, "popa.adrian@gmail.com",0752524, "Iasi");
			students = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(student, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<Student>>(){});
			assertTrue("Fail to read students!", students.size() > 0);
		}
		students = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Student>>(){});
		assertTrue("Fail to add students!", students.size() >=studentsToAdd);
		students.stream().forEach(System.out::println);
	}

	@Test
	public void test3_CreateStudent() {
		String resourceURL = serviceURL + "/new/"; //createNewStudent
		logger.info("DEBUG: Junit TESTING: test3_CreateStudent ...");
		Client client = ClientBuilder.newClient();
		
		Integer studentsToAdd = 3;
		Student student;
		for (int i=1; i <= studentsToAdd; i++){
			student = ClientBuilder.newClient().target(resourceURL + i)
					.request().accept(MediaType.APPLICATION_JSON)
					.post(null).readEntity(Student.class);
			System.out.println(">>> Created/posted Student: " + student);
		}

		Collection<Student> students = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Student>>(){});
		
		assertTrue("Fail to add Students!", students.size() >= studentsToAdd);
	}	
	
	@Test
	public void test2_DeleteStudent() {
		String resourceURL = serviceURL + "/";
		logger.info("DEBUG: Junit TESTING: test2_DeleteStudent ...");
		Client client = ClientBuilder.newClient();
		Collection<Student> students = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Student>>(){});		
		
		for (Student s: students) {
			client.target(resourceURL + s.getIdStudent()).request().delete();
		}
		
		Collection<Student> studentsAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Student>>(){});	
		assertTrue("Fail to read Students!", studentsAfterDelete.size() == 0);
	}
	
//	@Test
	public void test1_GetByID() {
		String resourceURL = serviceURL + "/1";
		logger.info("DEBUG: Junit TESTING: test1_GetMessage ...");
		
		Student student = ClientBuilder.newClient().target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Student.class);
		
		assertNotNull("REST Data Service failed!", student);
		logger.info(">>>>>> DEBUG: REST Response ..." + student);
	}	
	
	@Test
	public void test5_UpdateStudent() {
		String resourceURL = serviceURL + "/1"; //createNewStudent
		logger.info("************* DEBUG: Junit TESTING: test5_UpdateStudent ... :" + resourceURL);
		Client client = ClientBuilder.newClient();
		// Get Student
		Student student = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Student.class);
		
		assertNotNull("REST Data Service failed!", student);
		logger.info(">>> Initial Student: " + student);
		
		// update and save Student
		student.setNumeStudent(student.getNumeStudent() + "_UPD_JSON");
		student = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(student, MediaType.APPLICATION_JSON))
				.readEntity(Student.class);
		
		logger.info(">>> Updated student: " + student);
		
		assertNotNull("REST Data Service failed!", student);
	}	
}
