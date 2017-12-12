package org.app.service.ejb;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.Student;
import org.app.service.entities.Team;

@Remote
public interface TeamStudentDataService extends EntityRepository<Team> {

	// create aggregate entity: team root with students as components
			Team createNewTeam(Integer id);
			// Query method on student components
			Student getStudentById(Integer idStudent);
			// Other
			String getMessage();
}
