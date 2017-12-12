package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.InternshipDataService;
import org.app.service.ejb.InternshipDataServiceEJB;
import org.app.service.ejb.InternshipStudentDataService;
import org.app.service.ejb.InternshipStudentDataServiceEJB;
import org.app.service.ejb.StudentDataService;
import org.app.service.ejb.StudentDataServiceEJB;
import org.app.service.entities.Internship;
import org.app.service.entities.Student;
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
public class TestInternshipStudentDataServiceEJBArq {
	private static Logger logger = Logger.getLogger(TestInternshipStudentDataServiceEJBArq.class.getName());
	// Arquilian infrastructure
	@Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap
                .create(WebArchive.class, "msd-test.war")
                .addPackage(EntityRepository.class.getPackage()).addPackage(Internship.class.getPackage())
                .addClass(StudentDataService.class).addClass(StudentDataServiceEJB.class)
                .addClass(InternshipDataService.class).addClass(InternshipDataServiceEJB.class)
                .addClass(InternshipStudentDataService.class).addClass(InternshipStudentDataServiceEJB.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
	
	@EJB // Test EJB Data Service Reference is injected
	private static InternshipStudentDataService service;	
	// JUnit test methods
	@Test
	public void test4_GetInternship() {
		logger.info("DEBUG: Junit TESTING: testGetInternship 7002 ...");
		Internship internship = service.getById(7002);
		assertNotNull("Fail to Get Internship 7002!", internship);
	}
	
	/* CREATE Test 2: create aggregate*/
	@Test
	public void test3_CreateNewInternship(){
		Internship internship = service.createNewInternship(7002);
		assertNotNull("Fail to create new internship in repository!", internship);
		// update internship
		internship.setDenumireInternship(internship.getDenumireInternship() + " - changed by test client");		
		List<Student> students = internship.getStudents();
		for(Student s: students) s.setIndicative(s.getIndicative() + " - changed by test client");
			
		internship = service.add(internship);
		assertNotNull("Fail to save new internship in repository!", internship);
		logger.info("DEBUG createNewInternship: internship changed: " + internship);
		// check read
		internship = service.getById(7002);  // !!!
		assertNotNull("Fail to find changed internship in repository!", internship);
		logger.info("DEBUG createNewInternship: queried internship" + internship);
	}	
	
	@Test
	public void test2_DeleteInternship() {
		logger.info("DEBUG: Junit TESTING: testDeleteInternship 7002...");
		Internship internship = service.getById(7002);  // !!!
		if (internship != null)
			service.remove(internship);
		internship = service.getById(7002);  // !!!
		assertNull("Fail to delete Internship 7002!", internship);
	}	
	@Test
	public void test1_GetMessage() {
		logger.info("DEBUG: Junit TESTING: testGetMessage ...");
		String response = service.getMessage();
		assertNotNull("Data Service failed!", response);
		logger.info("DEBUG: EJB Response ..." + response);
	}	
}
