package org.app.service.rest.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.TaskDataService;
import org.app.service.entities.Task;
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
public class TestTaskDataServiceRESTArq {

	//http://localhost:8080/TAM4/resources/tasks
	
	private static Logger logger = Logger.getLogger(TestTaskDataServiceRESTArq.class.getName());

//	 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
	private static String serviceURL = "http://localhost:8080/TAM4/resources/tasks";	
//	private static String serviceURL = "http://localhost:8080/TAM4/data/tasks";	
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "TAM4.war")
	                .addPackage(Task.class.getPackage())
	                .addPackage(TaskDataService.class.getPackage())
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
	public void test4_GetTasks() {
		logger.info("DEBUG: Junit TESTING: test4_GetTasks ...");
		Collection<Task> tasks = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Task>>(){});
		assertTrue("Fail to read Tasks!", tasks.size() > 0);
		tasks.stream().forEach(System.out::println);
	}

//	@Test
	public void test3_AddTask() {
		// addIntoCollection
		logger.info("DEBUG: Junit TESTING: test3_AddTask ...");
		Client client = ClientBuilder.newClient();
		Collection<Task> tasks;
		Integer tasksToAdd = 3;
		Task task;
		for (int i=1; i <= tasksToAdd; i++){
			task = new Task(null, "Implementare clase Java", "Implementare","In progres", new Date(), 2 , 1, "descriere");
			tasks = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(task, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<Task>>(){});
			assertTrue("Fail to read tasks!", tasks.size() > 0);
		}
		tasks = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Task>>(){});
		assertTrue("Fail to add tasks!", tasks.size() >= tasksToAdd);
		tasks.stream().forEach(System.out::println);
	}

	@Test
	public void test3_CreateTask() {
		String resourceURL = serviceURL + "/new/"; //createNewTask
		logger.info("DEBUG: Junit TESTING: test3_CreateTask ...");
		Client client = ClientBuilder.newClient();
		
		Integer tasksToAdd = 3;
		Task task;
		for (int i=1; i <= tasksToAdd; i++){
			task = ClientBuilder.newClient().target(resourceURL + i)
					.request().accept(MediaType.APPLICATION_JSON)
					.post(null).readEntity(Task.class);
			System.out.println(">>> Created/posted Task: " + task);
		}

		Collection<Task> tasks = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Task>>(){});
		
		assertTrue("Fail to add Tasks!", tasks.size() >= tasksToAdd);
	}	
	
	@Test
	public void test2_DeleteTask() {
		String resourceURL = serviceURL + "/";
		logger.info("DEBUG: Junit TESTING: test2_DeleteTask ...");
		Client client = ClientBuilder.newClient();
		Collection<Task> tasks = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Task>>(){});		
		
		for (Task t: tasks) {
			client.target(resourceURL + t.getIdTask()).request().delete();
		}
		
		Collection<Task> tasksAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Task>>(){});	
		assertTrue("Fail to read Tasks!", tasksAfterDelete.size() == 0);
	}
	
//	@Test
	public void test1_GetByID() {
		String resourceURL = serviceURL + "/1";
		logger.info("DEBUG: Junit TESTING: test1_GetMessage ...");
		
		Task task = ClientBuilder.newClient().target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Task.class);
		
		assertNotNull("REST Data Service failed!", task);
		logger.info(">>>>>> DEBUG: REST Response ..." + task);
	}	
	
	@Test
	public void test5_UpdateTask() {
		String resourceURL = serviceURL + "/1"; //createNewTask
		logger.info("************* DEBUG: Junit TESTING: test5_UpdateTask ... :" + resourceURL);
		Client client = ClientBuilder.newClient();
		// Get Task
		Task task = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Task.class);
		
		assertNotNull("REST Data Service failed!", task);
		logger.info(">>> Initial Task: " + task);
		
		// update and save Task
		task.setDenumireTask(task.getDenumireTask() + "_UPD_JSON");
		task = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(task, MediaType.APPLICATION_JSON))
				.readEntity(Task.class);
		
		logger.info(">>> Updated task: " + task);
		
		assertNotNull("REST Data Service failed!", task);
	}	
}
