package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.app.service.entities.Project;



@Stateless @LocalBean
public class ProjectDataServiceEJB implements ProjectDataService {
	private static Logger logger = Logger.getLogger(ProjectDataServiceEJB.class.getName());
	
	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public ProjectDataServiceEJB() {
	}
	// Init after constructor
	@PostConstruct
	public void init(){
		logger.info("POSTCONSTRUCT-INIT : " + this.em);
	}		

	
	/* CRUD operations implementation */
	// CREATE or UPDATE
	@Override
	public Project addProject(Project projectToAdd){
		em.persist(projectToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(projectToAdd);
		return projectToAdd;
	}
	
	// READ
	@Override
		public Project getProjectByID(Integer idProiect) {
			return em.find(Project.class, idProiect);
		}	
		@Override
		public Collection<Project> getProjects(){
			List<Project> projects = em.createQuery("SELECT p FROM Project p", Project.class)
					.getResultList();
			return projects;
		}
		
		// REMOVE
		@Override
				public String removeProject(Project projectToDelete){
					projectToDelete = em.merge(projectToDelete);
					em.remove(projectToDelete);
					em.flush();
					return "True";
				}
		
				// Custom READ: custom query
		@Override
				public Project getProjectByNume(String numeProiect) {
					return em.createQuery("SELECT p FROM Project p WHERE p.Nume = :Nume", Project.class)
							.setParameter("Nume", numeProiect)
							.getSingleResult();
				}	


				// Others
				@Override
				public String getMessage() {
					return "ProjectServiceEJB is ON... ";
				}

}
