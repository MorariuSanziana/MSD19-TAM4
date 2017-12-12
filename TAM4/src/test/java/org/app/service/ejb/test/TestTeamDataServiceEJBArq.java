package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.logging.Logger;

import javax.ejb.EJB;


import org.app.service.ejb.TeamDataService;
import org.app.service.ejb.TeamDataServiceEJB;

import org.app.service.entities.Team;
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
public class TestTeamDataServiceEJBArq {

	private static Logger logger = 
			Logger.getLogger(TestTeamDataServiceEJBArq.class.getName());
	
	@EJB // EJB DataService Ref
	private static TeamDataService service;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(Team.class.getPackage())
	                .addClass(TeamDataService.class)
	                .addClass(TeamDataServiceEJB.class)
	                .addAsResource("META-INF/persistence.xml")
	                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Test
	public void test1_GetMessage() {
		logger.info("DEBUG: Junit TESTING: getMessage ...");
		String response = service.getMessage();
		assertNotNull("Data Service failed!", response);
		logger.info("DEBUG: EJB Response ..." + response);
	}

	@Test
	public void test4_GetTeams() {
		logger.info("DEBUG: Junit TESTING: testGetTeams ...");
		
		Collection<Team> teams = service.getTeams();
		assertTrue("Fail to read teams!", teams.size() > 0);
	}

	@Test
	public void test3_AddTeam() {
		logger.info("DEBUG: Junit TESTING: testAddTeam ...");
		
		Integer teamsToAdd = 1;
		for (int i=1; i <= teamsToAdd; i++){
			
			service.addTeam(new Team(null, "Echipa1", 4, null, null));
		}
		Collection<Team> teams = service.getTeams();
		assertTrue("Fail to add teams!", teams.size() == teamsToAdd);
	}

	@Test
	public void test2_DeleteTeam() {
		logger.info("DEBUG: Junit TESTING: testDeleteTeam ...");
		
		Collection<Team> teams = service.getTeams();
		for (Team t: teams)
			service.removeTeam(t);
		Collection<Team> teamsAfterDelete = service.getTeams();
		assertTrue("Fail to read teams!", teamsAfterDelete.size() == 0);
	}	
}
