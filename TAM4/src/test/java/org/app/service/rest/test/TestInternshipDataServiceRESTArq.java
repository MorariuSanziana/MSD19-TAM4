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
import org.app.service.ejb.InternshipDataService;
import org.app.service.entities.Internship;
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
public class TestInternshipDataServiceRESTArq {
	
	// http://localhost:8080/TAM4/resources/internships

	private static Logger logger = Logger.getLogger(TestInternshipDataServiceRESTArq.class.getName());

//	 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
	private static String serviceURL = "http://localhost:8080/TAM4/resources/internships";	
//	private static String serviceURL = "http://localhost:8080/TAM4/data/internships";	
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "TAM4.war")
	                .addPackage(Internship.class.getPackage())
	                .addPackage(InternshipDataService.class.getPackage())
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
	public void test4_GetInternships() {
		logger.info("DEBUG: Junit TESTING: test4_GetInternships ...");
		Collection<Internship> internships = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Internship>>(){});
		assertTrue("Fail to read Internships!", internships.size() > 0);
		internships.stream().forEach(System.out::println);
	}

	//@Test
	public void test3_AddInternship() {
		// addIntoCollection
		logger.info("DEBUG: Junit TESTING: test3_AddInternship ...");
		Client client = ClientBuilder.newClient();
		Collection<Internship> internships;
		Integer internshipsToAdd = 3;
		Internship internship;
		for (int i=1; i <= internshipsToAdd; i++){
			internship = new Internship(null,"Audit Internship", new Date(), new Date(), 3, null);
			internships = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(internship, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<Internship>>(){});
			assertTrue("Fail to read Internships!", internships.size() > 0);
		}
		internships = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Internship>>(){});
		assertTrue("Fail to add Internships!", internships.size() >= internshipsToAdd);
		internships.stream().forEach(System.out::println);
	}

	@Test
	public void test3_CreateInternship() {
		String resourceURL = serviceURL + "/new/"; //createNewInternship
		logger.info("DEBUG: Junit TESTING: test3_CreateInternship ...");
		Client client = ClientBuilder.newClient();
		
		Integer internshipsToAdd = 3;
		Internship internship;
		for (int i=1; i <= internshipsToAdd; i++){
			internship = ClientBuilder.newClient().target(resourceURL + i)
					.request().accept(MediaType.APPLICATION_JSON)
					.post(null).readEntity(Internship.class);
			System.out.println(">>> Created/posted Internship: " + internship);
		}

		Collection<Internship> internships = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Internship>>(){});
		
		assertTrue("Fail to add Internships!", internships.size() >= internshipsToAdd);
	}	
	
	@Test
	public void test2_DeleteInternship() {
		String resourceURL = serviceURL + "/";
		logger.info("DEBUG: Junit TESTING: test2_DeleteInternship ...");
		Client client = ClientBuilder.newClient();
		Collection<Internship> internships = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Internship>>(){});		
		
		for (Internship i: internships) {
			client.target(resourceURL + i.getIdInternship()).request().delete();
		}
		
		Collection<Internship> internshipsAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Internship>>(){});	
		assertTrue("Fail to read Internships!", internshipsAfterDelete.size() == 0);
	}
	
//	@Test
	public void test1_GetByID() {
		String resourceURL = serviceURL + "/1";
		logger.info("DEBUG: Junit TESTING: test1_GetMessage ...");
		
		Internship internship = ClientBuilder.newClient().target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Internship.class);
		
		assertNotNull("REST Data Service failed!", internship);
		logger.info(">>>>>> DEBUG: REST Response ..." + internship);
	}	
	
	@Test
	public void test5_UpdateInternship() {
		String resourceURL = serviceURL + "/1"; //createNewInternship
		logger.info("************* DEBUG: Junit TESTING: test5_UpdateInternship ... :" + resourceURL);
		Client client = ClientBuilder.newClient();
		// Get internship
		Internship internship = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Internship.class);
		
		assertNotNull("REST Data Service failed!", internship);
		logger.info(">>> Initial Internship: " + internship);
		
		// update and save project
		internship.setDenumireInternship(internship.getDenumireInternship() + "_UPD_JSON");
		internship = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(internship, MediaType.APPLICATION_JSON))
				.readEntity(Internship.class);
		
		logger.info(">>> Updated Internship: " + internship);
		
		assertNotNull("REST Data Service failed!", internship);
	}	






}




