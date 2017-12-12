package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.app.service.ejb.InternshipDataService;
import org.app.service.ejb.InternshipDataServiceEJB;
import org.app.service.entities.Internship;

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
public class TestInternshipDataServiceEJBArq {
	private static Logger logger = 
			Logger.getLogger(TestInternshipDataServiceEJBArq.class.getName());

	
	@EJB // EJB DataService Ref
	private static InternshipDataService service;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(Internship.class.getPackage())
	                .addClass(InternshipDataService.class)
	                .addClass(InternshipDataServiceEJB.class)
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
	public void test4_GetInternships() {
		logger.info("DEBUG: Junit TESTING: testGetInternships ...");
		
		Collection<Internship> internships = service.getInternships();
		assertTrue("Fail to read internships!", internships.size() > 0);
	}
	
	
	
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	 Date date = new Date();
	@Test
	public void test3_AddInternship() {
		logger.info("DEBUG: Junit TESTING: testAddInternship ...");
		
		
		Integer internshipsToAdd = 1;
		for (int i=1; i <= internshipsToAdd; i++){
			
			service.addInternship(new Internship(null,"Audit Internship", new Date(), new Date(), 3, null));
			
		}
		Collection<Internship> internships = service.getInternships();
		assertTrue("Fail to add internships!", internships.size() == internshipsToAdd);
	}
		
		
	@Test
	public void test2_DeleteInternship() {
		logger.info("DEBUG: Junit TESTING: testDeleteInternship ...");
		
		Collection<Internship> internships = service.getInternships();
		for (Internship i: internships)
			service.removeInternship(i);
		Collection<Internship> internshipsAfterDelete = service.getInternships();
		assertTrue("Fail to read internships!", internshipsAfterDelete.size() == 0);
	}	


}
