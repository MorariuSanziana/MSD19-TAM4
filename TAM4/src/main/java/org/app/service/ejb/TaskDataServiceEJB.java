package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.app.service.entities.Task;


@Stateless @LocalBean
public class TaskDataServiceEJB implements TaskDataService {
	private static Logger logger = Logger.getLogger(TaskDataServiceEJB.class.getName());
	
	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public TaskDataServiceEJB() {
	}
	// Init after constructor
	@PostConstruct
	public void init(){
		logger.info("POSTCONSTRUCT-INIT : " + this.em);
	}	
	
	/* CRUD operations implementation */
	// CREATE or UPDATE	
	@Override
	public Task addTask(Task taskToAdd){
		em.persist(taskToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(taskToAdd);
		return taskToAdd;
	}
	
	// READ
	
	@Override
		public Task getTaskByID(Integer idTask) {
			return em.find(Task.class, idTask);
		}
	@Override
		public Collection<Task> getTasks(){
			List<Task> tasks = em.createQuery("SELECT t FROM Task t", Task.class)
					.getResultList();
			return tasks;
		}

		
		// REMOVE
	
	@Override
		public String removeTask(Task taskToDelete){
					taskToDelete = em.merge(taskToDelete);
					em.remove(taskToDelete);
					em.flush();
					return "True";
				}

		// Custom READ: custom query
	
	@Override
		public Task getTaskByDenumire(String denumireTask) {
			return em.createQuery("SELECT t FROM Task t WHERE t.Denumire = :Denumire", Task.class)
							.setParameter("Denumire", denumireTask)
							.getSingleResult();
				}	


				// Others
				@Override
				public String getMessage() {
					return "TaskServiceEJB is ON... ";
				}
}
