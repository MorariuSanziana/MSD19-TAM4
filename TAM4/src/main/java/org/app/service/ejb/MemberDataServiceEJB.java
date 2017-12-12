package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.app.service.entities.Member;


@Stateless @LocalBean
public class MemberDataServiceEJB implements MemberDataService {
	private static Logger logger = Logger.getLogger(MemberDataServiceEJB.class.getName());
	
	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public MemberDataServiceEJB() {
	}
	// Init after constructor
	@PostConstruct
	public void init(){
		logger.info("POSTCONSTRUCT-INIT : " + this.em);
	}		
	
	/* CRUD operations implementation */
	// CREATE or UPDATE
	@Override
	public Member addMember(Member memberToAdd){
		em.persist(memberToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(memberToAdd);
		return memberToAdd;
	}
	
	// READ
	@Override
		public Member getMemberByID(Integer idMembru) {
		return em.find(Member.class, idMembru);
		}	
		@Override
		
		public Collection<Member> getMembers(){
			List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
					.getResultList();
			return members;
		}
	
		// REMOVE
		@Override
				public String removeMember(Member memberToDelete){
					memberToDelete = em.merge(memberToDelete);
					em.remove(memberToDelete);
					em.flush();
					return "True";
				}

				// Custom READ: custom query
				@Override		
				public Member getMemberByNume(String numeMembru) {
					return em.createQuery("SELECT m FROM Member m WHERE m.Nume = :Nume", Member.class)
							.setParameter("Nume", numeMembru)
							.getSingleResult();
				}	
	
				// Others
				@Override
				public String getMessage() {
					return "MemberServiceEJB is ON... ";
				}
	

}
