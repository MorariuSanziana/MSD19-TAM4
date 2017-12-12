package org.app.service.ejb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.EJB;


import org.app.service.ejb.TaskDataService;
import org.app.service.ejb.TaskDataServiceEJB;

import org.app.service.entities.Task;
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
public class TestTaskDataServiceEJBArq {
	private static Logger logger = 
			Logger.getLogger(TestTaskDataServiceEJBArq.class.getName());
	
	@EJB // EJB DataService Ref
	private static TaskDataService service;
	
	@Deployment // Arquilian infrastructure
	public static Archive<?> createDeployment() {
	        return ShrinkWrap
	                .create(WebArchive.class, "msd-test.war")
	                .addPackage(Task.class.getPackage())
	                .addClass(TaskDataService.class)
	                .addClass(TaskDataServiceEJB.class)
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
	public void test4_GetTasks() {
		logger.info("DEBUG: Junit TESTING: testGetTasks ...");
		
		Collection<Task> tasks = service.getTasks();
		assertTrue("Fail to read tasks!", tasks.size() > 0);
	}

	@Test
	public void test3_AddTask() {
		logger.info("DEBUG: Junit TESTING: testAddTask ...");
		
			
		 
		Integer tasksToAdd = 1;
		for (int i=1; i <= tasksToAdd; i++){
			
			service.addTask(new Task(null, "Implementare clase Java", "Implementare","In progres", new Date(), 2 , 1, "descriere"));
		}
		Collection<Task> tasks = service.getTasks();
		assertTrue("Fail to add tasks!", tasks.size() == tasksToAdd);
	}

	@Test
	public void test2_DeleteTask() {
		logger.info("DEBUG: Junit TESTING: testDeleteTask ...");
		
		Collection<Task> tasks = service.getTasks();
		for (Task t: tasks)
			service.removeTask(t);
		Collection<Task> tasksAfterDelete = service.getTasks();
		assertTrue("Fail to read tasks!", tasksAfterDelete.size() == 0);
	}	
}
