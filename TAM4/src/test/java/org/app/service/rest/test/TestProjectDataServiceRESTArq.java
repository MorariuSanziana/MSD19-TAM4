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
import org.app.service.ejb.ProjectDataService;
import org.app.service.entities.Project;
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
public class TestProjectDataServiceRESTArq {

	// http://localhost:8080/TAM4/resources/projects
	
	private static Logger logger = Logger.getLogger(TestProjectDataServiceRESTArq.class.getName());

//	 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
	private static String serviceURL = "http://localhost:8080/TAM4/resources/projects";	
//	private static String serviceURL = "http://localhost:8080/TAM4/data/projects";	
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "TAM4.war")
	                .addPackage(Project.class.getPackage())
	                .addPackage(ProjectDataService.class.getPackage())
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
	public void test4_GetProjects() {
		logger.info("DEBUG: Junit TESTING: test4_GetProjects ...");
		Collection<Project> projects = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Project>>(){});
		assertTrue("Fail to read Projects!", projects.size() > 0);
		projects.stream().forEach(System.out::println);
	}

//	@Test
	public void test3_AddProject() {
		// addIntoCollection
		logger.info("DEBUG: Junit TESTING: test3_AddProject ...");
		Client client = ClientBuilder.newClient();
		Collection<Project> projects;
		Integer projectsToAdd = 3;
		Project project;
		for (int i=1; i <= projectsToAdd; i++){
			project = new Project(1, "Dezvoltare Aplicatii Multistrat", new Date());
			projects = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(project, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<Project>>(){});
			assertTrue("Fail to read Projects!", projects.size() > 0);
		}
		projects = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Project>>(){});
		assertTrue("Fail to add Projects!", projects.size() >= projectsToAdd);
		projects.stream().forEach(System.out::println);
	}

	@Test
	public void test3_CreateProject() {
		String resourceURL = serviceURL + "/new/"; //createNewProject
		logger.info("DEBUG: Junit TESTING: test3_CreateProject ...");
		Client client = ClientBuilder.newClient();
		
		Integer projectsToAdd = 3;
		Project project;
		for (int i=1; i <= projectsToAdd; i++){
			project = ClientBuilder.newClient().target(resourceURL + i)
					.request().accept(MediaType.APPLICATION_JSON)
					.post(null).readEntity(Project.class);
			System.out.println(">>> Created/posted Project: " + project);
		}

		Collection<Project> projects = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Project>>(){});
		
		assertTrue("Fail to add Projects!", projects.size() >= projectsToAdd);
	}	
	
	@Test
	public void test2_DeleteProject() {
		String resourceURL = serviceURL + "/";
		logger.info("DEBUG: Junit TESTING: test2_DeleteProject ...");
		Client client = ClientBuilder.newClient();
		Collection<Project> projects = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Project>>(){});		
		
		for (Project p: projects) {
			client.target(resourceURL + p.getIdProiect()).request().delete();
		}
		
		Collection<Project> projectsAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Project>>(){});	
		assertTrue("Fail to read Projects!", projectsAfterDelete.size() == 0);
	}
	
//	@Test
	public void test1_GetByID() {
		String resourceURL = serviceURL + "/1";
		logger.info("DEBUG: Junit TESTING: test1_GetMessage ...");
		
		Project project = ClientBuilder.newClient().target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Project.class);
		
		assertNotNull("REST Data Service failed!", project);
		logger.info(">>>>>> DEBUG: REST Response ..." + project);
	}	
	
	@Test
	public void test5_UpdateProject() {
		String resourceURL = serviceURL + "/1"; //createNewProject
		logger.info("************* DEBUG: Junit TESTING: test5_UpdateProject ... :" + resourceURL);
		Client client = ClientBuilder.newClient();
		// Get project
		Project project = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Project.class);
		
		assertNotNull("REST Data Service failed!", project);
		logger.info(">>> Initial Project: " + project);
		
		// update and save project
		project.setNumeProiect(project.getNumeProiect() + "_UPD_JSON");
		project = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(project, MediaType.APPLICATION_JSON))
				.readEntity(Project.class);
		
		logger.info(">>> Updated Project: " + project);
		
		assertNotNull("REST Data Service failed!", project);
	}	
}
