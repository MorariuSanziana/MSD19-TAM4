package org.app.service.ejb;

import javax.ejb.Remote;

import org.app.patterns.EntityRepository;
import org.app.service.entities.Internship;
import org.app.service.entities.Student;

@Remote
public interface InternshipStudentDataService extends EntityRepository<Internship> {

	// create aggregate entity: internship root with students as components
		Internship createNewInternship(Integer id);
		// Query method on student components
		Student getStudentById(Integer idStudent);
		// Other
		String getMessage();
}
