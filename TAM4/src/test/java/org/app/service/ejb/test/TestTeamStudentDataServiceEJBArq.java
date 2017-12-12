package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.StudentDataService;
import org.app.service.ejb.StudentDataServiceEJB;
import org.app.service.ejb.TeamDataService;
import org.app.service.ejb.TeamDataServiceEJB;
import org.app.service.ejb.TeamStudentDataService;
import org.app.service.ejb.TeamStudentDataServiceEJB;
import org.app.service.entities.Student;
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
public class TestTeamStudentDataServiceEJBArq {

	
	private static Logger logger = Logger.getLogger(TestInternshipStudentDataServiceEJBArq.class.getName());
	// Arquilian infrastructure
	@Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap
                .create(WebArchive.class, "msd-test.war")
                .addPackage(EntityRepository.class.getPackage()).addPackage(Team.class.getPackage())
                .addClass(StudentDataService.class).addClass(StudentDataServiceEJB.class)
                .addClass(TeamDataService.class).addClass(TeamDataServiceEJB.class)
                .addClass(TeamStudentDataService.class).addClass(TeamStudentDataServiceEJB.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
	
	@EJB // Test EJB Data Service Reference is injected
	private static TeamStudentDataService service;	
	// JUnit test methods
	@Test
	public void test4_GetTeam() {
		logger.info("DEBUG: Junit TESTING: testGetTeam 7002 ...");
		Team team = service.getById(7002);
		assertNotNull("Fail to Get Team 7002!", team);
	}
	
	/* CREATE Test 2: create aggregate*/
	@Test
	public void test3_CreateNewTeam(){
		Team team = service.createNewTeam(7002);
		assertNotNull("Fail to create new Team in repository!", team);
		// update Team
		team.setNumeEchipa(team.getNumeEchipa() + " - changed by test client");		
		List<Student> students = team.getStudenti();
		for(Student s: students) s.setIndicative(s.getIndicative() + " - changed by test client");
			
		team = service.add(team);
		assertNotNull("Fail to save new team in repository!", team);
		logger.info("DEBUG createNewTeam: team changed: " + team);
		// check read
		team = service.getById(7002);  // !!!
		assertNotNull("Fail to find changed team in repository!", team);
		logger.info("DEBUG createNewInternship: queried team" + team);
	}	
	
	@Test
	public void test2_DeleteTeam() {
		logger.info("DEBUG: Junit TESTING: testDeleteTeam 7002...");
		Team team = service.getById(7002);  // !!!
		if (team != null)
			service.remove(team);
		team = service.getById(7002);  // !!!
		assertNull("Fail to delete team 7002!", team);
	}	
	@Test
	public void test1_GetMessage() {
		logger.info("DEBUG: Junit TESTING: testGetMessage ...");
		String response = service.getMessage();
		assertNotNull("Data Service failed!", response);
		logger.info("DEBUG: EJB Response ..." + response);
	}	
}
