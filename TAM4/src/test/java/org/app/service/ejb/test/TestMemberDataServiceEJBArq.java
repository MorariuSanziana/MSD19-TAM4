package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.logging.Logger;

import javax.ejb.EJB;


import org.app.service.ejb.MemberDataService;
import org.app.service.ejb.MemberDataServiceEJB;
import org.app.service.entities.Member;
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
public class TestMemberDataServiceEJBArq {
	private static Logger logger = 
			Logger.getLogger(TestMemberDataServiceEJBArq.class.getName());
	
	@EJB // EJB DataService Ref
	private static MemberDataService service;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(Member.class.getPackage())
	                .addClass(MemberDataService.class)
	                .addClass(MemberDataServiceEJB.class)
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
	public void test4_GetMembers() {
		logger.info("DEBUG: Junit TESTING: testGetMembers ...");
		
		Collection<Member> members = service.getMembers();
		assertTrue("Fail to read members!", members.size() > 0);
	}

	@Test
	public void test3_AddMember() {
		logger.info("DEBUG: Junit TESTING: testAddMember ...");
		
		Integer membersToAdd = 1;
		for (int i=1; i <= membersToAdd; i++){
			
			service.addMember(new Member(null, "Popescu Constantin"  + (100+i), "popescu.constantin@gmail.com" + (100+i), "Analyst"));
		}
		Collection<Member> members = service.getMembers();
		assertTrue("Fail to add members!", members.size() == membersToAdd);
	}

	@Test
	public void test2_DeleteMember() {
		logger.info("DEBUG: Junit TESTING: testDeleteMember ...");
		
		Collection<Member> members = service.getMembers();
		for (Member m: members)
			service.removeMember(m);
		Collection<Member> membersAfterDelete = service.getMembers();
		assertTrue("Fail to read members!", membersAfterDelete.size() == 0);
	}	


}
