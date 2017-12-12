package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.app.service.entities.Team;

@Stateless @LocalBean
public class TeamDataServiceEJB implements TeamDataService {
	private static Logger logger = Logger.getLogger(TeamDataServiceEJB.class.getName());
	
	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public TeamDataServiceEJB() {
	}
	// Init after constructor
	@PostConstruct
	public void init(){
		logger.info("POSTCONSTRUCT-INIT : " + this.em);
	}	
	
	/* CRUD operations implementation */
	// CREATE or UPDATE
	
	@Override
	public Team addTeam(Team teamToAdd){
		em.persist(teamToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(teamToAdd);
		return teamToAdd;
	}
	
	// READ
	
		@Override
		public Team getTeamByID(Integer idEchipa) {
			return em.find(Team.class, idEchipa);
		}
	@Override
		public Collection<Team> getTeams(){
			List<Team> teams = em.createQuery("SELECT t FROM Team t", Team.class)
					.getResultList();
			return teams;
		}

		
	// REMOVE

	@Override
		public String removeTeam(Team teamToDelete){
					teamToDelete = em.merge(teamToDelete);
					em.remove(teamToDelete);
					em.flush();
					return "True";
				}
	// Custom READ: custom query

	@Override
		public Team getTeamByNume(String numeEchipa) {
				return em.createQuery("SELECT t FROM Team t WHERE t.Nume = :Nume", Team.class)
							.setParameter("Nume", numeEchipa)
							.getSingleResult();
				}	

	// Others
	@Override
		public String getMessage() {
			return "TeamServiceEJB is ON... ";
				}


}
