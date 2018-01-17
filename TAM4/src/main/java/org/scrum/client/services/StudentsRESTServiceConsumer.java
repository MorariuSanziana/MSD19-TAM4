package org.scrum.client.services;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.app.service.entities.Student;


@Named @SessionScoped
public class StudentsRESTServiceConsumer implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(StudentsRESTServiceConsumer.class.getName());
	//
	private static String serviceURL = "http://localhost:8080/TAM4/resources/students";
	
	// private List<Project> projects = new ArrayList<Project>();
	
	public List<Student> getStudents() {
		logger.info(">>> REST Consumer: getProjects.");
		List<Student> students = new ArrayList<Student>();
		students.addAll(this.getAllStudents());
		return students;
	}

	@PostConstruct
	public void init() {
		//this.students.addAll(this.getAllStudents());
	}
	
	// RESTfull CRUD methods
	public Collection<Student> getAllStudents() {
		logger.info("DEBUG: ProjectsRESTServiceConsumer: getAllProjects ...");
		Collection<Student> students = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Student>>(){});
		//projects.stream().forEach(System.out::println);
		return students;
	}

	public List<Student> addStudent(Student student) {
		logger.info("DEBUG: ProjectsRESTServiceConsumer: addProject(): " + student);
		Client client = ClientBuilder.newClient();
		Collection<Student> students;
		
		students = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(student, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<Student>>(){});
		
		// projects.stream().forEach(System.out::println);
		return new ArrayList<>(students);
	}	
	
	public Collection<Student> deleteStudent(Student student) {
		logger.info("DEBUG: ProjectsRESTServiceConsumer: deleteProject: " + student);
		
		String resourceURL = serviceURL + "/";
		
		Client client = ClientBuilder.newClient();
		client.target(resourceURL + student.getIdStudent()).request().delete();
		
		Collection<Student> studentsAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Student>>(){});	
		
		studentsAfterDelete.stream().forEach(System.out::println);
		return studentsAfterDelete;
	}
	
	public Student getByID(Integer idStudent) {
		String resourceURL = serviceURL + "/" + idStudent;
		logger.info("DEBUG: ProjectsRESTServiceConsumer: getByID: " + idStudent);
		
		Student student = ClientBuilder.newClient().target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Student.class);
		
		assertNotNull("REST Data Service failed!", student);
		logger.info(">>>>>> DEBUG: REST Response ..." + student);
		
		return student;
	}	
	
	public Student updateStudent(Student student) {
		String resourceURL = serviceURL + "/" + student.getIdStudent();
		logger.info("DEBUG: ProjectsRESTServiceConsumer: project: " + student);
		Client client = ClientBuilder.newClient();

		// Get project server version
		Student serverStudent = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Student.class);
		
		logger.info(">>> Server-side Project: " + serverStudent);
		
		student = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(student, MediaType.APPLICATION_JSON))
				.readEntity(Student.class);
		
		logger.info(">>> Updated Project: " + student);
		return student;
	}
	
	public Student createStudent() {
		String resourceURL = serviceURL + "/new/"; //createNewProject
		logger.info("DEBUG: ProjectsRESTServiceConsumer: CreateProject ");

		Student student = ClientBuilder.newClient().target(resourceURL + 999)
					.request().accept(MediaType.APPLICATION_JSON)
					.post(null).readEntity(Student.class);
		logger.info(">>> Created/posted Project: " + student);
		
		return student;
	}	
}
