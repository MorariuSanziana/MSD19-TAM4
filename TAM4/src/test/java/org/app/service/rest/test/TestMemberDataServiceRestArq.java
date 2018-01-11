package org.app.service.rest.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.app.patterns.EntityRepository;
import org.app.service.ejb.MemberDataService;
import org.app.service.entities.Internship;
import org.app.service.entities.Member;
import org.app.service.entities.Student;
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
public class TestMemberDataServiceRestArq {

	
	//http://localhost:8080/TAM4/resources/members
	
		private static Logger logger = Logger.getLogger(TestMemberDataServiceRestArq.class.getName());

		
//		 server_wildfly_web_url/deployment_archive_name/ApplicationConfig_@ApplicationPath/EJB_@Path
		private static String serviceURL = "http://localhost:8080/TAM4/resources/members";	
//		private static String serviceURL = "http://localhost:8080/TAM4/data/members";	
		
		@Deployment // Arquilian infrastructure
		public static Archive<?> createDeployment() {
		        return ShrinkWrap
		                .create(WebArchive.class, "TAM4.war")
		                .addPackage(Member.class.getPackage())
		                .addPackage(MemberDataService.class.getPackage())
		                .addPackage(EntityRepository.class.getPackage())
		                .addPackage(ApplicationConfig.class.getPackage())
		                .addAsResource("META-INF/persistence.xml")
		                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); // all mode by default
		}	
		
//		@Test
		public void test1_GetMessage() {
			String resourceURL = serviceURL + "/test";
			logger.info("DEBUG: Junit TESTING: test1_GetMessage ...");
			String response = ClientBuilder.newClient().target(resourceURL)
					.request().get()
					.readEntity(String.class);
			assertNotNull("Data Service failed!", response);
			logger.info("DEBUG: EJB Response ..." + response);
		}

//		@Test
		public void test4_GetMembers() {
			logger.info("DEBUG: Junit TESTING: test4_GetMembers ...");
			Collection<Member> members = ClientBuilder.newClient()
					.target(serviceURL)
					.request().get()
					.readEntity(new GenericType<Collection<Member>>(){});
			assertTrue("Fail to read Members!", members.size() > 0);
			members.stream().forEach(System.out::println);
		}

//		@Test
		public void test3_AddMember() {
			// addIntoCollection
			logger.info("DEBUG: Junit TESTING: test3_AddMember ...");
			Client client = ClientBuilder.newClient();
			Collection<Member> members;
			Integer membersToAdd = 3;
			Member member;
			for (int i=1; i <= membersToAdd; i++){
				member = new Member(null, "Popescu Constantin"  + (100+i), "popescu.constantin@gmail.com" + (100+i), "Analyst");
				members = client.target(serviceURL)
					.request().accept(MediaType.APPLICATION_JSON)
					.post(Entity.entity(member, MediaType.APPLICATION_JSON))
					.readEntity(new GenericType<Collection<Member>>(){});
				assertTrue("Fail to read Members!", members.size() > 0);
			}
			members = client.target(serviceURL)
					.request().get()
					.readEntity(new GenericType<Collection<Member>>(){});
			assertTrue("Fail to add Members!", members.size() >= membersToAdd);
			members.stream().forEach(System.out::println);
		}

		@Test
		public void test3_CreateMember() {
			String resourceURL = serviceURL + "/new/"; //createNewMember
			logger.info("DEBUG: Junit TESTING: test3_CreateMember ...");
			Client client = ClientBuilder.newClient();
			
			Integer membersToAdd = 3;
			Member member;
			for (int i=1; i <= membersToAdd; i++){
				member = ClientBuilder.newClient().target(resourceURL + i)
						.request().accept(MediaType.APPLICATION_JSON)
						.post(null).readEntity(Member.class);
				System.out.println(">>> Created/posted Member: " + member);
			}

			Collection<Member> members = client.target(serviceURL)
					.request().get()
					.readEntity(new GenericType<Collection<Member>>(){});
			
			assertTrue("Fail to add Members!", members.size() >= membersToAdd);
		}	
		
		@Test
		public void test2_DeleteMember() {
			String resourceURL = serviceURL + "/";
			logger.info("DEBUG: Junit TESTING: test2_DeleteMember ...");
			Client client = ClientBuilder.newClient();
			Collection<Member> members = client.target(serviceURL)
					.request().get()
					.readEntity(new GenericType<Collection<Member>>(){});		
			
			for (Member m: members) {
				client.target(resourceURL + m.getIdMembru()).request().delete();
			}
			
			Collection<Member> membersAfterDelete = client.target(serviceURL)
					.request().get()
					.readEntity(new GenericType<Collection<Member>>(){});	
			assertTrue("Fail to read Members!", membersAfterDelete.size() == 0);
		}
		
//		@Test
		public void test1_GetByID() {
			String resourceURL = serviceURL + "/1";
			logger.info("DEBUG: Junit TESTING: test1_GetMessage ...");
			
			Member member = ClientBuilder.newClient().target(resourceURL)
					.request().accept(MediaType.APPLICATION_JSON)
					.get().readEntity(Member.class);
			
			assertNotNull("REST Data Service failed!", member);
			logger.info(">>>>>> DEBUG: REST Response ..." + member);
		}	
		
		@Test
		public void test5_UpdateMember() {
			String resourceURL = serviceURL + "/1"; //createNewMember
			logger.info("************* DEBUG: Junit TESTING: test5_UpdateMember ... :" + resourceURL);
			Client client = ClientBuilder.newClient();
			// Get Member
			Member member = client.target(resourceURL)
					.request().accept(MediaType.APPLICATION_JSON)
					.get().readEntity(Member.class);
			
			assertNotNull("REST Data Service failed!", member);
			logger.info(">>> Initial Member: " + member);
			
			// update and save Member
			member.setNumeMembru(member.getNumeMembru() + "_UPD_JSON");
			member = client.target(resourceURL)
					//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
					.request().accept(MediaType.APPLICATION_JSON)
					.put(Entity.entity(member, MediaType.APPLICATION_JSON))
					.readEntity(Member.class);
			
			logger.info(">>> Updated Member: " + member);
			
			assertNotNull("REST Data Service failed!", member);
		}	
}
