package org.app.service.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;


import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;
import org.app.service.entities.Internship;
import org.app.service.entities.Student;


public class InternshipStudentDataServiceEJB extends EntityRepositoryBase<Internship> implements InternshipStudentDataService, Serializable {
	private static Logger logger = Logger.getLogger(InternshipStudentDataServiceEJB.class.getName());
	
	@EJB // injected DataService
	private StudentDataService studentDataService; 
	// Local component-entity-repository
	private EntityRepository<Student> studentRepository;
	@PostConstruct
	public void init() {
		// local instantiation of local component-entity-repository
		studentRepository = new EntityRepositoryBase<Student>(this.em,Student.class);
		logger.info("POSTCONSTRUCT-INIT studentRepository: " + this.studentRepository);
	}
	
	// Aggregate factory method
		public Internship createNewInternship(Integer id){
			// create internship aggregate
			Internship internship = new Internship(null,"Audit Internship", new Date(), new Date(), 3, null);
			List<Student> studentsInternship = new ArrayList<>();
			
			Integer studentCount = 3;
			
			for (int i=0; i<=studentCount-1; i++){
				studentsInternship.add(new Student(null, "Popa Adrian" + internship.getIdInternship() + "." + i, 
						195052624, "popa.adrian@gmail.com",0752524, "Iasi"));
			}
			
			internship.setStudenti(studentsInternship);		
			// save internship aggregate
			this.add(internship);
			// return internship aggregate to service client
			return internship;
		}
		
		public Student getStudentById(Integer idStudent) {
			return studentRepository.getById(idStudent);
		}

		public String getMessage() {
			return "ProjectSprint DataService is working...";
		}
	
	
}
