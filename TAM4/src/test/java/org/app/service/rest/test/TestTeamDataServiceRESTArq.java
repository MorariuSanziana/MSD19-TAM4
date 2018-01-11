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
import org.app.service.ejb.TeamDataService;
import org.app.service.entities.Team;
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
public class TestTeamDataServiceRESTArq {

	//http://localhost:8080/TAM4/resources/teams
	
	private static Logger logger = Logger.getLogger(TestTeamDataServiceRESTArq.class.getName());

//	 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
	private static String serviceURL = "http://localhost:8080/TAM4/resources/teams";	
//	private static String serviceURL = "http://localhost:8080/TAM4/data/teams";	
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "TAM4.war")
	                .addPackage(Team.class.getPackage())
	                .addPackage(TeamDataService.class.getPackage())
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
	public void test4_GetTeams() {
		logger.info("DEBUG: Junit TESTING: test4_GetTeams ...");
		Collection<Team> teams = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Team>>(){});
		assertTrue("Fail to read Teams!", teams.size() > 0);
		teams.stream().forEach(System.out::println);
	}

//	@Test
	public void test3_AddTeam() {
		// addIntoCollection
		logger.info("DEBUG: Junit TESTING: test3_AddTeam ...");
		Client client = ClientBuilder.newClient();
		Collection<Team> teams;
		Integer teamsToAdd = 3;
		Team team;
		for (int i=1; i <= teamsToAdd; i++){
			team = new Team(null, "Echipa1", 4, null, null);
			teams = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(team, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<Team>>(){});
			assertTrue("Fail to read teams!", teams.size() > 0);
		}
		teams = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Team>>(){});
		assertTrue("Fail to add teams!",teams.size() >= teamsToAdd);
		teams.stream().forEach(System.out::println);
	}

	@Test
	public void test3_CreateTeam() {
		String resourceURL = serviceURL + "/new/"; //createNewTeam
		logger.info("DEBUG: Junit TESTING: test3_CreateTeam ...");
		Client client = ClientBuilder.newClient();
		
		Integer teamsToAdd = 3;
		Team team;
		for (int i=1; i <= teamsToAdd; i++){
			team = ClientBuilder.newClient().target(resourceURL + i)
					.request().accept(MediaType.APPLICATION_JSON)
					.post(null).readEntity(Team.class);
			System.out.println(">>> Created/posted team: " + team);
		}

		Collection<Team> teams = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Team>>(){});
		
		assertTrue("Fail to add teams!", teams.size() >= teamsToAdd);
	}	
	
	@Test
	public void test2_DeleteTeam() {
		String resourceURL = serviceURL + "/";
		logger.info("DEBUG: Junit TESTING: test2_DeleteTeam ...");
		Client client = ClientBuilder.newClient();
		Collection<Team> teams = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Team>>(){});		
		
		for (Team t: teams) {
			client.target(resourceURL + t.getIdEchipa()).request().delete();
		}
		
		Collection<Team> teamsAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Team>>(){});	
		assertTrue("Fail to read Teams!", teamsAfterDelete.size() == 0);
	}
	
//	@Test
	public void test1_GetByID() {
		String resourceURL = serviceURL + "/1";
		logger.info("DEBUG: Junit TESTING: test1_GetMessage ...");
		
		Team team = ClientBuilder.newClient().target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Team.class);
		
		assertNotNull("REST Data Service failed!", team);
		logger.info(">>>>>> DEBUG: REST Response ..." + team);
	}	
	
	@Test
	public void test5_UpdateTeam() {
		String resourceURL = serviceURL + "/1"; //createNewTeam
		logger.info("************* DEBUG: Junit TESTING: test5_UpdateTeam ... :" + resourceURL);
		Client client = ClientBuilder.newClient();
		// Get Team
		Team team = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Team.class);
		
		assertNotNull("REST Data Service failed!", team);
		logger.info(">>> Initial Team: " + team);
		
		// update and save Team
		team.setNumeEchipa(team.getNumeEchipa() + "_UPD_JSON");
		team = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(team, MediaType.APPLICATION_JSON))
				.readEntity(Team.class);
		
		logger.info(">>> Updated team: " + team);
		
		assertNotNull("REST Data Service failed!", team);
	}	
}
