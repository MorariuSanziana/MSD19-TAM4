package org.app.service.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.app.service.entities.Student;
import org.app.service.entities.Team;
import org.app.patterns.EntityRepository;
import org.app.patterns.EntityRepositoryBase;

public class TeamStudentDataServiceEJB extends EntityRepositoryBase<Team>
		implements Serializable, TeamStudentDataService {

private static Logger logger = Logger.getLogger(TeamStudentDataServiceEJB.class.getName());
	
	@EJB // injected DataService
	private StudentDataService studentDataService; 
	// Local component-entity-repository
	private EntityRepository<Student> studentRepository;
	@PostConstruct
	public void init() {
		// local instantiation of local component-entity-repository
		studentRepository = new EntityRepositoryBase<Student>(this.em,Student.class);
		logger.info("POSTCONSTRUCT-INIT studentRepository: " + this.studentRepository);
		logger.info("POSTCONSTRUCT-INIT studentDataService: " + this.studentDataService);
	}
	
	// Aggregate factory method
			public Team createNewTeam(Integer id){
				// create team aggregate
				Team team = new Team(null, "Echipa1", 4, null, null);
				List<Student> studentsTeam = new ArrayList<>();
				
				Integer studentCount = 3;
				
				for (int i=0; i<=studentCount-1; i++){
					studentsTeam.add(new Student(null, "Popa Adrian" + team.getIdEchipa() + "." + i, 
							195052624, "popa.adrian@gmail.com",0752524, "Iasi"));
				}
				
				team.setStudenti(studentsTeam);		
				// save team aggregate
				this.add(team);
				// return team aggregate to service client
				return team;
			}
			
			public Student getStudentById(Integer idStudent) {
				return studentRepository.getById(idStudent);
			}

			public String getMessage() {
				return "TeamSprint DataService is working...";
			}
		
}
