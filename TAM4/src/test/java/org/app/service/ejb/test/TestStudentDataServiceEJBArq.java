package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.service.ejb.StudentDataService;
import org.app.service.ejb.StudentDataServiceEJB;

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
public class TestStudentDataServiceEJBArq {
	private static Logger logger = 
			Logger.getLogger(TestStudentDataServiceEJBArq.class.getName());
	
	@EJB // EJB DataService Ref
	private static StudentDataService service;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(Student.class.getPackage())
	                .addClass(StudentDataService.class)
	                .addClass(StudentDataServiceEJB.class)
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
	public void test4_GetStudents() {
		logger.info("DEBUG: Junit TESTING: testGetStudents ...");
		
		Collection<Student> students = service.getStudents();
		assertTrue("Fail to read students!",students.size() > 0);
	}

	@Test
	public void test3_AddStudent() {
		logger.info("DEBUG: Junit TESTING: testAddStudent ...");
		
		Integer studentsToAdd = 1;
		for (int i=1; i <= studentsToAdd; i++){
			
			service.addStudent(new Student(null, "Popa Adrian", 195052624, "popa.adrian@gmail.com",0752524, "Iasi"));
			
		}
		Collection<Student> students = service.getStudents();
		assertTrue("Fail to add students!", students.size() == studentsToAdd);
	}

	@Test
	public void test2_DeleteStudent() {
		logger.info("DEBUG: Junit TESTING: testDeleteStudent ...");
		
		Collection<Student> students = service.getStudents();
		for (Student s: students)
			service.removeStudent(s);
		Collection<Student> studentsAfterDelete = service.getStudents();
		assertTrue("Fail to read students!", studentsAfterDelete.size() == 0);
	}	

}
