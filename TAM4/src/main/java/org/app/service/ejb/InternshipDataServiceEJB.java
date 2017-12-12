package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.app.service.entities.Internship;


@Stateless @LocalBean
public class InternshipDataServiceEJB implements InternshipDataService {
	private static Logger logger = Logger.getLogger(InternshipDataServiceEJB.class.getName());
      
	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public InternshipDataServiceEJB() {
	}
	// Init after constructor
	@PostConstruct
	public void init(){
		logger.info("POSTCONSTRUCT-INIT : " + this.em);
	}		

	/* CRUD operations implementation */
	// CREATE or UPDATE		
	@Override
	public Internship addInternship(Internship internshipToAdd){
		em.persist(internshipToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(internshipToAdd);
		return internshipToAdd;
	}
	
	// READ
	@Override
	public Internship getInternshipByID(Integer IdInternship) {
		
		return em.find(Internship.class, IdInternship);
	}	
	@Override	
	public Collection<Internship> getInternships(){
		List<Internship> internships = em.createQuery("SELECT i FROM Internship i", Internship.class)
				.getResultList();
		return internships;
	}
	
	// REMOVE
	@Override
		public String removeInternship(Internship internshipToDelete){
			internshipToDelete = em.merge(internshipToDelete);
			em.remove(internshipToDelete);
			em.flush();
			return "True";
		}
	
		// Custom READ: custom query
		@Override
		public Internship getInternshipByDenumire(String denumireInternship) {
			return em.createQuery("SELECT i FROM Internship i WHERE i.Denumire = :Denumire", Internship.class)
					.setParameter("Denumire", denumireInternship)
					.getSingleResult();
		}	

		// Others
		@Override
		public String getMessage() {
			return "InternshipServiceEJB is ON... ";
		}

}
