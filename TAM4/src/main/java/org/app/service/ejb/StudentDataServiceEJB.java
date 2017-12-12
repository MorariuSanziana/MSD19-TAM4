package org.app.service.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.app.service.entities.Student;


@Stateless @LocalBean
public class StudentDataServiceEJB implements StudentDataService {
	private static Logger logger = Logger.getLogger(StudentDataServiceEJB.class.getName());

	/* DataService initialization */
	// Inject resource 
	@PersistenceContext(unitName="MSD")
	private EntityManager em;
	// Constructor
	public StudentDataServiceEJB() {
	}
	// Init after constructor
	@PostConstruct
	public void init(){
		logger.info("POSTCONSTRUCT-INIT : " + this.em);
	}		

	/* CRUD operations implementation */
	// CREATE or UPDATE
	
	@Override
	public Student addStudent(Student studentToAdd){
		em.persist(studentToAdd);
		em.flush();
		// transactions are managed by default by container
		em.refresh(studentToAdd);
		return studentToAdd;
	}
	
	// READ
	
	@Override
		public Student getStudentByID(Integer idStudent) {
			return em.find(Student.class, idStudent);
		}

	@Override
		public Collection<Student> getStudents(){
			List<Student> students = em.createQuery("SELECT s FROM Student s", Student.class)
					.getResultList();
		return students;
		}
	
		// REMOVE
	
	@Override
		public String removeStudent(Student studentToDelete){
					studentToDelete = em.merge(studentToDelete);
					em.remove(studentToDelete);
					em.flush();
			return "True";
				}
				
		// Custom READ: custom query
	
	@Override
		public Student getStudentByNume(String numeStudent) {
				return em.createQuery("SELECT s FROM Student s WHERE s.Nume = :Nume", Student.class)
							.setParameter("Nume", numeStudent)
							.getSingleResult();
				}	
	

		// Others
	@Override
		public String getMessage() {
			return "StudentServiceEJB is ON... ";
				}
	@Override
	public Student getStudentByName(String numeStudent) {
		// TODO Auto-generated method stub
		return null;
	}
				

}
