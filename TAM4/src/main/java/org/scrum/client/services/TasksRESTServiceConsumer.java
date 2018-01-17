package org.scrum.client.services;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;


import org.app.service.entities.Task;

@Named @SessionScoped
public class TasksRESTServiceConsumer implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(TasksRESTServiceConsumer.class.getName());
	//
	private static String serviceURL = "http://localhost:8080/TAM4/resources/tasks";
	
	// private List<Project> projects = new ArrayList<Project>();
	
	public List<Task> getTasks() {
		logger.info(">>> REST Consumer: getTasks.");
		List<Task> tasks = new ArrayList<Task>();
		tasks.addAll(this.getAllTasks());
		return tasks;
	}

	@PostConstruct
	public void init() {
		//this.tasks.addAll(this.getAllTasks());
	}
	
	

	// RESTfull CRUD methods
	public Collection<Task> getAllTasks() {
		logger.info("DEBUG: TasksRESTServiceConsumer: getAllTasks ...");
		Collection<Task> tasks = ClientBuilder.newClient()
				.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Task>>(){});
		//projects.stream().forEach(System.out::println);
		return tasks;
	}

	public List<Task> addTask(Task task) {
		logger.info("DEBUG: TasksRESTServiceConsumer: addTask(): " + task);
		Client client = ClientBuilder.newClient();
		Collection<Task> tasks;
		
		tasks = client.target(serviceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(task, MediaType.APPLICATION_JSON))
				.readEntity(new GenericType<Collection<Task>>(){});
		
		// projects.stream().forEach(System.out::println);
		return new ArrayList<>(tasks);
	}	
	
	public Collection<Task> deleteTask(Task task) {
		logger.info("DEBUG: TasksRESTServiceConsumer: deleteTask: " + task);
		
		String resourceURL = serviceURL + "/";
		
		Client client = ClientBuilder.newClient();
		client.target(resourceURL + task.getIdTask()).request().delete();
		
		Collection<Task> tasksAfterDelete = client.target(serviceURL)
				.request().get()
				.readEntity(new GenericType<Collection<Task>>(){});	
		
		tasksAfterDelete.stream().forEach(System.out::println);
		return tasksAfterDelete;
	}
	
	public Task getByID(Integer idTask) {
		String resourceURL = serviceURL + "/" + idTask;
		logger.info("DEBUG: TasksRESTServiceConsumer: getByID: " + idTask);
		
		Task task = ClientBuilder.newClient().target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Task.class);
		
		assertNotNull("REST Data Service failed!", task);
		logger.info(">>>>>> DEBUG: REST Response ..." + task);
		
		return task;
	}	
	
	public Task updateTask(Task task) {
		String resourceURL = serviceURL + "/" + task.getIdTask();
		logger.info("DEBUG: TasksRESTServiceConsumer: task: " + task);
		Client client = ClientBuilder.newClient();

		// Get project server version
		Task serverTask = client.target(resourceURL)
				.request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(Task.class);
		
		logger.info(">>> Server-side Task: " + serverTask);
		
		task = client.target(resourceURL)
				//.request().accept(MediaType.APPLICATION_XML).header("Content-Type", "application/xml")
				.request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(task, MediaType.APPLICATION_JSON))
				.readEntity(Task.class);
		
		logger.info(">>> Updated Project: " + task);
		return task;
	}
	
	public Task createTask() {
		String resourceURL = serviceURL + "/new/"; //createNewProject
		logger.info("DEBUG: TasksRESTServiceConsumer: CreateTask ");

		Task task = ClientBuilder.newClient().target(resourceURL + 999)
					.request().accept(MediaType.APPLICATION_JSON)
					.post(null).readEntity(Task.class);
		logger.info(">>> Created/posted Project: " + task);
		
		return task;
	}	
}


